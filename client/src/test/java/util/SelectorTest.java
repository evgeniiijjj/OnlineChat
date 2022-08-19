package util;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.util.HashSet;
import java.util.Set;

public class SelectorTest extends AbstractSelector {

    private Set<SelectionKey> keys;

    public SelectorTest() {
        super(null);
        keys = new HashSet<>();
    }

    protected SelectionKey register(AbstractSelectableChannel ch, int ops, Object att) {
        return null;
    }

    @Override
    public Set<SelectionKey> keys() {
        return keys;
    }

    @Override
    public Set<SelectionKey> selectedKeys() {
        return keys;
    }

    @Override
    public int selectNow() {
        return 0;
    }

    @Override
    public int select(long timeout) {
        return 0;
    }

    @Override
    public int select() {
        if (keys.isEmpty()) {
            Thread.currentThread().interrupt();
        }
        return 0;
    }

    @Override
    public Selector wakeup() {
        return null;
    }

    protected void implCloseSelector() { }

    public void addKey(SelectionKey key) {
        keys.add(key);
    }
}
