package Bot;

import Models.Film;
import org.apache.http.client.fluent.Request;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Main
{
    private static Properties clientProperties = new Properties();

    public static void main(String[] args) throws IOException, IllegalAccessException {
        initClientProperties();
        var films = getAllFilms();

        System.out.println(films.get(new Random().nextInt(films.size())));
    }

    private static List<Film> getAllFilms() throws IOException, IllegalAccessException {
        final String host = clientProperties.getProperty("NETWORK");
        final String port = clientProperties.getProperty("NETWORK_PORT");
        final String ip = InetAddress.getByName(host).getHostAddress();
        var jsonArray = Request.Get(String.format("http://%s:%s/get", ip, port))
                .execute()
                .returnContent()
                .asString()
                .split(clientProperties.getProperty("SEPARATOR"));

        var films = new ArrayList<Film>();
        for (var json: jsonArray) {
            films.add(Film.fromDocument(Document.parse(json)));
        }
        return films;
    }

    private static void initClientProperties() throws IOException {
        final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        final String serverConfigPath = rootPath + "client.properties";
        clientProperties.load(new FileInputStream(serverConfigPath));
    }
}
