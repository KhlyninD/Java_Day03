package ex00;

public class Program {
    private static final String HEN = "Hen";
    private static final String EGG = "Egg";
    private static final String HUMAN = "Human";
    private static final String COUNT = "--count=";


    public static void main(String[] args) throws InterruptedException {
        int count = checkCount(args);

        if (count < 1) {
            System.err.println("Invalid flag!");
            return;
        }

        MyThread hen = new MyThread(HEN, count);
        MyThread egg = new MyThread(EGG, count);

        hen.start();
        egg.start();

        hen.join();
        egg.join();

        for (int i = 0; i < count; i++) {
            System.out.println(HUMAN);
        }
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

class MyThread extends Thread{

    private String name;
    private int count;

    public MyThread(String name, int count){
        this.name = name;
        this.count = count;
    }

    public void run(){

        for (int i = 0; i < count; i++) {
            try{
                Thread.sleep((long) ((Math.random() * (50 - 10)) + 10));
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(name);
        }
    }
}
