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

        for (var field: ConfigHelper.class.getFields()) {
            try {
                field.set(ConfigHelper.class, config.getProperty(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initServerProperties() throws IOException {
        final InputStream propertiesSource = ConfigHelper.class.getResourceAsStream("/config.ini");
        config.load(propertiesSource);
    }
}
