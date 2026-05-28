//Name: Li Ming Chun Simon (25017659D)
//BlueJ

import java.util.Scanner;

public class Square extends Shape {
    private float length;
    
    //offset drawing positions
    private static int count = 0;
    
    //default length = 1.0
    public Square() {
        length = (float)1.0;
    }
    
    //create a square with given length
    public Square(float l) {
        length = l;
    }
    
    //read the circle radius from users
    public void readShape() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the length: ");
        length = scanner.nextFloat();
    }
    
    //compute and store the area
    public void computeArea() {
        area = (float)(length*length);
    }
    
    //compute and store the perimeter
    public void computePerimeter() {
        perimeter = (float)(4.0 * length);
    }
    
    //display the computed area and perimeter on the console
    public void displayShape() {
        System.out.println("Area of Square = " + area);
        System.out.println("Perimeter of Square = " + perimeter);
    }
    
}