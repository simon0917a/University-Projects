package counter;
public class MultiThreadCounter2 {
    public static void main(String[] args) {
        int n = 10;
        for (int i=0; i<n; i++) {
            CounterRunnable myRunnable = new CounterRunnable(i);
            Thread thread = new Thread(myRunnable);
            thread.start();
        }
    }
}
