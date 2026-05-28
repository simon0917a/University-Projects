// SEHH2042/SEHS2042 Assignment 1
// Program template file: A1Template.cpp
// Do not modify the given codes

// Insert more header files when necessary
#include <iostream>
#include <iomanip>
using namespace std;

void showInfo()
{
	// Fill in your personal particulars below
	cout << "Name      : Li Ming Chun Simon\n";
	cout << "Student ID: 23097293A\n";
	cout << "Class     : 103C\n";
}

void Q1()
{
	// Insert your codes for Question 1 here
	char secretletter, guessedletter1, guessedletter2, guessedletter3;
	cin >> secretletter >> guessedletter1;
	guessedletter2 = 'a';
	guessedletter3 = 'z';
	while ((int)secretletter != (int)guessedletter1) {
		if ((int)secretletter > (int)guessedletter1) {
			if (guessedletter1 < guessedletter2) {
				cout << "Game over"; break;
			}
			guessedletter2 = guessedletter1;
			cout << guessedletter2 << " to " << guessedletter3 << endl;
			cin >> guessedletter1;
		}
		else if ((int)secretletter < (int)guessedletter1) {
			if (guessedletter1 > guessedletter3) {
				cout << "Game over"; break;
			}
			guessedletter3 = guessedletter1;
			cout << guessedletter2 << " to " << guessedletter3 << endl;
			cin >> guessedletter1;
		}
		if ((int)secretletter == (int)guessedletter1) {
			cout << "Correct";
		}
	}
}

void Q2()
{
	// Insert your codes for Question 2 here
	double localdata, roamingdata, localdata2, roamingdata2;
	double localcall, roamingcall, localcall2, roamingcall2;
	double totaldata, totalcall, totaldata2, totalcall2, totalcost;
	cin >> localdata >> roamingdata >> localcall >> roamingcall;
	localdata2 = localdata;
	roamingdata2 = roamingdata;
	localcall2 = localcall;
	roamingcall2 = roamingcall;
	if (localdata <= 2) {
		localdata = localdata * 15;
	}
	else if (localdata > 2 && localdata <= 5) {
		localdata = 2 * 15 + (localdata - 2) * 12.5;
	}
	else if (localdata > 5) {
		localdata = 2 * 15 + 3 * 12.5 + (localdata - 5) * 8.8;
	}
	if (localcall <= 500) {
		localcall = localcall * 0.11;
	}
	else if (localcall > 500) {
		localcall = 500 * 0.11 + (localcall - 500) * 0.07;
	}

	totaldata = localdata + roamingdata * 25;
	totalcall = localcall + roamingcall * 1.2;
	totaldata2 = localdata2 + roamingdata2;
	totalcall2 = localcall2 + roamingcall2;
	if (totaldata2 > 10) {
		totaldata = totaldata * 0.9;
	}
	if (totalcall2 > 1000) {
		totalcall = totalcall * 0.95;
	}
	totalcost = totaldata + totalcall;
	cout << setprecision(2);
	cout << fixed;
	cout << totalcost;
}

void Q3()
{
	// Insert your codes for Question 3 here
	int size, n1, n2;
	cin >> size;
	if (size >= 0 && size % 2 != 0) {
		for (n1 = 1; n1 <= size; n1++) {
			for (n2 = 1; n2 <= size * 2 - 1; n2++) {
				if (n2 == 1 || n2 == size * 2 - 1 || n2 == n1 || n2 == size * 2 - n1) {
					cout << "*";
				}
				else {
					cout << " ";
				}
			}
			cout << endl;
		}
	}
	else {
		cout << "E";
	}
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