package util;


public class Client {

    private String name;

    public Client() {
        name = "unknown";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isAuthorized() {
        return !name.equals("unknown");
    }
}
