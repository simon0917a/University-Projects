//Name: Li Ming Chun Simon (25017659D)
//BlueJ

import java.util.Scanner;

public class ShapeTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String input = scanner.nextLine();

            // Exit
            if (input.equals("x")) {
                break;
            }

            // Circle
            if (input.equals("c")) {
                Circle c = new Circle();
                c.readShape(); // prompt and read radius
                c.computeArea(); // compute area
                c.computePerimeter(); // compute perimeter
                c.displayShape(); //display the area and perimeter
                c.draw(); // draw on Canvas
                System.out.println();
                continue;
            }

            // Square
            if (input.equals("s")) {
                Square s = new Square();
                s.readShape();
                s.computeArea();
                s.computePerimeter();
                s.displayShape();
                s.draw();
                System.out.println();
                continue;
            }

            // Rectangle
            if (input.equals("r")) {
                Rectangle r = new Rectangle();
                r.readShape();
                r.computeArea();
                r.computePerimeter();
                r.displayShape();
                r.draw();
                System.out.println();
                continue;
            }

            // Invalid input
            System.out.println("Invaild commend!");
            System.out.println();
        }

        System.out.println("Program exited.");
        scanner.close();
    }

    //Prints the interactive menu to the console
    private static void printMenu() {
        System.out.println("**************************************");
        System.out.println("* Please choose one the followings:  *");
        System.out.println("* Press 'c' - Circle                 *");
        System.out.println("* Press 's' - Square                 *");
        System.out.println("* Press 'r' - Rectangle              *");
        System.out.println("* Press 'x' - EXIT                   *");
        System.out.println("**************************************");
        System.out.println();
    }
}