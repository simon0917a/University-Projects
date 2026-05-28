// SEHH2042 SEHS2042 In-class Exercises (Tutorial Work)
// Program template file
// Do not modify the given codes

// =======================================
// Insert more header files when necessary
// =======================================
#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <cstring>
using namespace std;

void swapString(char[], char[]);
const int NMSG = 3;
const int MAXLEN = 80;
void printReverse(char[][MAXLEN]);

void showInfo()
{
	// Fill in your personal particulars below
	cout << "Name      : Li Ming Chun, Simon\n";
	cout << "Student ID: 23097293A\n";
	cout << "Class     : 103C\n";
	cout << "T9 submission\n";
}

void Q1()
{
	// =====================================
	// Insert your codes for Question 1 here
	// No need main() and return 0
	// =====================================
	char text[100];
	cout << "Enter a string: ";
	cin >> text;
	for (int i = 0; text[i] != '\0'; i++) {
		if (text[i] >= 'a' && text[i] <= 'z')
			text[i] = text[i] - 'a' + 'A';

	}
	cout << "Converted string is: " << text;
}

void Q2()
{
	// =====================================
	// Insert your codes for Question 2 here
	// No need main() and return 0
	// =====================================
	char w1[20], w2[20];

	cout << "Enter the two words (separated by space): ";
	cin >> w1;
	cin >> w2;

	cout << "Before swapping, words are:\n";
	cout << w1 << endl << w2 << endl;

	// Function call on swapString
	// Insert your code here
	swapString(w1, w2);

	cout << "After swapping, words are:\n";
	cout << w1 << endl << w2 << endl;
}

void swapString(char s1[], char s2[]) {
	//s1[k] <-> s2[k]
	for (int k = 0; s1[k] != '\0' && s2[k] != '\0'; k++) {
		char temp = s1[k];
		s1[k] = s2[k];
		s2[k] = temp;
	}
}

void Q3()
{
	// =====================================
	// Insert your codes for Question 3 here
	// No need main() and return 0
	// =====================================
	char buffer[80];
	char msgs[10][15] = {
		"a", "ab", "abc", "abcd", "abcde", "abcdef",
		"abcdefg", "abcdefgh", "abcdefghi", "abcdefghij"
	};

	// Put strings in msgs into buffer
	// Your codes should be inserted here.
	buffer[0] = '\0';

	for (int k = 0; k < 10; k++) {
		strcat(buffer, msgs[k]);
		strcat(buffer, "\n");
	}
	//strcpy_s(buffer, 80, "");
	// Print the buffer content

	cout << "buffer is:" << endl;
	cout << buffer;

	// Show the length of buffer, using strlen()
	// Your codes should be inserted here.
	cout << "Length of buffer is: " << strlen(buffer) << endl;
}

void Q4()
{
	// =====================================
	// Insert your codes for Question 4 here
	// No need main() and return 0
	// =====================================
	char message[NMSG][MAXLEN];

	cin.ignore(255, '\n');

	for (int k = 0; k < NMSG; k++) {
		cout << "Enter message " << k << ": ";
		cin.getline(message[k], MAXLEN, '\n');
	}

	printReverse(message);

}

void printReverse(char msg[][MAXLEN]) {
	//print content in reverse order...
	cout << "The messages are printed in a last-in-first-out sequence." << endl;
	cout << "Characters in each message are printed in a reverse order." << endl;
	for (int i = NMSG - 1; i >= 0; i--) {
		cout << "Message " << i << ": ";
		for (int j = strlen(msg[i]) - 1; j >= 0; j--) {
			cout << msg[i][j];
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