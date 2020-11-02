package Bot;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

public class RequestHandler {
    private static Properties config = new Properties();

    private static String HOST_NAME;
    private static int PORT;

    public RequestHandler() throws IOException {
        initProperties();

        HOST_NAME = config.getProperty("HOST_NAME");
        PORT = Integer.parseInt(config.getProperty("HOST_PORT"));
    }

    private void initProperties() throws IOException {
        final InputStream propertiesSource = this.getClass().getResourceAsStream("/config.ini");
        config.load(propertiesSource);
    }

    public String getFilm() throws IOException, IllegalAccessException {
        final String ip = InetAddress.getByName(HOST_NAME).getHostAddress();

        return Request.Get(String.format("http://%s:%d/get", ip, PORT))
                .execute()
                .returnContent()
                .asString();
    }
}
