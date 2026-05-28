//Name: Li Ming Chun Simon (25017659D)
//BlueJ

import java.util.Scanner;

public class Rectangle extends Shape {
    private float length;
    private float width;
    
    //offset drawing positions
    private static int count = 0;
    
    //default length and width = 1.0
    public Rectangle() {
        length = (float)1.0;
        width = (float)1.0;
    }
    
    //create a rectangle with given length and width
    public Rectangle(float l, float w) {
        length = l;
        width = w;
    }
    
    //read the circle radius from users
    public void readShape() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the length: ");
        length = scanner.nextFloat();
        System.out.println("Please input the width: ");
        width = scanner.nextFloat();
    }
    
    //compute and store the area
    public void computeArea() {
        area = (float)(length*width);
    }
    
    //compute and store the perimeter
    public void computePerimeter() {
        perimeter = (float)(2.0 * (length + width));
    }
    
    //display the computed area and perimeter on the console
    public void displayShape() {
        System.out.println("Area of Rectangle = " + area);
        System.out.println("Perimeter of Rectangle = " + perimeter);
    }
    
}