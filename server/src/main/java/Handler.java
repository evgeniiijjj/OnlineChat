import util.Client;
import util.LoggerMessages;
import util.ServerConstants;
import util.ServerMessages;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Handler implements Runnable {

    private final SocketChannel socketChannel;
    private final Map<SocketChannel, Client> clients;
    private final Client client;

    public Handler(SocketChannel socketChannel, Map<SocketChannel, Client> clients) {
        this.socketChannel = socketChannel;
        this.clients = clients;
        client = clients.get(socketChannel);
    }

    @Override
    public void run() {
        try {
            String message = read();
            if (!message.isEmpty()) {
                Main.LOGGER.info(LoggerMessages.READ.getMessage(socketChannel.getRemoteAddress().toString(), client.getName(), message));
                if ("exit".equalsIgnoreCase(message)) {
                    Main.LOGGER.info(LoggerMessages.OUT.getMessage(socketChannel.getRemoteAddress().toString(), client.getName()));
                    clients.remove(socketChannel);
                    socketChannel.close();
                    return;
                }
                if (!client.isAuthorized()) {
                    authorization(message);
                } else {
                    broadcastMessage(message, client.getName(), null);
                }
            }
        } catch (IOException e) {
            try {
                clients.remove(socketChannel);
                socketChannel.close();
                Main.LOGGER.warning(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
            } catch (IOException ex) {
                Main.LOGGER.warning(LoggerMessages.EXCEPTION.getMessage(ex.getMessage()));
            }
        }
    }

    private String read() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2 << 10);
        StringBuilder builder = new StringBuilder();
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            builder.append(new String(buffer.array(), 0, buffer.limit()));
        }
        return builder.toString().trim();
    }

    private void authorization(String name) throws IOException {
        int len = name.length();
        if (len >= ServerConstants.CLIENT_NAME_MIN_LEN.getLen() && len <= ServerConstants.CLIENT_NAME_MAX_LEN.getLen()) {
            boolean checkName = true;
            for (Client cl : clients.values()) {
                if (cl.getName().equalsIgnoreCase(name)) {
                    checkName = false;
                    break;
                }
            }
            if (checkName) {
                client.setName(name);
                Main.LOGGER.info(LoggerMessages.AUTH.getMessage(socketChannel.getRemoteAddress().toString(), name));
                send(socketChannel, ServerMessages.WELCOME.getMessage(name), name);
                broadcastMessage(ServerMessages.WELCOMES.getMessage(name), "Server", socketChannel);
            } else {
                send(socketChannel, ServerMessages.NAME_IS_ALREADY_EXISTS.getMessage(name), client.getName());
            }
        } else {
            send(socketChannel, ServerMessages.FAILURE.getMessage(), client.getName());
        }
    }

    private void broadcastMessage(String message, String nameFrom, SocketChannel except) throws IOException {
        for (SocketChannel target : clients.keySet()) {
            Client cl = clients.get(target);
            if (cl.isAuthorized()) {
                if (except == null || !except.equals(target)) {
                    if ("Server".equals(nameFrom)) {
                        send(target, message, cl.getName());
                    } else {
                        send(target, ServerMessages.BROADCAST.getMessage(nameFrom, message), cl.getName());
                    }
                }
            }
        }
    }

    private void send(SocketChannel socketChannel, String message, String nameTo) throws IOException {
        socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
        Main.LOGGER.info(LoggerMessages.SEND.getMessage(socketChannel.getRemoteAddress().toString(), nameTo, message));
    }
}
