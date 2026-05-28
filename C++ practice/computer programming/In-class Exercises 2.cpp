// SEHH2042 In-class Exercises (Tutorial Work)
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
	cout << "T2 submission\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	int n, digit1, digit2;
	cout << "Enter a two-digit integer (00 - 99): ";
	cin >> n;
	digit1 = n / 10;
	digit2 = n % 10;
	if (digit1 > digit2)
		cout << digit1 << " > " << digit2;
	else if (digit1 < digit2)
		cout << digit1 << " < " << digit2;
	else
		cout << digit1 << " = " << digit2;
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	int x, y;
	cout << "Input x: ";
	cin >> x;
	cout << "Input y: ";
	cin >> y;
	if (y % x == 0) {
		cout << x << " is a factor of " << y;
	}
	else {
		cout << x << " is not a factor of " << y;
	}
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	int A;
	cout << "Input a year: ";
	cin >> A;
	if ((A % 4 == 0 && A % 100 != 0) || (A % 400 == 0)) {
		cout << "Is " << A << " a leap year? Yes";
	}
	else {
		cout << "Is " << A << " a leap year? No";
	}
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	double x, y, z;
	cout << "Input side x: ";
	cin >> x;
	cout << "Input side y: ";
	cin >> y;
	cout << "Input side z: ";
	cin >> z;
	if (z * z == (x * x + y * y)) {
		cout << fixed << setprecision(3);
		cout << setw(10) << "sinA" << setw(10) << "cosA" << setw(10) << "tanA" << endl;
		cout << setw(10) << x / z << setw(10) << y / z << setw(10) << x / y;
	}
	else {
		cout << "Error: Not right-angled triangle";
	}
}

void Q5()
{
	// =====================================
	// Insert your codes for Question 5 here
	// No need main() and return 0
	// =====================================
	int value, type;
	cout << "What is the principal value? ";
	cin >> value;
	cout << "Please enter the customer type: ";
	cin >> type;
	cout << "Interest payable after one year: ";
	switch (type) {
	case 0: cout << "$" << value * 0.005;
		break;
	case 1: cout << "$" << value * 0.008;
		break;
	case 2: cout << "$" << value * 0.01;
		break;
	case 3: cout << "$" << value * 0.012;
		break;
	case 4: cout << "$" << value * 0.02;
	default: cout << "Error in customer type";
	}
}

void Q6()
{
	// =====================================
	// Insert your codes for Question 6 here
	// No need main() and return 0
	// =====================================
	int day, month, year;
	bool A, B, C, D;
	cout << "Input day month year: ";
	cin >> day >> month >> year;
	cout << day << "-" << month << "-" << year << " is ";
	A = (day <= 31 && day > 0 && month <= 12 && month > 0);
	B = ((month == 2 && day <= 28) || (month == 4 && day <= 30) || (month == 6 && day <= 30) || (month == 9 && day <= 30) || (month == 11 && day <= 30));
	C = (((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) && (month == 2) && (day == 29));
	D = ((month == 1 && day <= 31) || (month == 3 && day <= 31) || (month == 5 && day <= 31) || (month == 7 && day <= 31) || (month == 8 && day <= 31) || (month == 10 && day <= 31) || (month == 12 && day <= 31));
	if (A && (B || C || D)) {
		cout << "correct";
	}
	else {
		cout << "incorrect";
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
		case '6': Q6(); break;
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