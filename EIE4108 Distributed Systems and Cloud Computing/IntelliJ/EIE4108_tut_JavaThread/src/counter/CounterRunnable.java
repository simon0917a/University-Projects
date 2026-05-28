package counter;
public class CounterRunnable implements Runnable {
    private int id;
    private int value;
    public CounterRunnable(int id) {
        this.id = id;
        value = 0;
    }
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
                value++;
                System.out.println("Thread (Runnable) " + id + ": " + value);
            } catch (InterruptedException e) {
                System.out.println("Thread " + id + " get interrupted.");
            }
        }
    }
}