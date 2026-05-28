if (userFound == false) {
    cout << "User not found. Adding new user." << endl;
    //Ask the user to input the new user's type, token balance, and auto top-up
    cout << "Enter Type (T: Trial, F: Full, S: Student): ";
    cin >> type;
    cout << "Enter Token Balance: ";
    cin >> tokenBalance;
    cout << "Auto Top-up (Y/N): ";
    cin >> autoTopUp;

    //Check whether the user input correctly
    for (int i = 0; i <= 1; i++) {
        if ((type == 'T' || type == 'F' || type == 'S') && tokenBalance >= 0 && (autoTopUp == 'Y' || autoTopUp == 'N')) {
            //Add the new user
            users.push_back({ newUserID, type, tokenBalance, autoTopUp });
            cout << "User added successfully." << endl;
            cout << "Back to the Main Menu." << endl;
            return; //return to main menu
        }
        else {
            //Ask the user to input the information again
            cout << "Invalid input. Please input again." << endl;
            cout << "Enter Type (T: Trial, F: Full, S: Student): ";
            cin >> type;
            cout << "Enter Token Balance: ";
            cin >> tokenBalance;
            cout << "Auto Top-up (Y/N): ";
            cin >> autoTopUp;
            if (i == 1) {
                cout << "Error. Back to the Main Menu." << endl;
                return; //return to main menu
            }
        }
    }
}