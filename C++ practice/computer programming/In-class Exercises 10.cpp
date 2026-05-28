// SEHH2042 SEHS2042 In-class Exercises (Tutorial Work)
// Program template file
// Do not modify the given codes

// =======================================
// Insert more header files when necessary
// =======================================
#include <iostream>
using namespace std;

void swapInteger(int*, int*);
void printArray(const int[], int);
void multiplyArray(int* const, int, int);

void showInfo()
{
	// Fill in your personal particulars below
	cout << "Name      : Li Ming Chun, Simon\n";
	cout << "Student ID: 23097293A\n";
	cout << "Class     : 103C\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	int x = 1;
	int y[5] = { 10, 20, 30, 40, 50 };
	int* xPtr;
	int* yPtr;

	xPtr = &x;
	yPtr = y;

	//a
	cout << &x << endl;
	cout << y << endl;

	cout << endl;

	//b
	cout << xPtr << endl;
	cout << yPtr << endl;

	cout << endl;

	//c
	cout << *xPtr << endl; //x
	cout << *yPtr << endl; //y[0]

	cout << endl;

	//d
	cout << (*yPtr + 2) << endl; // y[0] + 2
	cout << *(yPtr + 2) << endl; // y[2]

	cout << endl;

	//e
	xPtr = yPtr + 2;
	(*xPtr)++; //y[2]++
	cout << *xPtr << endl;
	cout << y[2] << endl;
	(*yPtr)++; //y[0]++
	cout << *yPtr << endl;
	cout << y[0] << endl;

}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	int x = 2, y = 5;

	cout << "Before swapping:" << endl;
	cout << "x is: " << x << endl;
	cout << "y is: " << y << endl;

	// call swapInteger here...
	swapInteger(&x, &y);

	cout << "After swapping:" << endl;
	cout << "x is: " << x << endl;
	cout << "y is: " << y << endl;
}

void swapInteger(int* a, int* b) {
	int temp = *a;
	*a = *b;
	*b = temp;
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	const int arraySize = 10;

	int c[arraySize] = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };

	cout << "Before multiplyArray, array is: ";
	printArray(c, arraySize);

	// Function call on multiplyArray
	// Insert your codes here
	multiplyArray(c, arraySize, 3);

	cout << "After multiplyArray, array is: ";
	printArray(c, arraySize);
}

void multiplyArray(int* const a, int size, int n) {
	for (int k = 0; k < size; k++) {
		*(a + k) = *(a + k) * n;
	}
}

void printArray(const int a[], int size) {
	for (int i = 0; i < size; i++) {
		cout << a[i] << " ";
	}
	cout << endl;
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	char* buffer = new char[100];	// reserve 100 characters
	char* word[10] = {};			// initialize all pointers to 0 (NULL)

	// Your code should be inserted here
	cin.ignore(255, '\n');

	cout << "Enter a sentence with at most 10 words and 100 characters: ";
	cin.getline(buffer, 100, '\n');

	for (int i = 0; i < 10; i++) {
		word[i] = buffer;
		while (*buffer != ' ' && *buffer != '\0') {
			buffer++;
		}
		if (*buffer == '\0') {
			break;
		}
		*buffer = '\0';
		buffer++;
	}

	for (int i = 0; i < 10; i++)
		if (word[i] != 0)			// check if it is a NULL pointer
			cout << i << ": " << word[i] << endl;
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