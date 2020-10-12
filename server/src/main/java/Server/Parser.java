package Server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Parser{
    public Document GetDocumentForParse(String link) throws IOException {
        return Jsoup.connect(link).get();
    }

    public ArrayList<org.bson.Document> Parse(Document document){
        var films = new ArrayList<org.bson.Document>();
        var elements = document.getElementsByAttributeValue("class", "titleColumn");

        elements.forEach(element -> {
            var tdElement = element.child(0);
            var url = "www.imdb.com";
            url += tdElement.attr("href");
            var text = tdElement.text();
            var doc = new org.bson.Document();
                doc.put("title", text);
                doc.put("url", url);
            films.add(doc);
        });
        return films;
    }
}