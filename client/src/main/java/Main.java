import util.LoggerMessages;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public final static Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        try {
            FileHandler fh = new FileHandler("logging/file.log", true);
            LOGGER.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.setUseParentHandlers(false);
            Properties prop = new Properties();
            String path = Objects.requireNonNull(Main.class.getClassLoader().getResource("properties")).getPath();
            prop.load(new FileReader(path));
            String address = prop.getProperty("server_address");
            int port = Integer.parseInt(prop.getProperty("server_port"));
            new Client(new InetSocketAddress(address, port)).run();
        } catch (IOException e) {
            Main.LOGGER.severe(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
        }
    }
}
