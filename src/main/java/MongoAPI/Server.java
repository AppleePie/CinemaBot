package MongoAPI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.bson.Document;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Server {
    private static DataHelper dataHelper;

    public static void main(String[] args) throws IOException {
        dataHelper = new DataHelper();
        dataHelper.initDB("films");
        server();
    }

    private static void server() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 4004), 1);
        server.createContext("/get", he -> {
            try (he) {
                System.out.println("Сервер!");
                final Headers headers = he.getResponseHeaders();
                final String requestMethod = he.getRequestMethod().toUpperCase();
                
                final String responseBody = getJSONArrayFromDocuments(dataHelper.getAllData());
                headers.set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
                final byte[] rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
                
                he.sendResponseHeaders(200, rawResponseBody.length);
                he.getResponseBody().write(rawResponseBody);
            }
        });
        server.start();
    }
    
    private static String getJSONArrayFromDocuments(List<Document> data) {
        var result = new StringBuilder();
        var separator = ";";
        for (var document: data) {
            result.append(document.toJson()).append(separator);
        }
        return result.toString();
    }
}
