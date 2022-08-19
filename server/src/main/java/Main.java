import util.LoggerMessages;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public final static Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {

        try {
            FileHandler fh = new FileHandler("logging/file.log", true);
            LOGGER.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.setUseParentHandlers(false);
            String path = Objects.requireNonNull(Main.class.getResource("properties")).getPath();
            Properties prop = new Properties();
            prop.load(new FileReader(path));
            int port = Integer.parseInt(prop.getProperty("port"));
            new Server(port).run();
        } catch (IOException e) {
            LOGGER.severe(LoggerMessages.EXCEPTION.getMessage(e.getMessage()));
        }
    }
}
