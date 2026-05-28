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

void showInfo()
{
	// Fill in your personal particulars below
	cout << "Name      : Li Ming Chun, Simon\n";
	cout << "Student ID: 23097293A\n";
	cout << "Class     : 103C\n";
	cout << "T5 Submission\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	const double PI = 3.14159265;
	double radian;
	cout << "Degree\t\tSin\t\tCos\n";
	for (int degree = 0; degree <= 360; degree += 10) {
		radian = degree * PI / 180;
		cout << degree << "\t\t" << sin(radian) << "\t\t" << cos(radian) << endl;
	}
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	const double PI = 3.14159265;
	double radian;
	cout << fixed << setprecision(4);
	cout << "Degree\t\tSin\t\tCos\n";
	for (int degree = 0; degree <= 360; degree += 10) {
		radian = degree * PI / 180;
		cout << noshowpos;
		cout << setw(3) << degree << "\t\t";
		cout << showpos;
		cout << sin(radian) << "\t\t" << cos(radian) << endl;
		cout << noshowpos;
	}
}

double integerPower(double b, int e) {
	double result = 1;
	if (e >= 0) {
		for (int k = 1; k <= e; k++) {
			result = result * b;
		}
	}
	else {
		for (int k = 1; k <= -e; k++)
			result = result * (1 / b);
	}
	return result;
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	double base;
	int exp;
	double ans;

	cout << "Enter the base value: ";
	cin >> base;
	cout << "Enter the exponent value: ";
	cin >> exp;

	ans = integerPower(base, exp);
	cout << base << " to the power " << exp << " is " << ans << endl;
}

void nChar(int n, char c)
{
	for (int i = 0; i < n; i++)
		cout << c;
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	//a
	for (int a = 1; a <= 5; a++) {
		nChar(a, '*');
		cout << endl;
	}

	cout << endl;

	//b
	for (int a = 1; a <= 5; a++) {
		nChar(5 - a, ' ');
		nChar(a, '*');
		cout << endl;
	}

	cout << endl;

	//c
	for (int a = 1; a <= 5; a++) {
		nChar(5 - a, ' ');
		nChar(a, '*');
		nChar(a - 1, '*');
		cout << endl;
	}

	cout << endl;

	//d
	for (int a = 1; a <= 5; a++) {
		nChar(5 - a, ' ');
		nChar(a, '*');
		nChar(a - 1, '*');
		cout << endl;
	}
	for (int a = 4; a >= 0; a--) {
		nChar(5 - a, ' ');
		nChar(a, '*');
		nChar(a - 1, '*');
		cout << endl;
	}

	cout << endl;

	//e
	for (int i = 1; i <= 3; i++) {
		for (int a = 1; a <= 5; a++) {
			nChar(5 - a, ' ');
			nChar(a, '*');
			nChar(a - 1, '*');
			cout << endl;
		}
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