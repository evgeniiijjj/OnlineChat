package util;

import java.nio.channels.*;


public class SelectionKeyTest extends SelectionKey {

    private final ServerSocketChannel serverSocketChannel;
    private final SocketChannel socketChannel;

    private int ops;

    public SelectionKeyTest(ServerSocketChannel serverSocketChannel, SocketChannel socketChannel) {
        this.serverSocketChannel = serverSocketChannel;
        this.socketChannel = socketChannel;
        ops = SelectionKey.OP_ACCEPT;
    }

    @Override
    public SelectableChannel channel() {
        SelectableChannel ch;
        if (ops == SelectionKey.OP_ACCEPT) {
            ch = serverSocketChannel;
            ops = 0;//SelectionKey.OP_READ;
        } else {
            ch = socketChannel;
        }
        return ch;
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
        return ops;
    }
}
