package util;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Random;

public class Util {

    public static final int NUMBER_OF_KEYS_MIN = 10;
    public static final int NUMBER_OF_KEYS_MAX_DIFF = 20;
    public static final int NUMBER_OF_CLIENTS_MIN = 10;
    public static final int NUMBER_OF_CLIENTS_MAX_DIFF = 20;
    public static final int LEN_OF_MESSAGE_MIN = 5;
    public static final int LEN_OF_MESSAGE_MAX_DIFF = 40;
    public static final int PORT_MIN = 50000;
    public static final int PORT_MAX = 65000;

    public static int generateSelectionKeys(SelectorTest selector) {
        Random rnd = new Random();
        int keysNum = NUMBER_OF_KEYS_MIN + rnd.nextInt(NUMBER_OF_KEYS_MAX_DIFF);
        for (int i = 0; i < keysNum; i++) {
            SocketChannelTest socketChannel = new SocketChannelTest(new InetSocketAddress("localhost", PORT_MIN + rnd.nextInt(PORT_MAX)));
            socketChannel.setReceivedMessage(generateMessage(LEN_OF_MESSAGE_MIN, LEN_OF_MESSAGE_MAX_DIFF));
            ServerSocketChannel serverSocketChannel = new ServerSocketChannelTest(socketChannel);
            SelectionKeyTest key = new SelectionKeyTest(serverSocketChannel, socketChannel);
            selector.addKey(key);
        }
        return keysNum;
    }

    public static void generateClients(Map<SocketChannel, Client> clients) {
        Random rnd = new Random();
        int clientsSize = NUMBER_OF_CLIENTS_MIN + rnd.nextInt(NUMBER_OF_CLIENTS_MAX_DIFF);
        for (int i = 0; i < clientsSize; i++) {
            SocketChannelTest socketChannel = new SocketChannelTest(new InetSocketAddress("localhost", PORT_MIN + rnd.nextInt(PORT_MAX)));
            Client client = new Client();
            client.setName(generateMessage(ServerConstants.CLIENT_NAME_MIN_LEN.getLen(), ServerConstants.CLIENT_NAME_MAX_LEN.getLen()));
            clients.put(socketChannel, client);
        }
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
