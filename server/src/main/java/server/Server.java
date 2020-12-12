package server;

import com.sun.net.httpserver.HttpExchange;
import models.Film;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import models.Genre;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

public class Server {
    private final DataHelper dataHelper = new DataHelper();
    private static final boolean checkUpdate = false;

    public Server() throws IOException, SQLException {
        dataHelper.clearTable();
        ConfigHelper.initValues();
        updateDataBase();
    }

    public void startServer() throws IOException {
        final String ip = InetAddress.getByName(ConfigHelper.HOST_NAME).getHostAddress();
        final int port = Integer.parseInt(ConfigHelper.HOST_PORT);

        final InetSocketAddress socket = new InetSocketAddress(ip, port);
        final HttpServer server = HttpServer.create(socket, 1);

        System.out.println("Server is ready.");
        server.createContext("/get", httpExchange -> {
            try (httpExchange) {
                var genre = new Genre(getBody(httpExchange));
                if (checkUpdate) {
                    updateDataBase();
                }

                final Headers headers = httpExchange.getResponseHeaders();
                headers.set("Content-Type", String.format("application/json; charset=%s", ConfigHelper.CHARSET));

                final Film filmForGenre = dataHelper.readFilmWithGenre(genre);
                final String responseBody = (filmForGenre != null ? filmForGenre : dataHelper.readRandomFilm()).toString();
                final byte[] rawResponseBody = responseBody.getBytes(ConfigHelper.CHARSET);

                httpExchange.sendResponseHeaders(200, rawResponseBody.length);
                httpExchange.getResponseBody().write(rawResponseBody);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        server.start();
    }

    private String getBody(HttpExchange httpExchange) {
        final URI requestURI = httpExchange.getRequestURI();
        return requestURI.toString().split("\\?")[1].split("=")[1];
    }

    private void updateDataBase() throws IOException, SQLException {
        final List<Film> films = parseWebSite(ConfigHelper.SOURCE_URL);
        dataHelper.insertMany(films);
    }

    private List<Film> parseWebSite(String url) throws IOException {
        var classParse = new Parser();
        var document = classParse.getDocumentForParse(url);
        return classParse.parse(document);
    }
}
