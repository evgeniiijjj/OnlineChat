package util;

import java.nio.channels.*;


public class SelectionKeyTest extends SelectionKey {

    public static int channelCallsNum;

    private final SocketChannel socketChannel;

    public SelectionKeyTest(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public SelectableChannel channel() {
        channelCallsNum++;
        return socketChannel;
    }

    @Override
    public Selector selector() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void cancel() { }

    @Override
    public int interestOps() {
        return 0;
    }

    @Override
    public SelectionKey interestOps(int ops) {
        return null;
    }

    @Override
    public int readyOps() {
        return SelectionKey.OP_READ;
    }
}
