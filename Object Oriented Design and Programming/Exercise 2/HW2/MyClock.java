//HW2. Q1. Name: Li Ming Chun Simon; Student ID:25017659D
//Tool: BlueJ

//In addition to the question, please have a understanding of all the 
//given source codes. E.g. timeTick() is a function to add one second.

//Do not change the main() program. However, please do testing by 
//changing the input number inside ClockDisplay().

public class MyClock
{
    public static void main(String[] args) {
        ClockDisplay hkTime = new ClockDisplay(12,00);
        ClockDisplay londonTime = new ClockDisplay(23,59);
        
        hkTime.timeTick(); //add one second
        londonTime.timeTick(); //add one second
        
        System.out.println(hkTime.getTime());
        System.out.println(londonTime.getTime());
    }
}