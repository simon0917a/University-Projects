//HW3 Q1. Name: Li Ming Chun Simon Student ID: 25017659D
//BlueJ

public class Course {
    
    private String courseName;
    private int testMarks;
    
    public Course(String courseName, int testMarks) {
        this.courseName = courseName;
        this.testMarks = testMarks;
    }
    
    public String toString() {
        return courseName + ", " + testMarks;
    }
    
}