// SEHH2042 SEHS2042 In-class Exercises (Tutorial Work)
// Program template file
// Do not modify the given codes

// =======================================
// Insert more header files when necessary
// =======================================
#include <iostream>
#include <iomanip>
#include <cmath>
using namespace std;

double hypoten(double, double);
void sumAvg(int, int, int&, float&);
double calcPI(int);
void printBinary(int);

void showInfo()
{
	// Fill in your personal particulars below
	cout << "Name      : Li Ming Chun, Simon\n";
	cout << "Student ID: 23097293A\n";
	cout << "Class     : 103C\n";
	cout << "T6 submission\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	double side1, side2;
	cout << "Please enter the length of first side: ";
	cin >> side1;
	cout << "Please enter the length of second side: ";
	cin >> side2;
	cout << "Hypotenuse of a " << side1 << " by " << side2
		<< " right triangle is " << hypoten(side1, side2);
}

double hypoten(double x, double y) {
	double z;
	z = sqrt(x * x + y * y);
	return z;
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	int lower, upper, sum;
	float average;
	cout << "Enter the lower bound: ";
	cin >> lower;
	cout << "Enter the upper bound: ";
	cin >> upper;
	sumAvg(lower, upper, sum, average);
	cout << "From " << lower << " to " << upper << ":\n";
	cout << "Sum = " << sum << endl;
	cout << "Average = " << average << endl;
}

void sumAvg(int n1, int n2, int& sum, float& avg) {
	int count = 0;
	sum = 0;
	for (int k = n1; k <= n2; k++) {
		sum = sum + k;
		count++;
	}
	avg = (float)sum / count;
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	int terms;
	double result;

	cout << "How many terms for PI: ";
	cin >> terms;

	result = calcPI(terms);

	cout << fixed << setprecision(15);

	cout << "PI with " << terms << " terms is " << result << endl;
}

double calcPI(int n) {
	if (n == 1) {
		return 4.0 / 1;
	}
	else {
		if (n % 2 == 1) {
			return calcPI(n - 1) + 4.0 / (2 * n - 1);
		}
		else {
			return calcPI(n - 1) - 4.0 / (2 * n - 1);
		}
	}
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	int num;
	cout << "Input a positive decimal integer: ";
	cin >> num;
	cout << "The binary version is ";
	printBinary(num);
	cout << endl;
}

void printBinary(int a) {
	if (a / 2 != 0) {
		printBinary(a / 2);
	}
	cout << a % 2;
}

// IMPORTANT: DO NOT MODIFY main()
int main()
{
	char prog_choice;

	do {
		cout << "\n\n";
		cout << "Program Selection Menu" << endl;
		cout << "---------------------------------------" << endl;
		cout << "Enter question number ('q' to quit): ";
		cin >> prog_choice;
		cout << "\n\n";

		switch (prog_choice) {
		case '0': showInfo(); break;
		case '1': Q1(); break;
		case '2': Q2(); break;
		case '3': Q3(); break;
		case '4': Q4(); break;
		case 'q': break;
		default:
			cout << "No such question " << prog_choice << endl;
			break;
		}
	} while (prog_choice != 'q');

	cout << "Program terminates. Good bye!" << endl;
	return 0;
}
// END