import util.Client;
import util.LoggerMessages;
import util.ServerMessages;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {

    private final Map<SocketChannel, Client> clients;
    private final Selector selector;
    private final ServerSocketChannel serverSocket;
    private final ExecutorService threadPool;

    public Server(int port) throws IOException {
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        clients = new ConcurrentHashMap<>();
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        Main.LOGGER.info(LoggerMessages.START.getMessage());
        try {
            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey key : selected) {
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        threadPool.execute(new Handler(socketChannel, clients));
                    }
                }
                selected.clear();
            }
            Main.LOGGER.info(LoggerMessages.STOP.getMessage());
        } catch (Exception e) {
            Main.LOGGER.severe(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
        } finally {
            threadPool.shutdown();
            try {
                selector.close();
                serverSocket.close();
            } catch (IOException e) {
                Main.LOGGER.warning(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        Main.LOGGER.info(LoggerMessages.ACCEPT.getMessage(socketChannel.getRemoteAddress().toString()));
        socketChannel.configureBlocking(false);
        clients.put(socketChannel, new Client());
        socketChannel.register(selector, SelectionKey.OP_READ);
        send(socketChannel, ServerMessages.INFO.getMessage());
    }

    private void send(SocketChannel socketChannel, String message) throws IOException {
        socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
        Main.LOGGER.info(LoggerMessages.SEND.getMessage(socketChannel.getRemoteAddress().toString(), clients.get(socketChannel).getName(), message));
    }
}
