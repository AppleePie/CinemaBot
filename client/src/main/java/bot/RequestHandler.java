package bot;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

public class RequestHandler {
    private static final Properties config = new Properties();

    private static String HOST_NAME;
    private static int PORT;

    static {
        try {
            initProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HOST_NAME = config.getProperty("HOST_NAME");
        PORT = Integer.parseInt(config.getProperty("HOST_PORT"));
    }

    public static String getFilm(String criteria) throws IOException {
        final String ip = InetAddress.getByName(HOST_NAME).getHostAddress();

        return Request.Get(String.format("http://%s:%d/get?parts=%s", ip, PORT, criteria))
                .execute()
                .returnContent()
                .asString();
    }

    private static void initProperties() throws IOException {
        final InputStream propertiesSource = RequestHandler.class.getClass().getResourceAsStream("/config.ini");
        config.load(propertiesSource);
    }
}
