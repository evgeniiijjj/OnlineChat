package util;

public enum ServerConstants {

    CLIENT_NAME_MIN_LEN (3),
    CLIENT_NAME_MAX_LEN (10);

    private int len;

    ServerConstants(int len) {
        this.len = len;
    }

    public int getLen() {
        return len;
    }
}
