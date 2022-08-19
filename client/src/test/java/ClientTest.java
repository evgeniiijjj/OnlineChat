import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    static final String SERVER_ADDRESS = "localhost";
    static final int SERVER_PORT = 8085;
    static Client client;

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        InetSocketAddress address = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        SelectorTest selector = new SelectorTest();
        SocketChannel socketChannel = new SocketChannelTest(address);
        client = new Client(address);
        Field field = client.getClass().getDeclaredField("selector");
        field.setAccessible(true);
        ((Selector) field.get(client)).close();
        field.set(client, selector);
        field = client.getClass().getDeclaredField("socketChannel");
        field.setAccessible(true);
        ((SocketChannel) field.get(client)).close();
        field.set(client, socketChannel);
        field = client.getClass().getDeclaredField("reader");
        field.setAccessible(true);
        BufferedReader reader = (BufferedReader) field.get(client);
        reader.close();
        reader = new BufferedReaderTest();
        field.set(client, reader);
    }

    @Test
    void testSendMessage() {
        client.run();
        assertEquals(BufferedReaderTest.MESSAGES_NUM, SocketChannelTest.SENT_MESSAGES_NUM);
    }
}
