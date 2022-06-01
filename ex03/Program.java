package ex03;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Program {
    private static final String INPUT_FILE = "./ex03/files_urls.txt";

    public static void main(String[] args) {

        int threadsCount = checkArgs(args, 0, "--threadsCount=");

        if (threadsCount < 1) {
            System.err.println("Invalid flag!");
            return;
        }

        Queue <String> queue = addQueue();

        if (threadsCount > queue.size()) {
            threadsCount = queue.size();
        }

        MyThread[] threads = new MyThread[threadsCount];

        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new MyThread(queue, i + 1);
        }
        for (Thread t: threads) {
            t.start();
        }
    }

    public static Queue <String> addQueue() {

        Queue <String> queue = new ConcurrentLinkedQueue<>();

        try (BufferedReader br = new BufferedReader (new FileReader(INPUT_FILE))) {
            String tmp;
            while ((tmp = br.readLine()) != null) {
                for (String str :tmp.split("\n")) {
                    queue.offer(str);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return queue;
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
    private Queue <String> queue;
    private int numTread;

    public MyThread(Queue <String> queue, int numTread) {
        this.numTread = numTread;
        this.queue = queue;
    }

    public void run() {

        String urll;
        String fileName;
        int numFile;
        String str_c;

        while ((str_c = queue.poll()) != null) {
            String[] str = str_c.split(" ");
            urll = str[1];
            numFile = Integer.parseInt(str[0]);
            fileName =  urll.substring(urll.lastIndexOf('/'));

            System.out.println("Thread-" + numTread + " start download file number " + numFile);
            try {
                downloadUsingStream(urll, ("./ex03/" + fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Thread-" + numTread + " finish download file number " + numFile);
        }
    }

    private void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;

        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }

        fis.close();
        bis.close();
    }
}


