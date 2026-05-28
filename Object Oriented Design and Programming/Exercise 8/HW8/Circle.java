//Name: Li Ming Chun Simon (25017659D)
//BlueJ

import java.util.Scanner;

public class Circle extends Shape {
    private float radius;
    
    //offset drawing positions
    private static int count = 0;
    
    //default radius = 1.0
    public Circle() {
        radius = (float)1.0;
    }
    
    //create a circle with given radius
    public Circle(float r) {
        radius = r;
    }
    
    //read the circle radius from users
    public void readShape() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the radius: ");
        radius = scanner.nextFloat();
    }
    
    //compute and store the area
    public void computeArea() {
        area = (float)(Math.PI * radius * radius);
    }
    
    //compute and store the perimeter
    public void computePerimeter() {
        perimeter = (float)(2.0 * Math.PI * radius);
    }
    
    //display the computed area and perimeter on the console
    public void displayShape() {
        System.out.println("Area of circle = " + area);
        System.out.println("Perimeter of circle = " + perimeter);
    }
    
}