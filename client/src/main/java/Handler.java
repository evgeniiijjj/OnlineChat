import util.LoggerMessages;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Handler implements Runnable {

    private final Selector selector;
    public Handler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        Main.LOGGER.info(LoggerMessages.HANDLER.getMessage());
        try {
            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey key : selected) {
                    if (key.isReadable()) {
                        System.out.println(read((SocketChannel) key.channel()));
                    }
                }
                selected.clear();
            }
        } catch (IOException e) {
            Main.LOGGER.warning(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
        }
    }

    private String read(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2 << 10);
        StringBuilder builder = new StringBuilder();
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            builder.append(new String(buffer.array(), 0, buffer.limit(), StandardCharsets.UTF_8));
        }
        Main.LOGGER.info(LoggerMessages.READ.getMessage(socketChannel.getRemoteAddress().toString(), builder.toString()));
        return builder.toString();
    }
}
