import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import multivariate_normal
from sklearn.model_selection import train_test_split

# Load the dataset with region information
print("Loading heart_disease_with_region.csv ...")
df = pd.read_csv('heart_disease_with_region.csv')

# Keep only rows where our selected features and target are present
df = df.dropna(subset=['age', 'trestbps', 'chol', 'thalach', 'target']).reset_index(drop=True)

# Choose the four continuous features we will use
features = ['age', 'trestbps', 'chol', 'thalach']
X = df[features].values
y = df['target'].values
regions = df['region'].values

print(f"Final dataset: {len(df)} samples with features {features}")

# Split into training and test sets (80/20)
X_train, X_test, y_train, y_test, regions_train, regions_test = train_test_split(
    X, y, regions, test_size=0.2, random_state=42, stratify=y)

# Compute MLE parameters for multivariate Gaussian (mean and covariance)
def mle_gaussian_params(data):
    mean = np.mean(data, axis=0)
    cov = np.cov(data.T, bias=True)   # bias=True gives the true MLE estimator
    return mean, cov

group_0_train = X_train[y_train == 0]
group_1_train = X_train[y_train == 1]
mu0, cov0 = mle_gaussian_params(group_0_train)
mu1, cov1 = mle_gaussian_params(group_1_train)

print(f"Healthy MLE mean: {mu0.round(2)}")
print(f"Diseased MLE mean: {mu1.round(2)}")

# MAP prediction function using regional priors
def predict_map(x, prior0, prior1):
    like0 = multivariate_normal.pdf(x, mu0, cov0)
    like1 = multivariate_normal.pdf(x, mu1, cov1)
    post0 = like0 * prior0
    post1 = like1 * prior1
    return 1 if post1 > post0 else 0

# Calculate actual prior probabilities for each region from the training set
priors = {}
for reg in np.unique(regions_train):
    mask = (regions_train == reg)
    p1 = y_train[mask].mean()          # disease prevalence in this region
    priors[reg] = (1 - p1, p1)

# Add an overall prior for comparison
priors['Overall'] = (1 - y_train.mean(), y_train.mean())

# Test accuracy with different regional priors
results = {}
for reg, (p0, p1) in priors.items():
    y_pred = np.array([predict_map(x, p0, p1) for x in X_test])
    acc = (y_pred == y_test).mean()
    results[reg] = acc
    print(f"{reg} prior (disease rate={p1:.3f}) → Test Accuracy: {acc*100:.2f}%")

# Visualization: marginal Gaussian fits for all four features
plt.figure(figsize=(12, 8))
for i, feat in enumerate(features):
    plt.subplot(2, 2, i + 1)
    plt.hist(X_train[y_train == 0][:, i], bins=30, density=True, alpha=0.5, color='g', label='Healthy')
    plt.hist(X_train[y_train == 1][:, i], bins=30, density=True, alpha=0.5, color='r', label='Diseased')
    x = np.linspace(X_train[:, i].min(), X_train[:, i].max(), 200)
    plt.plot(x, multivariate_normal.pdf(x.reshape(-1, 1), mu0[i], cov0[i, i]), 'g-', lw=2, label='MLE Healthy')
    plt.plot(x, multivariate_normal.pdf(x.reshape(-1, 1), mu1[i], cov1[i, i]), 'r-', lw=2, label='MLE Diseased')
    plt.title(feat)
    plt.legend()
plt.suptitle('MLE Gaussian Fits (Marginal) across Features')
plt.tight_layout()
plt.savefig('mle_gaussian_fits.png', dpi=300)
plt.show()

# Save the final cleaned dataset for submission
df.to_csv('heart_disease_with_region.csv', index=False)
print("All experiments completed. CSV and plot saved.")