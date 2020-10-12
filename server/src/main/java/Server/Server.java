package Server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.bson.Document;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.List;

public class Server {
    private final DataHelper dataHelper = new DataHelper();

    private static final int PORT = 4004;
    private static final String HOST_NAME = "localhost"; //for local development
//    private final String HOST_NAME = "server"; //for using docker
    private static final String SOURCE_URL = "https://www.imdb.com/chart/top/";
    private static final String CHARSET = "UTF-8";

    private static final boolean checkUpdate = false;

    public Server() throws IOException, SQLException, ClassNotFoundException {
        dataHelper.initDB();
        updateDataBase();
    }

    public void startServer() throws IOException {
        final String ip = InetAddress.getByName(HOST_NAME).getHostAddress();
        final InetSocketAddress socket = new InetSocketAddress(ip, PORT);
        final HttpServer server = HttpServer.create(socket, 1);

        server.createContext("/get", he -> {
            try (he) {
                if (checkUpdate) {
                    updateDataBase();
                }

                final Headers headers = he.getResponseHeaders();
                headers.set("Content-Type", String.format("application/json; charset=%s", CHARSET));

                final String responseBody = dataHelper.readRandomFilm().toJson();
                final byte[] rawResponseBody = responseBody.getBytes(CHARSET);

                he.sendResponseHeaders(200, rawResponseBody.length);
                he.getResponseBody().write(rawResponseBody);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        server.start();
    }

    private void updateDataBase() throws IOException, SQLException {
        var filmsFromIMDB = parseWebSite(SOURCE_URL);
        dataHelper.insertMany(filmsFromIMDB);
    }

    private List<Document> parseWebSite(String url) throws IOException {
        var classParse = new Parser();
        var document = classParse.GetDocumentForParse(url);
        return classParse.Parse(document);
    }
}
