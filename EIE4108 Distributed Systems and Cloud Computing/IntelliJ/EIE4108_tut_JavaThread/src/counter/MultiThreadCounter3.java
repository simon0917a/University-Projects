package counter;

public class MultiThreadCounter3 {
    private int n = 2;

    public static void main(String[] args) {
        MultiThreadCounter3 mt = new MultiThreadCounter3();
        mt.start();
    }

    public void start() {
        for (int i = 0; i < n; i++) {
            new Thread(new Runnable() {
                public void run() {
                    String name = Thread.currentThread().getName();
                    int value = 0;
                    while (true) {
                        try {
                            Thread.sleep(2000);
                            value++;
                            System.out.println("Thread " + name + ": " + value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, String.valueOf(i)).start();
        }
    }
}