package util;

import java.io.BufferedReader;
import java.io.Reader;

public class BufferedReaderTest extends BufferedReader {

    public static final int MESSAGES_NUM = 10;
    public static final int MESSAGES_MIN_LEN = 5;
    public static final int MESSAGES_MAX_LEN = 40;
    public static final int PAUSE = 500;

    private int nextLineCallsNum;

    public BufferedReaderTest() {
        super(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) {
                return 0;
            }
            @Override
            public void close() { }
        });
    }

    public String readLine() {

        try {
            Thread.sleep(PAUSE);
        } catch (InterruptedException ignored) {}
        nextLineCallsNum++;

        if (nextLineCallsNum > MESSAGES_NUM) {
            Thread.currentThread().interrupt();
        }
        return Util.generateMessage(MESSAGES_MIN_LEN, MESSAGES_MAX_LEN);
    }
}
