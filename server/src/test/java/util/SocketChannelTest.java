package util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class SocketChannelTest extends SocketChannel {

    private String receivedMessage;
    private String sentMessage;

    private final InetSocketAddress remoteAddress;

    public SocketChannelTest(InetSocketAddress remoteAddress) {
        super(null);
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void implCloseSelectableChannel() { }

    @Override
    protected void implConfigureBlocking(boolean block) { }

    @Override
    public SocketChannel bind(SocketAddress local) {
        return null;
    }

    @Override
    public <T> SocketChannel setOption(SocketOption<T> name, T value) {
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
    public SocketChannel shutdownInput() {
        return null;
    }

    @Override
    public SocketChannel shutdownOutput() {
        return null;
    }

    @Override
    public Socket socket() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isConnectionPending() {
        return false;
    }

    @Override
    public boolean connect(SocketAddress remote) {
        return false;
    }

    @Override
    public boolean finishConnect() {
        return false;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public int read(ByteBuffer dst) {
        if (dst.limit() == dst.capacity())
            dst.put(receivedMessage.getBytes(StandardCharsets.UTF_8));
        return dst.position();
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) {
        return 0;
    }

    @Override
    public int write(ByteBuffer src) {
        int pos = src.position();
        src.flip();
        sentMessage = new String(src.array(), 0, src.limit());
        return pos;
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) {
        return 0;
    }

    @Override
    public SocketAddress getLocalAddress() {
        return null;
    }

    public String getSentMessage() {
        return sentMessage;
    }

    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage = receivedMessage;
    }
}
