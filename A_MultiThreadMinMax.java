import java.util.Random;
import java.time.LocalDateTime;
import java.time.ZoneId;
class MinMaxThread extends Thread {
    private int[] data;
    private int start, end;
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;
    private int threadId;
    public MinMaxThread(int[] data, int start, int end, int threadId) {
        this.data = data;
        this.start = start;
        this.end = end;
        this.threadId = threadId;
    }
    @Override 
    public void run() {
        for (int i = start; i < end; i++) {
            if (data[i] < min)
                min = data[i];
            if (data[i] > max)
                max = data[i];
        }
    }
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }
    public int getThreadId() {
        return threadId;
    }
}
public class A_MultiThreadMinMax {
    public static String getISTTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString();
    }

    public static void main(String[] args) throws InterruptedException {

        int TOTAL = 1_000_000;
        int THREADS = 5;
        int CHUNK = TOTAL / THREADS;

        int[] numbers = new int[TOTAL];
        Random random = new Random();

        for (int i = 0; i < TOTAL; i++) {
            numbers[i] = random.nextInt(1_000_000);
        }
        System.out.println("===== SEQUENTIAL EXECUTION =====");
        System.out.println("Start Time (IST): " +  getISTTime());

        long seqStart = System.currentTimeMillis();

        int seqMin = Integer.MAX_VALUE;
        int seqMax = Integer.MIN_VALUE;

        for (int num : numbers) {
            if (num < seqMin)
                seqMin = num;
            if (num > seqMax)
                seqMax = num;
        }
        long seqEnd = System.currentTimeMillis();

        System.out.println("End Time (IST): " + getISTTime());
        System.out.println("Sequential Min: " + seqMin);
        System.out.println("Sequential Max: " + seqMax);
        System.out.println("Time Taken: " + (seqEnd - seqStart) + " ms\n");
        
        System.out.println("===== MULTITHREADED EXECUTION =====");
        System.out.println("Start Time (IST): " + getISTTime());

        long mtStart = System.currentTimeMillis();

        MinMaxThread[] threads = new MinMaxThread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            int start = i * CHUNK;
            int end = (i + 1) * CHUNK;
            threads[i] = new MinMaxThread(numbers, start, end, i + 1);
            threads[i].start();
        }

        for (MinMaxThread t : threads) {
            t.join();
        }

        int mtMin = Integer.MAX_VALUE;
        int mtMax = Integer.MIN_VALUE;

        System.out.println("\n--- Individual Thread Results ---");

        for (MinMaxThread t : threads) {
            System.out.println(
                "Thread-" + t.getThreadId() +
                " | Min: " + t.getMin() +
                " | Max: " + t.getMax()
            );

            if (t.getMin() < mtMin)
                mtMin = t.getMin();
            if (t.getMax() > mtMax)
                mtMax = t.getMax();
        }

        long mtEnd = System.currentTimeMillis();

        System.out.println("\nEnd Time (IST): " + getISTTime());
        System.out.println("Final Multithreaded Min: " + mtMin);
        System.out.println("Final Multithreaded Max: " + mtMax);
        System.out.println("Time Taken: " + (mtEnd - mtStart) + " ms");
    }
}
