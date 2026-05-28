// SEHH2042 SEHS2042 In-class Exercises (Tutorial Work)
// Program template file
// Do not modify the given codes

// =======================================
// Insert more header files when necessary
// =======================================
#include <iostream>
#include <iomanip>
using namespace std;

void showInfo()
{
	// Fill in your personal particulars below
	cout << "Name      : Li Ming Chun Simon\n";
	cout << "Student ID: 23097293A\n";
	cout << "Class     : 103C\n";
	cout << "T4 submission\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	int input;
	cout << "Enter a positive integer: ";
	cin >> input;
	while (input > 0) {
		cout << "Factors of " << input << ": ";
		for (int n = 1; n <= input; n++)
			if (input % n == 0) {
				cout << n << " ";
			}
		cout << "\nEnter a positive integer: ";
		cin >> input;
	}
	if (input <= 0) {
		cout << "Only positive integer is accepted. Program ends. ";
	}
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	int count = 0;
	for (int n = 2; n <= 200; n++) {
		bool isPrime = true;

		for (int k = 2; k <= n - 1; k++) {
			if (n % k == 0) {
				isPrime = false;
				break;
			}
		}
		if (isPrime) {

			count++;
			cout << setw(5) << n;
			if (count % 10 == 0) {
				cout << endl;
			}
		}
	}
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	cout << setw(10) << "Terms" << setw(20) << "Value of PI" << endl;
	cout << setw(10) << "-----" << setw(20) << "-----------" << endl;

	cout << fixed << setprecision(15);
	for (int n = 10; n <= 100000; n *= 10) {
		double pi = 0;

		for (int k = 1; k <= n; k++) {
			if (k % 2 == 1) {
				pi = pi + 4.0 / (2 * k - 1);
			}
			else {
				pi = pi - 4.0 / (2 * k - 1);
			}
		}
		cout << setw(10) << n << setw(20) << pi << endl;
	}
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	int size, n1, n2;
	cout << "Pattern size: ";
	cin >> size;

	//a
	for (n1 = 1; n1 <= size; n1++) {
		for (n2 = 1; n2 <= size; n2++) {
			if (n1 == 1 || n2 == 1 || n1 == size || n2 == size) {
				cout << "*";
			}
			else {
				cout << " ";
			}
		}
		cout << endl;
	}

	cout << endl << endl;

	//b
	for (n1 = 1; n1 <= size; n1++) {
		for (n2 = 1; n2 <= size; n2++) {
			if (n1 == 1 || n1 == size || n2 == n1) {
				cout << "*";
			}
			else {
				cout << " ";
			}
		}
		cout << endl;
	}

	cout << endl << endl;

	//c
	for (n1 = 1; n1 <= size; n1++) {
		for (n2 = 1; n2 <= size; n2++) {
			if (n1 == 1 || n1 == size || n2 == size - n1 + 1) {
				cout << "*";
			}
			else {
				cout << " ";
			}
		}
		cout << endl;
	}

	cout << endl << endl;

	//d
	for (n1 = 1; n1 <= size; n1++) {
		for (n2 = 1; n2 <= size; n2++) {
			if (n1 == 1 || n1 == size || n2 == n1 || n2 == size - n1 + 1) {
				cout << "*";
			}
			else {
				cout << " ";
			}
		}
		cout << endl;
	}

	cout << endl << endl;

	//e
	for (n1 = 1; n1 <= size; n1++) {
		for (n2 = 1; n2 <= size; n2++) {
			if (n1 == 1 || n1 == size || n2 == n1 || n2 == size - n1 + 1 || n2 == 1 || n2 == size) {
				cout << "*";
			}
			else {
				cout << " ";
			}
		}
		cout << endl;
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