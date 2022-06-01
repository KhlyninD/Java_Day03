package ex02;


import java.util.Arrays;
import java.util.Random;

public class Program {
    private static final int MODULO_LIMIT = 1000;

    public static void main(String[] args) throws InterruptedException {
        int arraySize = checkArgs(args, 0, "--arraySize=");
        int threadsCount = checkArgs(args, 1, "--threadsCount=");

        if (arraySize < 1 || threadsCount < 1) {
            System.err.println("Invalid flag!");
            return;
        }

        int[] array = (new Random().ints(arraySize, -MODULO_LIMIT, MODULO_LIMIT)).toArray();
        System.out.printf("Sum: %d\n", Arrays.stream(array).sum());

        MyThread[] threads = new MyThread[threadsCount];

        int[] resArray = new int[threadsCount];

        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new MyThread(array, resArray, i, threadsCount);
        }

        for (Thread t: threads) {
            t.start();
            t.join();
        }

        System.out.printf("Sum by threads: %d\n", Arrays.stream(resArray).sum());
    }

    public static int checkArgs(String[] args, int id, String str) {
        int num = -1;
        if (args.length > 0 && args[id].startsWith(str)) {
            try {
                num = Integer.parseInt(args[id].substring(args[id].indexOf("=") + 1));
                return num;
            }catch (NumberFormatException e){
                return num;
            }
        }
        return num;
    }
}

class MyThread extends Thread {

    private int start;
    private int startArray;
    private int endArray;
    private int[] array;
    private int[] resArray;


    public MyThread(int[] array, int[] resArray, int start, int threadsCount) {
        int sectionSize = array.length / threadsCount;
        this.array= array;
        this.resArray= resArray;
        this.start = start;
        this.startArray = start * sectionSize;
        endArray = startArray + sectionSize;
        if (start == (threadsCount - 1)) {
            endArray = array.length;
        }
    }

    public synchronized void run() {
        for (int i = startArray; i < endArray; i++) {
            resArray[start] += array[i];
        }
        System.out.println("Thread " + start + ": from " + startArray + " to " + (endArray - 1) + " sum is " + resArray[start]);
    }
}
