package server;

import models.Film;
import models.Genre;

import java.io.IOException;
import java.util.List;

public class ImdbInformationManager {
    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    public void updateDataBase() throws IOException {
        final List<Film> films = parseWebSite(ConfigHelper.SOURCE_URL);
        databaseHelper.insertMany(films);
    }

    public String getFilmAsJson(Genre inputGenre) {
        final Film filmForInputGenre = databaseHelper.readFilmWithGenre(inputGenre);
        return filmForInputGenre != null
                ? filmForInputGenre.toString()
                : databaseHelper.readRandomFilm().toString();
    }

    private List<Film> parseWebSite(String url) throws IOException {
        var classParse = new Parser();
        var document = classParse.getDocumentForParse(url);
        return classParse.parse(document);
    }
}
