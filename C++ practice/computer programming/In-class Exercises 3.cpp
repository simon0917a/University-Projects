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
	cout << "T3 Submission\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	int input, ans = 31;
	cout << "I have a number between 1 and 100.\n";
	cout << "Can you guess my number?\n";
	do {
		cout << "Guess: ";
		cin >> input;
		if (input < ans) {
			cout << "Too low. Try again.\n";
		}
		else if (input > ans) {
			cout << "Too high. Try again.\n";
		}
		else {
			cout << "Excellent! Correct guess.";
		}
	} while (input != ans);
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	int n, A, B;
	cout << "n: ";
	cin >> n;
	B = n;
	for (A = n - 1; A > 0; A--) {
		n *= A;
	}
	cout << B << "! = " << n;
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	int A, B;
	int sum = 0;
	int smaller, larger;
	cout << "Please input value A: ";
	cin >> A;
	cout << "Please input value B: ";
	cin >> B;
	if (A < B) {
		smaller = A;
		larger = B;
	}
	else {
		smaller = B;
		larger = A;
	}
	for (int k = smaller; k <= larger; k++) {
		if (k % 2 == 1)
			sum = sum + k;
	}
	cout << "Sum of odd integers from " << smaller << " to " << larger << " is " << sum;
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	double base, exponent;
	double A, B, C;
	cout << "Enter the base value: ";
	cin >> base;
	cout << "Enter the exponent value: ";
	cin >> exponent;
	B = base;
	C = base;
	for (A = exponent - 1; A > 0; A--) {
		C *= B;
	}
	cout << base << " to the power of " << exponent << " is " << C;
}

void Q5()
{
	// =====================================
	// Insert your codes for Question 5 here
	// No need main() and return 0
	// =====================================
	int num1;
	bool num2;
	cout << "Input a positive integer: ";
	cin >> num1;
	for (int n = 2; n < num1; n++) {
		if (num1 == 1) {
			num2 = false; break;
		}

		if (num1 % n == 0) {
			num2 = false; break;
		}
		else if (num1 % n != 0) {
			num2 = true;
		}
	}
	if (num2 == true || num1 == 2) {
		cout << num1 << " is a prime number";
	}
	else {
		cout << num1 << " is not a prime number";
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
		case '5': Q5(); break;
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