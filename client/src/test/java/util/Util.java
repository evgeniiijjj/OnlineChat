package util;

import java.net.InetSocketAddress;
import java.util.Random;

public class Util {

    public static final int NUMBER_OF_KEYS_MIN = 10;
    public static final int NUMBER_OF_KEYS_MAX = 20;
    public static final int LEN_OF_MESSAGE_MIN = 5;
    public static final int LEN_OF_MESSAGE_MAX = 40;

    public static int generateSelectionKeys(SelectorTest selector, String address, int port) {
        Random rnd = new Random();
        int keysNum = NUMBER_OF_KEYS_MIN + rnd.nextInt(NUMBER_OF_KEYS_MAX);
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        for (int i = 0; i < keysNum; i++) {
            SocketChannelTest socketChannel = new SocketChannelTest(socketAddress);
            socketChannel.setReceivedMessage(generateMessage(LEN_OF_MESSAGE_MIN, LEN_OF_MESSAGE_MAX));
            SelectionKeyTest key = new SelectionKeyTest(socketChannel);
            selector.addKey(key);
        }
        return keysNum;
    }

    public static String generateMessage(int minMessageLen, int maxMessageLen) {
        Random rnd = new Random();
        int messageLen = minMessageLen + rnd.nextInt(maxMessageLen - minMessageLen);
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < messageLen; i++) {
            message.append((char) (100 + rnd.nextInt(20)));
        }
        return message.toString();
    }
}
