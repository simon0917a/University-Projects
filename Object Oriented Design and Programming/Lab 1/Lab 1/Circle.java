//Name: Li Ming Chun Simon (25017659D)
//BlueJ

import java.util.Scanner;
import java.awt.geom.Ellipse2D;

public class Circle extends Shape implements Drawable {
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
    
    //Create an Ellipse2D.Float for the circle and draw it on the Canvas
    public void draw() {
        float baseX = 20f;
        float baseY = 20f + count * 30f;
        float diameter = 2.0f * radius;

        Ellipse2D.Float circleShape = new Ellipse2D.Float(baseX, baseY, diameter, diameter);

        Canvas canvas = Canvas.getCanvas();
        canvas.draw(this, "red", circleShape);

        count++;
    } 
}