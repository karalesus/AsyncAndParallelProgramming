package java.iterativeParallelism;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SummationThread extends Thread {

    private long startFrom, endTo;

    private SummationCallback callback;

    public SummationThread(long startFrom, long endTo, SummationCallback callback) {
        this.startFrom = startFrom;
        this.endTo = endTo;
        this.callback = callback;
    }

    @Override
    public void run() {
        long partOfSum = 0;
        for (long i = startFrom + 1; i <= endTo; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(File.FILE_PATH + File.FILE_NAME + i + File.FILE_TYPE))) {
                String line;

                while ((line = br.readLine()) != null) {
                    partOfSum += Integer.parseInt(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // через обратный вызов, кидаем результат
        callback.call(partOfSum);
    }
}
