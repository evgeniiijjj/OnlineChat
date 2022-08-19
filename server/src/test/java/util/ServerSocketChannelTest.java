package util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class ServerSocketChannelTest extends ServerSocketChannel {

    private SocketChannel socketChannel;
    public static int acceptCallsNum;

    public ServerSocketChannelTest(SocketChannel socketChannel) {
        super(null);
        this.socketChannel = socketChannel;
    }

    public ServerSocketChannelTest() {
        super(null);
    }

    @Override
    public ServerSocketChannel bind(SocketAddress local, int backlog) {
        return null;
    }

    @Override
    public <T> ServerSocketChannel setOption(SocketOption<T> name, T value) {
        return null;
    }

    @Override
    public <T> T getOption(SocketOption<T> name) {
        return null;
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return null;
    }

    @Override
    public ServerSocket socket() {
        try {
            return new ServerSocketTest();
        } catch (IOException ignored) { }
        return null;
    }

    @Override
    public SocketChannel accept() {
        acceptCallsNum++;
        return socketChannel;
    }

    @Override
    public SocketAddress getLocalAddress() {
        return null;
    }

    @Override
    protected void implCloseSelectableChannel() { }

    @Override
    protected void implConfigureBlocking(boolean block) { }
}
