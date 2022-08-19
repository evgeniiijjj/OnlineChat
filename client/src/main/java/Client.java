import util.LoggerMessages;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


public class Client implements Runnable {

    private final BufferedReader reader;
    private final Selector selector;
    private final SocketChannel socketChannel;
    private final InetSocketAddress inetSocketAddress;

    public Client(InetSocketAddress inetSocketAddress) throws IOException {
        this.inetSocketAddress = inetSocketAddress;
        reader = new BufferedReader(new InputStreamReader(System.in));
        selector = Selector.open();
        socketChannel = SocketChannel.open();
    }

    @Override
    public void run() {
        Main.LOGGER.info(LoggerMessages.START.getMessage());
        try {
            connect();
            String messageForSend = "";
            do {
                messageForSend = reader.readLine();
                if (!messageForSend.isEmpty()) {
                    send(messageForSend);
                }
            } while (!Thread.currentThread().isInterrupted() && !"exit".equalsIgnoreCase(messageForSend));
        } catch (IOException e) {
            Main.LOGGER.severe(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
        } finally {
            try {
                socketChannel.close();
                reader.close();
            } catch (IOException e) {
                Main.LOGGER.warning(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
            }
        }
        Main.LOGGER.info(LoggerMessages.STOP.getMessage());
    }

    private void connect() throws IOException {
        socketChannel.connect(inetSocketAddress);
        Main.LOGGER.info(LoggerMessages.CONNECT.getMessage(socketChannel.getRemoteAddress().toString()));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        Thread thread = new Thread(new Handler(selector));
        thread.setDaemon(true);
        thread.start();
    }

    private void send(String messageForSend) throws IOException {
        socketChannel.write(ByteBuffer.wrap(messageForSend.getBytes(StandardCharsets.UTF_8)));
        Main.LOGGER.info(LoggerMessages.SEND.getMessage(socketChannel.getRemoteAddress().toString(), messageForSend));
    }
}
