package server;

import models.Film;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public Document GetDocumentForParse(String link) throws IOException {
        return Jsoup.connect(link).get();
    }

    public ArrayList<Film> Parse(Document document) {
        var films = new ArrayList<Film>();
        var elements = document.getElementsByAttributeValue("class", "titleColumn");

        for (var id = 0; id < elements.size(); id++) {
            var tdElement = elements.get(id).child(0);
            var url = "https://www.imdb.com";
            url += tdElement.attr("href");
            try {
                var film = ParseFilmPage(id, url);
                films.add(film);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return films;
    }

    public Film ParseFilmPage(int id, String link) throws IOException {
        var documentForParse = GetDocumentForParse(link);
        var maneInformation = documentForParse.select("div.title_wrapper").get(0);

        var originalTitle = maneInformation.select("div.originalTitle").text();
        if (originalTitle.equals("")) {
            originalTitle = maneInformation.select("h1").text();
            originalTitle = originalTitle.substring(0, originalTitle.length() - 7);
        } else
            originalTitle = originalTitle.substring(0, originalTitle.length() - 17);

        var subtext = maneInformation.select("div.subtext").get(0).children();
        var timing = subtext.select("time").text();

        var genreAndFullReleaseDate = subtext.select("a").textNodes();

        var genres = new ArrayList<String>();
        for (var i = 0; i < genreAndFullReleaseDate.size() - 1; i++)
            genres.add(genreAndFullReleaseDate.get(i).toString());
        var fullReleaseDate = genreAndFullReleaseDate.get(genreAndFullReleaseDate.size() - 1).toString();

        var generalInformation
                = documentForParse.getElementsByAttributeValue(
                "class", "plot_summary ").get(0);
        var description = generalInformation.select("div.summary_text").text();

        return new Film(id, originalTitle, link, description, timing, fullReleaseDate);
    }
}