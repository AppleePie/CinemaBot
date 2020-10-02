package Bot;

import Models.Film;
import org.apache.http.client.fluent.Request;
import org.bson.Document;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main
{
    public static void main(String[] args) throws IOException, IllegalAccessException {
        var films = getAllFilms();

        System.out.println(films.get(new Random().nextInt(films.size())));
    }

    private static List<Film> getAllFilms() throws IOException, IllegalAccessException {
        var separator = ";";
        var ip = "http://" + InetAddress.getLocalHost().toString().split("/")[1] + ":4004/get";
        System.out.println(ip);
        var jsonArray = Request.Get(ip)
                .execute()
                .returnContent()
                .asString()
                .split(separator);

        var films = new ArrayList<Film>();
        for (var json: jsonArray) {
            films.add(Film.fromDocument(Document.parse(json)));
        }
        return films;
    }
}
