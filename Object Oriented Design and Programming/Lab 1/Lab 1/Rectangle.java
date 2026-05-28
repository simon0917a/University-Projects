//Name: Li Ming Chun Simon (25017659D)
//BlueJ

import java.util.Scanner;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shape implements Drawable {
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
    
    //Create a Rectangle2D.Float for the rectangle and draw it on the Canvas
    public void draw() {
        float baseX = 200f - count * 30f;
        float baseY = 200f;

        Rectangle2D.Float rect = new Rectangle2D.Float(baseX, baseY, length, width);

        Canvas canvas = Canvas.getCanvas();
        canvas.draw(this, "green", rect);

        count++;
    }
}