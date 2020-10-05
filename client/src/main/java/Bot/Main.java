package Bot;

import Models.Film;
import org.apache.http.client.fluent.Request;
import org.bson.Document;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Main
{
    private static Properties properties = new Properties();

    public static void main(String[] args) throws IOException, IllegalAccessException {
        final InetAddress ip = InetAddress.getByName("server");
        System.out.println(ip.toString());
        //properties.load(new FileInputStream("./client.properties"));
        var films = getAllFilms();

        System.out.println(films.get(new Random().nextInt(films.size())));
    }

    private static List<Film> getAllFilms() throws IOException, IllegalAccessException {
        var ip = InetAddress.getByName("server").toString().split("/")[1];
        var jsonArray = Request.Get(String.format("http://%s:4004/get", ip))
                .execute()
                .returnContent()
                .asString()
                .split(";");

        var films = new ArrayList<Film>();
        for (var json: jsonArray) {
            films.add(Film.fromDocument(Document.parse(json)));
        }
        return films;
    }
}
