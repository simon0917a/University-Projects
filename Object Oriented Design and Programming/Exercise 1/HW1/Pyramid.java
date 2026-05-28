//Li Ming Chun Simon 25017659D
//BlueJ
import java.util.Scanner;

public class Pyramid {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
    System.out.print("Enter the number of lines: ");
    int number = sc.nextInt();
    for (int n = 1; n <= number; n++) {
        for (int n2 = number; n2 >= 1; n2--) {
            if (n2 > n) {
                System.out.print("   "); }
            else if (n2 < 10) {
                System.out.print(n2 + "  "); }
            else {
                System.out.print(n2 + " ");
            }
            }
        for (int n3 = 2; n3 <= n; n3++) {
            if (n3 < 10) {
            System.out.print(n3 + "  "); }
            else {
            System.out.print(n3 + " ");
            }
        }
            System.out.print("\n");
        }
    sc.close();
    }
}