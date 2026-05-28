//HW3 Q1. Name: Li Ming Chun Simon Student ID: 25017659D
//BlueJ

public class UniversityStudent {
    
    private String studentName;
    private int courseNumber;
    private Course[] courseList;
    
    public UniversityStudent(String studentName, int courseNumber, Course[] courseList) {
        this.studentName = studentName;
        this.courseNumber = courseNumber;
        this.courseList = courseList;
    }
    
    public void print() {
        System.out.println("Student Name: " + this.studentName);
        for (int n = 0; n < courseNumber; n++) {
            System.out.println(courseList[n]);
        }
        
        
    }
    
}