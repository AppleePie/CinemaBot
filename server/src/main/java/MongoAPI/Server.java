package MongoAPI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;

public class Server {
    private static DataHelper dataHelper;
    private static final Properties serverProperties = new Properties();

    public static void main(String[] args) throws IOException {
        initServerProperties();
        dataHelper = new DataHelper();
        dataHelper.initDB("films");
        startServer();
    }

    private static void startServer() throws IOException {
        final String ip = InetAddress.getByName(serverProperties.getProperty("NETWORK")).getHostAddress();
        final int port = Integer.parseInt(serverProperties.getProperty("SERVER_PORT"));
        final InetSocketAddress socket = new InetSocketAddress(ip, port);
        final HttpServer server = HttpServer.create(socket, 1);
        server.createContext("/get", he -> {
            try (he) {
                System.out.println("Сервер активен.");
                final Headers headers = he.getResponseHeaders();
                final String charset = serverProperties.getProperty("CHARSET");
                headers.set("Content-Type", String.format("application/json; charset=%s", charset));
                
                final String responseBody = getJSONArrayFromDocuments(dataHelper.getAllData());
                final byte[] rawResponseBody = responseBody.getBytes(charset);
                
                he.sendResponseHeaders(200, rawResponseBody.length);
                he.getResponseBody().write(rawResponseBody);
            } catch (Exception e) {
                System.out.println("Сервер отключен.");
            }
        });
        server.start();
    }
    
    private static String getJSONArrayFromDocuments(List<Document> data) {
        var result = new StringBuilder();
        var separator = serverProperties.getProperty("SEPARATOR");
        for (var document: data) {
            result.append(document.toJson()).append(separator);
        }
        return result.toString();
    }

    private static void initServerProperties() throws IOException {
        final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        final String serverConfigPath = rootPath + "server.properties";
        serverProperties.load(new FileInputStream(serverConfigPath));
    }
}
