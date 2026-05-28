package counter;
public class MultiThreadCounter1 {
    public static void main(String[] args) {
        //(a)
        System.out.println("Number of Thread: " + Thread.activeCount());

        //(b)
        // Get all active threads
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        // Print details of each thread
        for (Thread thread : threads) {
            if (thread != null) {
                System.out.println("Thread name: " + thread.getName() + ", Thread state: " + thread.getState());
            }
        }

        int n = 10;
        Counter counter[] = new Counter[n];
        for (int i=0; i<n; i++) {
            counter[i] = new Counter(i);
            counter[i].start();
        }
    }
}
