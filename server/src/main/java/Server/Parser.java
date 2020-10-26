package Server;

import Models.Film;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Parser{
    public Document GetDocumentForParse(String link) throws IOException {
        return Jsoup.connect(link).get();
    }

    public ArrayList<Film> Parse(Document document){
        var films = new ArrayList<Film>();
        var elements = document.getElementsByAttributeValue("class", "titleColumn");

        elements.forEach(element -> {
            var tdElement = element.child(0);
            var url = "www.imdb.com";
            url += tdElement.attr("href");
            var title = tdElement.text();
            films.add(new Film(title, url));
        });

        return films;
    }
}