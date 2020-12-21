package bot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {
    private static final Properties config = new Properties();

    public static String HOST;
    public static int PORT;
    public static String DELIMITER;

    static {
        try {
            initProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HOST = config.getProperty("HOST_NAME");
        PORT = Integer.parseInt(config.getProperty("HOST_PORT"));
        DELIMITER = config.getProperty("DELIMITER");
    }

    private static void initProperties() throws IOException {
        final InputStream propertiesSource = ConfigHelper.class.getResourceAsStream("/config.ini");
        config.load(propertiesSource);
    }
}
