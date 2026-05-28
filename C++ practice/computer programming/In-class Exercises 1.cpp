	// SEHH2042 SEHS2042 In-class Exercises (Tutorial Work)
// Program template file
// Do not modify the given codes

// =======================================
// Insert more header files when necessary
// =======================================
#include <iostream>
using namespace std;

void showInfo()
{
	// Fill in your personal particulars below
	cout << "Name      : Li Ming Chun Simon\n";
	cout << "Student ID: 23097293A\n";
	cout << "Class     : 103C\n";
	cout << "T1 Submission\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	cout << "This is tutorial 1\n";
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	double m, cm;
	cout << "Input meter: ";
	cin >> m;
	cm = m * 100;
	cout << m << "m = " << cm << "cm";
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	double radius, height, volume;
	cout << "Enter the radius (in cm): ";
	cin >> radius;
	cout << "Enter the height (in cm): ";
	cin >> height;
	volume = 1 / 3.0*3.14159265*radius*radius*height;
	cout << "The volume of the cone is " << volume << " cm^3";
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	double x, y;
	cout << "Input the value of x: ";
	cin >> x;
	y = ((3 + 4 * x) / 10 - 10 * (x - 2)*(x - 2) / (x - 3))*((3 + 4 * x) / 10 - 10 * (x - 2)*(x - 2) / (x - 3));
	cout << "The answer is " << y;
}

void Q5()
{
	// =====================================
	// Insert your codes for Question 5 here
	// No need main() and return 0
	// =====================================
	int A, B;
	cout << "Enter a two-digit integer (00 - 99): ";
	cin >> A;
	B = A / 10;
	cout << "The two digits are " << B << " and " << A % 10;

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