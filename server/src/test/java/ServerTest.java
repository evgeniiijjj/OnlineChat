import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.SelectorTest;
import util.ServerSocketChannelTest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Util.*;

class ServerTest {

    static final int SERVER_PORT = 8085;

    static Server server;
    static int keysNum;

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        SelectorTest selector = new SelectorTest();
        keysNum = generateSelectionKeys(selector);
        ServerSocketChannel serverSocket = new ServerSocketChannelTest();
        server = new Server(SERVER_PORT);
        Field field = server.getClass().getDeclaredField("selector");
        field.setAccessible(true);
        ((Selector) field.get(server)).close();
        field.set(server, selector);
        field = server.getClass().getDeclaredField("serverSocket");
        field.setAccessible(true);
        ((ServerSocketChannel) field.get(server)).close();
        field.set(server, serverSocket);
    }

    @Test
    void testAccept() {
        server.run();
        assertEquals(keysNum, ServerSocketChannelTest.acceptCallsNum);
    }
}