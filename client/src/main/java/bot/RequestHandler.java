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

    public RequestHandler() throws IOException {
        initProperties();

        HOST_NAME = config.getProperty("HOST_NAME");
        PORT = Integer.parseInt(config.getProperty("HOST_PORT"));
    }

    public String getFilm() throws IOException {
        final String ip = InetAddress.getByName(HOST_NAME).getHostAddress();
        final String genre = "Drama";

        return Request.Get(String.format("http://%s:%d/get?parts=%s", ip, PORT, genre))
                .execute()
                .returnContent()
                .asString();
    }

    private void initProperties() throws IOException {
        final InputStream propertiesSource = this.getClass().getResourceAsStream("/config.ini");
        config.load(propertiesSource);
    }
}
