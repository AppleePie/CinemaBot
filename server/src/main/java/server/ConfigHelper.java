package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {
    private static final Properties config = new Properties();

    public static String HOST_PORT;
    public static String HOST_NAME;
    public static String SOURCE_URL;
    public static String CHARSET;

    public static String LINK_ATTR;
    public static String WEBSITE;
    public static String MOVIE_TAG_IN_THE_TOP;
    public static String TAG_FOR_BASIC_INFORMATION_ABOUT_THE_MOVIE;
    public static String TAG_OF_THE_ORIGINAL_MOVIE_TITLE;
    public static String TITLE_TAG;
    public static String SUBTEXT_TAG;
    public static String TIME_TAG;
    public static String LINK_TAG;
    public static String TAG_FOR_DETAILED_INFORMATION_ABOUT_THE_MOVIE;
    public static String DESCRIPTION_TAG;
    public static String POSTER_TAG;
    public static String MULTIMEDIA_TAG;
    public static String IMAGE_TAG;
    public static String TAG_THE_ADDRESS_OF_THE_FILE;
    public static String DELIMITER;

    static {
        try {
            initServerProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HOST_PORT = config.getProperty("HOST_PORT");
        HOST_NAME = config.getProperty("HOST_NAME");
        SOURCE_URL = config.getProperty("SOURCE_URL");
        CHARSET = config.getProperty("CHARSET");

        LINK_ATTR = config.getProperty("LINK_ATTR");
        WEBSITE = config.getProperty("WEBSITE");
        MOVIE_TAG_IN_THE_TOP = config.getProperty("MOVIE_TAG_IN_THE_TOP");
        TAG_FOR_BASIC_INFORMATION_ABOUT_THE_MOVIE = config.getProperty("TAG_FOR_BASIC_INFORMATION_ABOUT_THE_MOVIE");
        TAG_OF_THE_ORIGINAL_MOVIE_TITLE = config.getProperty("TAG_OF_THE_ORIGINAL_MOVIE_TITLE");
        TITLE_TAG = config.getProperty("TITLE_TAG");
        SUBTEXT_TAG = config.getProperty("SUBTEXT_TAG");
        TIME_TAG = config.getProperty("TIME_TAG");
        LINK_TAG = config.getProperty("LINK_TAG");
        TAG_FOR_DETAILED_INFORMATION_ABOUT_THE_MOVIE = config.getProperty(
                "TAG_FOR_DETAILED_INFORMATION_ABOUT_THE_MOVIE");
        DESCRIPTION_TAG = config.getProperty("DESCRIPTION_TAG");
        POSTER_TAG = config.getProperty("POSTER_TAG");
        MULTIMEDIA_TAG = config.getProperty("MULTIMEDIA_TAG");
        IMAGE_TAG = config.getProperty("IMAGE_TAG");
        TAG_THE_ADDRESS_OF_THE_FILE = config.getProperty("TAG_THE_ADDRESS_OF_THE_FILE");
        DELIMITER = config.getProperty("DELIMITER");
    }

    private static void initServerProperties() throws IOException {
        final InputStream propertiesSource = ConfigHelper.class.getResourceAsStream("/config.ini");
        config.load(propertiesSource);
    }
}
