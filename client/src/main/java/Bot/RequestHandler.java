package Bot;

import Models.Film;
import org.apache.http.client.fluent.Request;
import org.bson.Document;

import java.io.IOException;
import java.net.InetAddress;

public class RequestHandler {
    private final static String HOST_NAME = "localhost";
//    private final static String HOST_NAME = "server"; //for docker
    private final static int PORT = 4004;

    public Film getFilm() throws IOException, IllegalAccessException {
        final String ip = InetAddress.getByName(HOST_NAME).getHostAddress();
        var jsonResponse = Request.Get(String.format("http://%s:%d/get", ip, PORT))
                .execute()
                .returnContent()
                .asString();

        return Film.fromDocument(Document.parse(jsonResponse));
    }
}
