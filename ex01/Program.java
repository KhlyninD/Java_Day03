package ex01;

public class Program {
    private static final String HEN = "Hen";
    private static final String EGG = "Egg";
    private static final String COUNT = "--count=";

    public static void main(String[] args) throws InterruptedException {
        int count = checkCount(args);

        if (count < 1) {
            System.err.println("Invalid flag!");
            return;
        }

        Printer print = new Printer();
        MyThread egg = new MyThread(EGG, count, print);
        MyThread hen = new MyThread(HEN, count, print);

        egg.start();
        hen.start();

        egg.join();
        hen.join();
    }

    public static int checkCount(String[] args) {
        int count = -1;
        if (args.length > 0 && args[0].startsWith(COUNT)) {
            try {
                count = Integer.parseInt(args[0].substring(args[0].indexOf("=") + 1));
                return count;
            }catch (NumberFormatException e){
                return count;
            }
        }
        return count;
    }
}

class Printer {

    String oldName = "Hen";
    public synchronized void printMessage(String name){
        if (oldName.equals(name)) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        oldName = name;
        System.out.println(name);
        notify();
    }
}

class MyThread extends Thread {

    private String name;
    private int count;
    Printer print;

    public MyThread(String name, int count, Printer print) {
        this.name = name;
        this.count = count;
        this.print = print;
    }

    public void run() {
        for (int i = 0; i < count; i++) {
            print.printMessage(name);
        }
    }
}
