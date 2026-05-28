//Name: Li Ming Chun Simon (25017659D)
//BlueJ

import java.util.ArrayList;
import java.util.Iterator;

public class Picture {
    private ArrayList<Shape> shapes;
    
    public Picture() {
        shapes = new ArrayList<Shape>();
    }
    
    //add Shape object s to the ArrayList shapes
    public void addShape(Shape s) {
        shapes.add(s);
    }
    
    //compute areas and perimeters of all objects
    public void computeShape() {
        for(Iterator<Shape> iter = shapes.iterator(); iter.hasNext(); ) {
            Shape shape = (Shape)iter.next(); 
            shape.computeArea();
            shape.computePerimeter();
        }   
    }
    
    //invoke displayShape()to display the areas and perimeters of all objects
    public void listAllShapeTypes() {
        for (Iterator<Shape> iter = shapes.iterator(); iter.hasNext(); ) {
            Shape shape = (Shape)iter.next();
            shape.displayShape();
        }
    }
    
    //display the areas and perimeters of all objects belonging to className
    public void listSingleShapeType(String className) {
        for (Iterator<Shape> iter = shapes.iterator(); iter.hasNext(); ) {
            Shape shape = (Shape)iter.next();
            
            if (shape.getClass().getSimpleName().equals(className)) {
                shape.displayShape();
            }
        }
    }
}