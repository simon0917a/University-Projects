#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <iomanip>
#include <cmath>

using namespace std;

struct Transaction_History {
    bool auto_top_up;
    int m_spt; //money_spent
    int tokensUsed; // tokens spent or purchased
    string serviceName;
};

struct User {
    string userID;
    char type;         // 'T' for Trial Account, 'F' for Full Account, 'S' for Student Account
    int tokenBalance;  // number of token
    int originalTokenBalance;
    char autoTopUp;    // 'Y' or 'N'
    vector<Transaction_History> tH;
};

class TokenManagementSystem {
private:
    vector<User> users;
    int user_id = -1;
    bool isDataLoaded = false;

public:
    // R0: Display a Welcome Message when the porgram start
    void displayWelcomeMessage() {
        cout << "Welcome to the AI Service Token Management System!\n*** Created By Group15 ***\n";
    }

    // R1: Load Starting Data
    //The predefined users data into the system 
    void loadStartingData() {

        users.clear();
        //Add the predefined users data into the system 
        // { "UserID" , 'Type' , TokenBalance , 'AutoTopUp' } 
        // after run one user , move to next line for next user (R2)
        users.push_back({ "SkyWalker", 'T', 20,20, 'N',{} });
        users.push_back({ "Ocean123", 'T', 35,35, 'N',{} });
        users.push_back({ "Forest99", 'T', 6,6, 'Y',{} });
        users.push_back({ "Valley777", 'F', 10,10, 'Y',{} });
        users.push_back({ "Desert2022", 'F', 25,25, 'N',{} });
        users.push_back({ "River456", 'F', 20,20, 'Y',{} });
        users.push_back({ "Blaze2023", 'F', 100,100, 'N',{} });
        users.push_back({ "Meadow888", 'S', 40,40, 'Y',{} });
        users.push_back({ "Galaxy", 'S', 15,15, 'Y',{} });
        users.push_back({ "Storm2024", 'S', 30,30, 'N',{} });

        cout << "Starting data loaded successfully.\n";
        isDataLoaded = true; //R3
    }


    //R2:Display all user record
    void showUserRecords() {

        //check isDataLoaded 
        if (isDataLoaded == false) {
            cout << "Error! Please load the starting data first.\n";
            return;
        }
        sort(users.begin(), users.end(), [](const User& a, const User& b) {
            return a.userID < b.userID;
            });

        // setw to display different users in a tabular format 
        cout << left << setw(15) << "UserID" << setw(15) << "Type" << setw(15) << "TokenBalance" << setw(15) << "AutoTopUp" << endl;


        for (const auto& user : users) {
            cout << left << setw(15) << user.userID << setw(15) << user.type << setw(15) << user.tokenBalance << setw(15) << user.autoTopUp << endl;
        }
    }

    //R3 (Edit Users)
    void editUsers() {

        string newUserID;
        string YesNo;
        string inputtype, inputautoTopUp;
        char type, autoTopUp;
        int tokenBalance;
        bool userFound = false;

        //Check whether the starting data was loaded
        if (isDataLoaded == false) {
            cout << "Error! Please load the starting data first." << endl;
            return; //return to main menu
        }

        cout << "Enter User ID: ";
        cin >> newUserID; //Store the user ID

        for (int i = 0; i < users.size(); i++) {
            //Check whether the user exists
            if (newUserID == users[i].userID) {
                userFound = true; //The user exists
                cout << "User found" << endl;
                cout << "------------" << endl;
                //Show the found user's record
                cout << "[UserID]: " << users[i].userID << endl;
                cout << "[Type]: " << users[i].type << endl;
                cout << "[TokenBalance]: " << users[i].tokenBalance << endl;
                cout << "[AutoTopUp]: " << users[i].autoTopUp << endl;

                //Ask the user if they want to delete the found user
                cout << "Delete the User(Yes/No)? ";
                cin >> YesNo;
                //Delete the found user
                if (YesNo == "Yes") {
                    cout << "Deleting user..." << endl;
                    users.erase(users.begin() + i);
                    cout << "User deleted successfully." << endl;
                    cout << "Back to the Main Menu." << endl;
                    return; //return to main menu
                }
                //Delete cancelled
                else if (YesNo == "No") {
                    cout << "Delete Cancelled." << endl;
                    cout << "Back to the Main Menu." << endl;
                    return; //return to main menu
                }
                //Delete cancelled (the user input incorrectly)
                else {
                    cout << "Invalid input." << endl;
                    cout << "Back to the Main Menu." << endl;
                    return; //return to main menu
                }
            }
        }

        //Add a new user if the input user does not exist
        if (userFound == false) {
            cout << "User not found. Adding new user." << endl;
            //Ask the user to input the new user's type, token balance, and auto top-up
            cout << "Enter Type (T: Trial, F: Full, S: Student): ";
            cin >> inputtype;

            //Check whether the user input correctly
            for (int i = 0; i <= 2; i++) {
                if (inputtype[0] == 'T' || inputtype[0] == 'F' || inputtype[0] == 'S') {
                    break;
                }
                else {
                    if (i == 2) {
                        cout << "Error! Back to the Main Menu." << endl;
                        return; //return to main menu
                    }
                    //Ask the user to input type again
                    cout << "Invalid input. Please input again." << endl;
                    cout << "Enter Type (T: Trial, F: Full, S: Student): ";
                    cin >> inputtype;
                }
            }

            cout << "Enter Token Balance (a non-negative integer): ";
            cin >> tokenBalance;

            //Check whether the user input correctly
            for (int i = 0; i <= 2; i++) {
                if (cin.fail() || tokenBalance < 0) {
                    cin.clear();
                    cin.ignore(1000, '\n');
                    if (i == 2) {
                        cout << "Error! Back to the Main Menu." << endl;
                        return; //return to main menu
                    }
                    //Ask the user to input token balance again
                    cout << "Invalid input. Please input again." << endl;
                    cout << "Enter Token Balance (a non-negative integer): ";
                    cin >> tokenBalance;
                }
                else {
                    break;
                }
            }

            cout << "Auto Top-up (Y/N): ";
            cin >> inputautoTopUp;

            //Check whether the user input correctly
            for (int i = 0; i <= 2; i++) {
                if (inputautoTopUp[0] == 'Y' || inputautoTopUp[0] == 'N') {
                    break;
                }
                else {
                    if (i == 2) {
                        cout << "Error! Back to the Main Menu." << endl;
                        return; //return to main menu
                    }
                    //Ask the user to input auto top-up again
                    cout << "Invalid input. Please input again." << endl;
                    cout << "Auto Top-up (Y/N): ";
                    cin >> inputautoTopUp;
                }
            }
            type = inputtype[0];
            autoTopUp = inputautoTopUp[0];
            users.push_back({ newUserID, type, tokenBalance, tokenBalance, autoTopUp,{} });
            cout << "User added successfully." << endl;
            cout << "Back to the Main Menu." << endl;
            return; //return to main menu
        }
    }

    //R4
    void enterUserView() {
        int option;
        int tokensRequired = 0;
        string curr_user;

        //Check whether the starting data was loaded
        if (isDataLoaded == false) {
            cout << "Error! Please load the starting data first." << endl;
            return; //return to main menu
        }

        // Step 1: Ask for the User ID
        cout << "Input your User ID: ";
        cin >> curr_user;
        // Find user
        user_id = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users[i].userID == curr_user) {
                user_id = i;
                break;
            }
        }

        if (user_id == -1) {
            cout << "User not found.\n";
            cout << "Return to the Main Menu.\n";
            return;
        }

        // Step 2: Display User View Menu
        while (true) {
            cout << "Action for User ID: " << users[user_id].userID << endl;
            cout << "***** User View Menu *****\n";
            cout << "[1] Select AI Service\n";
            cout << "[2] Purchase Tokens\n";
            cout << "[3] Edit Profile\n";
            cout << "[4] Show Transaction History\n";
            cout << "[5] Return to Main Menu\n";
            cout << "\n";

            while (true) {
                cout << "Option (1 - 5): ";
                cin >> option;

                if (cin.fail() || option < 1 || option > 5) {
                    cin.clear();
                    cin.ignore(1000, '\n');
                    cout << "Invalid option, Please input again.\n";
                }
                else {
                    break;
                }
            }

            switch (option) {
            case 1: {
                // AI service selection
                int serviceOption;
                cout << "***** Select AI service *****\n";
                cout << "[1] Image Recognition\n";
                cout << "[2] Speech-to-text transcription\n";
                cout << "[3] Predictive Analysis\n";
                cout << "[4] Natural Language Processing (NLP)\n";

                while (true) {
                    cout << "Option (1 - 4): ";
                    cin >> serviceOption;

                    if (cin.fail() || serviceOption < 1 || serviceOption > 4) {
                        cin.clear();
                        cin.ignore(1000, '\n');
                        cout << "Invalid option, Please input again.\n";
                    }
                    else {
                        break;
                    }
                }

                switch (serviceOption) {
                case 1: {  // Image Recognition
                    float imageSize;
                    cout << "Enter Image Size (in MB): ";
                    cin >> imageSize;

                    if (users[user_id].type == 'T') {
                        if (imageSize < 3) {
                            tokensRequired = 5;  // Trial accounts, under 3MB
                        }
                        else {
                            cout << "Trial accounts cannot process images over 3 MB.\n";
                            continue;
                        }
                    }
                    else if (users[user_id].type == 'F') {
                        tokensRequired = (imageSize < 3) ? 5 : 8;  // 5 tokens for <3MB, 8 tokens for >=3MB
                    }
                    else if (users[user_id].type == 'S') {
                        tokensRequired = (imageSize < 3) ? 5 : 7;  // 5 tokens for <3MB, 7 tokens for >=3MB
                    }

                    users[user_id].tH.push_back({
                        false, 0, tokensRequired, "Image Recognition"
                        });
                    break;
                }
                case 2: {  // Speech-to-text transcription
                    int audioMinutes;
                    cout << "Enter length of the audio (in minutes): ";
                    cin >> audioMinutes;

                    if (audioMinutes <= 3) {
                        tokensRequired = audioMinutes * 2;  // 2 tokens per minute for first 3 minutes
                    }
                    else {
                        tokensRequired = 6 + (audioMinutes - 3) * 3;  // 6 tokens for first 3 minutes, then 3 tokens per additional minute
                    }

                    users[user_id].tH.push_back({
                        false, 0, tokensRequired, "Speech-to-text transcription"
                        });
                    break;
                }
                case 3: {  // Predictive Analysis
                    tokensRequired = 10;  // Flat 10 tokens for all users

                    users[user_id].tH.push_back({
                        false, 0, tokensRequired, "Predictive Analysis"
                        });
                    break;
                }
                case 4: {  // Natural Language Processing (NLP)
                    int textLength;
                    cout << "Enter the number of characters in the text: ";
                    cin >> textLength;

                    if (users[user_id].type == 'T' && textLength > 2500) {
                        cout << "Trial accounts cannot process texts over 2500 characters.\n";
                        continue;
                    }

                    tokensRequired = ceil(textLength / 500.0);  // Round up to the nearest token
                    users[user_id].tH.push_back({
                        false, 0, tokensRequired, "Natural Language Processing (NLP)"
                        });
                    break;
                }
                default:
                    cout << "Invalid service option. ";
                    cout << "Return to User View Menu.\n";
                    continue;  // Invalid service option
                }

                if (users[user_id].tokenBalance >= tokensRequired) {
                    users[user_id].tokenBalance -= tokensRequired;  // Deduct tokens
                    cout << "Service completed. " << tokensRequired << " tokens were deducted.\n";
                }
                else if (users[user_id].autoTopUp == 'Y') {
                    int topUpAmount = (tokensRequired - users[user_id].tokenBalance + 19) / 20 * 20;  // Auto top-up in multiples of 20
                    users[user_id].tokenBalance += topUpAmount;  // Add the top-up amount
                    cout << "Auto top-up enabled. Added " << topUpAmount << " tokens.\n";

                    users[user_id].tH.push_back({
                        true, topUpAmount * 2, topUpAmount, "Auto Top-Up"
                        });
                    users[user_id].tokenBalance -= tokensRequired;  // Now deduct the required tokens
                    cout << "Service completed. " << tokensRequired << " tokens were deducted.\n";
                }
                else {
                    cout << "Insufficient tokens and auto top-up is disabled. Service cannot be completed.\n";
                }

                break;
            }
            case 2: {
                PurchaseTokens();  // Purchase Tokens
                break;
            }
            case 3: {
                EditProfile();  // Edit Profile
                break;
            }
            case 4: {
                ShowTransaction();  // Show Transaction History
                break;
            }
            case 5:
                return;  // Return to main menu
            default:
                cout << "Invalid option, please try again.\n";
            }
        }
    }

    void PurchaseTokens() {
        float money = 0;
        int money_int = 0;
        while (true) {
            cout << "***** Purchase Tokens *****\n";
            cout << "[Each token costs $2]\n";
            cout << "\n";
            cout << "Input the amount of money (an even integer value): ";
            cin >> money;// User inputs the amount of money
            money_int = money; // Convert float to int for validation
            // Validate the input: must be an even integer and greater than 0
            if (cin.fail() || money != money_int || money_int % 2 == 1 || money_int <= 0) {
                cin.clear();
                cin.ignore(1000, '\n');
                cout << "Invalid Input. Please input again.\n";// Prompt for valid input
                continue;// Restart the loop for valid input
            }

            int o = users[user_id].tokenBalance;// Store the original token balance
            // Record the transaction and update token balance
            users[user_id].tH.push_back({ false, money_int, money_int / 2, "" });
            users[user_id].tokenBalance += money_int / 2;// Add tokens based on money spent
            cout << "Balance change from ";
            cout << o;
            cout << " to ";
            cout << users[user_id].tokenBalance << endl;// Display balance change
            return;
        }
    }

    void EditProfile() {
        int choose = 0;// Variable to store user choice for editing
        char type;// Variable for account type
        int error = 0;// Error counter for invalid inputs
        while (true) {
            cout << "***** Edit Profile *****\n";
            cout << "[1] Account Type\n";
            cout << "[2] Auto Top-up\n";
            cout << "\n";
            cout << "Input: ";
            cin >> choose; // Get user choice for editing
            switch (choose) {
            case 1:// Edit account type
                error = 0;// Reset error counter
                while (true) {
                    if (error >= 3) { // Allow up to 3 invalid attempts
                        cout << "ERROR: 3 invalid option\n";
                        return;// Exit if too many invalid attempts
                    }
                    cout << "***** Edit Account Type *****\n";
                    cout << "Input: ";
                    cin >> type;// Get new account type
                    // Validate account type
                    if (type == 'T' || type == 'F' || type == 'S') {
                        //valid
                        char o = users[user_id].type;// Store old account type
                        users[user_id].type = type;// Update account type
                        cout << "Account Type change from ";
                        cout << o;
                        cout << " to ";
                        cout << type << endl;
                        return;// Exit after successful change
                    }
                    else {
                        error++;// Increment error counter on invalid input

                    }
                }
                break;
            case 2:// Edit auto top-up preference
                error = 0; // Reset error counter
                while (true) {
                    if (error >= 3) { // Allow up to 3 invalid attempts
                        cout << "ERROR: 3 invalid option\n";
                        return; // Exit if too many invalid attempts
                    }
                    cout << "***** Edit Auto Top-up *****\n";
                    cout << "Input: ";
                    cin >> type;// Get new auto top-up preference
                    // Validate auto top-up input
                    if (type == 'Y' || type == 'N') {
                        //valid
                        char o2 = users[user_id].autoTopUp; // Store old auto top-up preference
                        users[user_id].autoTopUp = type;// Update auto top-up preference
                        cout << "Auto Top-up change from ";
                        cout << o2;
                        cout << " to ";
                        cout << type << endl;
                        return;// Exit after successful change
                    }
                    else {
                        error++;// Increment error counter on invalid input

                    }
                }
                break;
            default:
                cout << "Invalid option, please try again.\n";// Handle invalid menu option
                break;
            }
        }

    }

    void ShowTransaction() {

        int MoneySpent = 0;
        int TokensPurchased = 0;
        int TokensUsed = 0;
        int TokenChange = 0;

        //Check whether the current user had any transaction history
        if (users[user_id].tH.size() == 0) { //the current user did not have any transaction history
            cout << "No Record Found." << endl;
            return; //return to User View Menu
        }

        cout << "Transaction History for User: " << users[user_id].userID << endl;

        for (const auto& transaction : users[user_id].tH) {
            if (transaction.serviceName.size() == 0) { //Check whether the current user select any AI service
                TokensPurchased += transaction.tokensUsed; //Add the number of purchased tokens
                MoneySpent += transaction.m_spt; //Add the money spent on purchasing tokens
            }

            //Show the current user's payment history for AI services
            else {
                cout << "***** Paying for an AI Service ******" << endl;
                cout << "Service Name: " << transaction.serviceName << endl;
                cout << "Tokens Spent: " << transaction.tokensUsed << endl;

                //Show the extra money paid for purchasing tokens if the current user has Auto Top-up option
                if (transaction.auto_top_up) {
                    cout << "Auto Top-Up: $" << transaction.m_spt << endl;
                }
                TokensUsed += transaction.tokensUsed; //Add the money spent on purchasing tokens
            }
        }

        //Show the number of tokens purchased and money spent
        if (TokensPurchased > 0) {
            cout << "***** Purchasing Tokens *****" << endl;
            cout << "Tokens Purchased: " << TokensPurchased << endl;
            cout << "Money Spent: $" << MoneySpent << endl;
        }

        TokenChange = users[user_id].tokenBalance - users[user_id].originalTokenBalance;

        //Show the summary of token balance and total money spent for the current user
        cout << "***** Summary *****" << endl;
        cout << "Original Token Balance: " << users[user_id].originalTokenBalance << endl;
        cout << "Final Token Balance: " << users[user_id].tokenBalance << endl;
        if (TokenChange > 0) {
            cout << "Change in Token Balance: +" << TokenChange << endl;
        }
        else {
            cout << "Change in Token Balance: " << TokenChange << endl;
        }
        cout << "Total Money Spent on Tokens: $" << MoneySpent << endl;
    }

    //R5
    void showSystemUsage() const {
        const double TOKEN_COST = 2.0; //constant token cost
        //formating
        cout << "\nSystem Usage Summary:\n";
        cout << setw(30) << "User ID"
            << setw(20) << "Total Tokens Spent"
            << setw(30) << "Total Money Spent$\n";
        cout << string(80, '-') << endl;
        //initialize the variable
        int overallTokensSpent = 0;
        double overallMoneySpent = 0;
        int totalTokensOfAllUsers = 0;
        //auto call the user
        for (const auto& user : users) {
            int userTokensSpent = 0;
            double userMoneySpent = 0;

            // calculate total spent for each user
            for (const auto& transaction : user.tH) {
                userTokensSpent += transaction.m_spt / 2; // the tokens each user buy
                userMoneySpent += transaction.m_spt; //total cost
            }
            //formating
            cout << setw(30) << user.userID
                << setw(20) << userTokensSpent
                << setw(20) << fixed << setprecision(2) << userMoneySpent << endl;
            // calculate total spent of all users
            overallTokensSpent += userTokensSpent;
            overallMoneySpent += userMoneySpent;
        }

        cout << "\nTotal Tokens Spent: " << overallTokensSpent << endl;
        cout << "Total Money Spent: $" << fixed << setprecision(2) << overallMoneySpent << endl;
        cout << endl;
    }

    // Function to display credits and exit (R6)
    void creditsAndExit() {
        char confirm;
        cout << "Do you want to exit the system? (Y/N): ";
        cin >> confirm;

        if (confirm == 'Y' || confirm == 'y') {
            cout << "This Program is developed by:\n";
            cout << "Group member 1:\tChan Ching Ho\nStudent ID:\t23008448A\nClass:\t\t103C\n";
            cout << "Group member 2:\tFong Ching Yin\nStudent ID:\t23151253A\nClass:\t\t103C\n";
            cout << "Group member 3:\tFENGJIAMING\nStudent ID:\t23060435A\nClass:\t\t103C\n";
            cout << "Group member 4:\tLi Ming Chun Simon\nStudent ID:\t23097293A\nClass:\t\t103C\n";
            cout << "Group member 5:\tLIN Simin\nStudent ID:\t23123900A\nClass:\t\t103C\n";
            exit(0);  // Exit the program
        }
        else if (confirm == 'N' || confirm == 'n') {
            cout << "Returning to Main Menu...\n";
            return;
        }
        else {
            // Invalid input case
            cout << "Invalid input. Please enter 'Y' or 'N'.\n";
        }
    }
};


int main() {

    TokenManagementSystem tms;
    int option;

    tms.displayWelcomeMessage();

    while (true) {
        cout << "*** Main Menu ***\n";
        cout << "[1] Load Starting Data\n";
        cout << "[2] Show User Records\n";
        cout << "[3] Edit Users\n";
        cout << "[4] Enter User View \n";
        cout << "[5] Show System Usage Summary\n";
        cout << "[6] Credits and Exit\n";
        cout << "*****************\n";

        do {
            cout << "Option (1 - 6): ";
            cin >> option;


            if (cin.fail() || option < 1 || option > 6) {
                cin.clear();                 // Clear the error input data like a letter or english word
                cin.ignore(100, '\n');      // Remove the invalid input (max 100 input length)
                cout << "Invalid option, please try again.\n";
            }
            else {
                break;                       // Exit the loop for valid input (input number 1-6)
            }
        } while (true);



        //option for different cases
        // Go to differrent functions when user input 
        switch (option) {
        case 1:     //Go to Load Starting Data
            tms.loadStartingData();
            break;
        case 2:     //Go to Show User Records
            tms.showUserRecords();
            break;
        case 3:     //Go to Edit Users
            tms.editUsers();
            break;
        case 4:     //Go to Enter User View
            tms.enterUserView();
            break;
        case 5:     //Show System Usage Summary
            tms.showSystemUsage();
            break;
        case 6:     //Credits and Exit
            tms.creditsAndExit();
            break;

        default:    //case for input num which != 1-6 
            cout << "Invalid option, please try again.\n";

        }
    }
    return 0;
}