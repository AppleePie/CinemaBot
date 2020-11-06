package server;

import models.Film;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.List;

public class Server {
    private final DataHelper dataHelper = new DataHelper();
    private static final boolean checkUpdate = false;

    public Server() throws IOException, SQLException {
        //dataHelper.clearTable();
        ConfigHelper.initValues();
        //updateDataBase();
    }

    public void startServer() throws IOException {
        final String ip = InetAddress.getByName(ConfigHelper.HOST_NAME).getHostAddress();
        final int port = Integer.parseInt(ConfigHelper.HOST_PORT);

        final InetSocketAddress socket = new InetSocketAddress(ip, port);
        final HttpServer server = HttpServer.create(socket, 1);

        server.createContext("/get", he -> {
            try (he) {
                if (checkUpdate) {
                    updateDataBase();
                }

                final Headers headers = he.getResponseHeaders();
                headers.set("Content-Type", String.format("application/json; charset=%s", ConfigHelper.CHARSET));

                final String responseBody = dataHelper.readRandomFilm().toString();
                final byte[] rawResponseBody = responseBody.getBytes(ConfigHelper.CHARSET);

                he.sendResponseHeaders(200, rawResponseBody.length);
                he.getResponseBody().write(rawResponseBody);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        server.start();
    }

    private void updateDataBase() throws IOException, SQLException {
        final List<Film> films = parseWebSite(ConfigHelper.SOURCE_URL);
        dataHelper.insertMany(films);
    }

    private List<Film> parseWebSite(String url) throws IOException {
        var classParse = new Parser();
        var document = classParse.GetDocumentForParse(url);
        return classParse.Parse(document);
    }
}
