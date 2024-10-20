package java.iterativeParallelism;

import java.io.*;

//3 3 3 4

public class Main {
    private static final int COUNT = 4;
    private static long sumMany = 0;

    public static void main(String[] args) throws InterruptedException {
        oneThread();
        manyThreads();
    }

    private static void oneThread() {
        long time = System.currentTimeMillis();
        long sum = 0;
        for (int i = 1; i <= File.FILES_NUMBER; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(File.FILE_PATH + File.FILE_NAME + i + File.FILE_TYPE))) {
                String line;

                while ((line = br.readLine()) != null) {
                    sum += Integer.parseInt(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Sum = " + sum + "\nOne thread worked " + time + "ms and exited\n");
    }

    private static void manyThreads() throws InterruptedException {
        long time = System.currentTimeMillis();

        Thread[] threads = new Thread[COUNT];

        SummationCallback callback = new SummationCallback() {
            @Override
            public void call(long part) {
                sumMany += part;
            }
        };

        int numberOfFiles = File.FILES_NUMBER / COUNT; // кол-во файлов для каждого потока
        int remainNumberOfFiles = File.FILES_NUMBER % COUNT; // кол-во файлов которое осталось

        // Создать и запустить
        for (int i = 0; i < COUNT; i++) {

                int startFrom = numberOfFiles * i;
                int endTo = numberOfFiles * (i + 1);

                if (i == COUNT - 1) {
                    endTo += remainNumberOfFiles;  // Добавляем оставшиеся файлы к последнему потоку
                }

                threads[i] = new SummationThread(startFrom, endTo, callback);
                threads[i].start();
        }

        for (int i = COUNT - 1; i >= 0; i--) {
            if (threads[i].isAlive()) {
                threads[i].join();
            }
        }

        System.out.println("Sum of " + COUNT + " threads = " + sumMany);
        long timeForManyThreads = System.currentTimeMillis() - time;
        System.out.println("All threads worked " + timeForManyThreads + "ms and exited");
    }
}
