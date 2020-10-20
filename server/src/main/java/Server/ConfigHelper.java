package Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {
    private static Properties config = new Properties();

    public static String HOST_PORT;
    public static String HOST_NAME;
    public static String SOURCE_URL;
    public static String CHARSET;

    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASS;

    public static void initValues() throws IOException {
        initServerProperties();
        HOST_PORT = config.getProperty("HOST_PORT");
        HOST_NAME = config.getProperty("HOST_NAME");
        SOURCE_URL = config.getProperty("SOURCE_URL");
        CHARSET = config.getProperty("CHARSET");

        DB_URL = config.getProperty("DB_URL");
        DB_USER = config.getProperty("DB_USER");
        DB_PASS = config.getProperty("DB_PASS");

    }

    private static void initServerProperties() throws IOException {
        final InputStream propertiesSource = ConfigHelper.class.getResourceAsStream("/config.ini");
        config.load(propertiesSource);
    }
}
