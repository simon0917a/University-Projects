package counter;

public class MultiThreadCounter4 {
    public static void main(String[] args) {
        int n = 10;
        Counter counter[] = new Counter[n];

        for (int i = 0; i < n; i++) {
            counter[i] = new Counter(i);
            counter[i].start();
        }

        while (true) {
            try {
                Thread.sleep(500);
                if (counter[0] != null) {
                    counter[0].interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}