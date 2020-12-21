package server;

import models.Film;
import models.Genre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImdbInformationManager {
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    public static void updateDataBase() throws IOException {
        final List<Film> films = parseImdbTopFilms();
        databaseHelper.insertMany(films);
    }

    public static String findRandomFilm() {
        return databaseHelper.readRandomFilm().toString();
    }

    public static ArrayList<String> getAllAvailableGenresAsStrings() {
        return databaseHelper.getAllAvailableGenresAsStrings();
    }

    public static ArrayList<String> getAllAvailableReleaseYearsAsStrings() {
        return databaseHelper.getAllAvailableReleaseYearsAsStrings();
    }

    public static String findFilmWithGenre(String userInput) {
        return databaseHelper.readFilmWithGenre(new Genre(userInput)).toString();
    }

    public static String findFilmsWithYear(String userInput) {
        return databaseHelper.readFilmWithYear(userInput)
                .stream()
                .map(Film::toString)
                .collect(Collectors.joining(";"));
    }

    private static List<Film> parseImdbTopFilms() throws IOException {
        var filmsOnImdbTop = new ArrayList<Film>();
        var parser = new Parser();

        var imdbUrlToTopFilms = ConfigHelper.SOURCE_URL;
        var document = DocumentManager.getDocumentFromLinkToSite(imdbUrlToTopFilms);
        var linksToAllMovieInTopFilmsPage = parser.getLinksToAllMovie(document);
        for (int index = 0; index < linksToAllMovieInTopFilmsPage.size(); index++) {
            var link = linksToAllMovieInTopFilmsPage.get(index);
            var film = parseFilmPage(index, link, parser);
            filmsOnImdbTop.add(film);
        }
        return filmsOnImdbTop;
    }

    private static Film parseFilmPage(int id, String link, Parser parser) throws IOException {
        var document = DocumentManager.getDocumentFromLinkToSite(link);
        parser.prepareDataForPageOfTheFilm(document);
        var originalTitle = parser.getOriginalTitle();
        var poster = parser.getPosterUrl();
        var timing = parser.getFilmTiming();
        var genreAndFullReleaseDate = parser.getGenreAndFullReleaseDate();
        var genres = parser.getGenres(genreAndFullReleaseDate);
        var fullReleaseDate = parser.getFullReleaseDate(genreAndFullReleaseDate);
        var description = parser.getDescription();
        return FilmFactory.createNewFilm(id, originalTitle, link, poster, description, timing, fullReleaseDate, genres);
    }
}
