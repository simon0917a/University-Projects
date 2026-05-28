// SEHH2042 SEHS2042 In-class Exercises (Tutorial Work)
// Program template file
// Do not modify the given codes

// =======================================
// Insert more header files when necessary
// =======================================
#include <iostream>
#include <iomanip>
#include <cstdlib>
using namespace std;

//function prototype
void printData(int[], int);
void printBar(int[], int);
int largest(int[], int);
double average(int[], int);
void maxAppear(int[], int, int&, int&);

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
	int n, data[20];
	cout << "How many integers to enter? ";
	cin >> n;
	for (int i = 0; i < n; i++) {
		cout << "? ";
		cin >> data[i];
	}
	cout << "The input integers are:\n";
	printData(data, n);
}

//function definition
void printData(int list[], int size) {
	for (int i = 0; i < size; i++) {
		cout << setw(5) << list[i];
		if (size == 20) {
			if (i % 5 == 4) cout << endl;
		}
		else {
			if (i % 10 == 9) cout << endl;
		}
	}
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	int n, data[20];
	cout << "How many integers to enter? ";
	cin >> n;
	for (int i = 0; i < n; i++) {
		cout << "? ";
		cin >> data[i];
	}
	cout << "The bar chart is:\n";

	printBar(data, n);
}

void printBar(int list[], int size) {
	for (int i = 0; i < size; i++) {
		for (int count = 1; count <= list[i]; count++) {
			cout << "*";
		}
		cout << endl;
	}
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	const int SIZE = 20;
	int data[SIZE];
	int seed;

	cout << "Please enter the seed value: ";
	cin >> seed;
	srand(seed);

	for (int k = 0; k < SIZE; k++) {
		data[k] = 1 + rand() % 100;
	}

	cout << "The random integers are:\n";
	printData(data, SIZE);
	cout << "The largest number is " << largest(data, SIZE) << endl;
	cout << "The average value is: " << average(data, SIZE) << endl;
}

int largest(int list[], int size) {
	int ans = list[0];
	for (int k = 0; k < size; k++) {
		if (list[k] > ans)
			ans = list[k];
	}
	return ans;
}

double average(int list[], int size) {
	double sum = 0;
	for (int k = 0; k < size; k++) {
		sum += list[k];
	}
	return sum / size;
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	const int SIZE = 100;
	const int RANGE = 10;
	int seed;
	int val[SIZE];
	int max;
	int freq;

	cout << "Please enter the seed value: ";
	cin >> seed;
	srand(seed);

	for (int k = 0; k < SIZE; k++) {
		val[k] = 1 + rand() % RANGE;
	}

	cout << "The random integers are:\n";
	printData(val, SIZE);
	maxAppear(val, SIZE, max, freq);
	cout << max << " appears the most with " << freq << " times " << endl;
}

void maxAppear(int list[], int size, int& max, int& freq) {
	const int RANGE = 10;
	int frequency[RANGE + 1] = {};

	for (int i = 0; i < size; i++) {
		frequency[list[i]]++;
	}

	for (int i = 1; i <= RANGE; i++) {
		if (frequency[i] > freq) {
			max = i;
			freq = frequency[i];
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