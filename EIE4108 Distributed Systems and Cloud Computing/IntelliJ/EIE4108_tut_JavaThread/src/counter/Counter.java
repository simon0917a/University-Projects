package counter;
public class Counter extends Thread {
    private int id;
    private int value;
    public Counter(int id) {
        this.id = id;
        value = 0;
    }
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
                value++;
                System.out.println("Thread " + id + ": " + value);
            } catch (InterruptedException e) {
                System.out.println("Thread " + id + " get interrupted.");
            }
        }
    }
}