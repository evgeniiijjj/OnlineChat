import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Util.*;

class HandlerTest {

    static final String SERVER_ADDRESS = "localhost";
    static final int SERVER_PORT = 8085;

    static Handler handler;
    static int messagesNum;

    @BeforeAll
    static void setUp() {
        SelectorTest selector = new SelectorTest();
        messagesNum = generateSelectionKeys(selector, SERVER_ADDRESS, SERVER_PORT);
        handler = new Handler(selector);
    }

    @Test
    void testReadMessage() {
        handler.run();
        assertEquals(messagesNum, SelectionKeyTest.channelCallsNum);
    }

}