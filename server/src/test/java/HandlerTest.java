import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.nio.channels.*;
import java.util.*;
import util.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Util.*;

class HandlerTest {

    static Map<SocketChannel, Client> clients;
    static Handler handler;
    static SocketChannelTest socketChannel;

    @BeforeAll
    static void setUp() {
        clients = new HashMap<>();
        generateClients(clients);
        socketChannel = (SocketChannelTest) clients.keySet().iterator().next();
        handler = new Handler(socketChannel, clients);
    }

    @Test
    void testRun() {
        testAuthorizationIfMessageIsTooShort();
        testAuthorizationIfMessageIsTooLong();
        testAuthorizationIfNameIsAlreadyUsed();
        testAuthorizationIfMessageIsNormal();
        testBroadcastMessage();
    }

    @Test
    void testAuthorizationIfMessageIsTooShort() {
        Client client = clients.get(socketChannel);
        client.setName("unknown");
        String message = generateMessage(0, ServerConstants.CLIENT_NAME_MIN_LEN.getLen() - 1);
        socketChannel.setReceivedMessage(message);
        handler.run();
        assertFalse(client.isAuthorized());
    }

    @Test
    void testAuthorizationIfMessageIsTooLong() {
        Client client = clients.get(socketChannel);
        client.setName("unknown");
        String message = generateMessage(ServerConstants.CLIENT_NAME_MAX_LEN.getLen() + 1, 40);
        socketChannel.setReceivedMessage(message);
        handler.run();
        assertFalse(client.isAuthorized());
    }

    @Test
    void testAuthorizationIfNameIsAlreadyUsed() {
        Client client = clients.get(socketChannel);
        client.setName("unknown");
        Iterator<SocketChannel> it = clients.keySet().iterator();
        SocketChannel sh = it.next();
        if (socketChannel.equals(sh)) sh = it.next();
        String message = clients.get(sh).getName();
        socketChannel.setReceivedMessage(message);
        handler.run();
        assertFalse(client.isAuthorized());
    }

    @Test
    void testAuthorizationIfMessageIsNormal() {
        Client client = clients.get(socketChannel);
        client.setName("unknown");
        String message = generateMessage(ServerConstants.CLIENT_NAME_MIN_LEN.getLen(), ServerConstants.CLIENT_NAME_MAX_LEN.getLen());
        socketChannel.setReceivedMessage(message);
        handler.run();
        assertTrue(client.isAuthorized());
    }

    @Test
    void testBroadcastMessage() {
        String message = generateMessage(10, 40);
        socketChannel.setReceivedMessage(message);
        handler.run();
        message = socketChannel.getSentMessage();
        boolean check = true;
        for (SocketChannel sk : clients.keySet()) {
            if (!message.equals(((SocketChannelTest) sk).getSentMessage())) {
                check = false;
                break;
            }
        }
        assertTrue(check);
    }

}