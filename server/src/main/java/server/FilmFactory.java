package server;

import models.Film;
import models.Genre;

import java.util.List;

public class FilmFactory {
    public static Film createNewFilm(
            int id, String originalTitle, String link, String poster,
            String description, String timing, String fullReleaseDate, List<Genre> genres) {
        return new Film(id, originalTitle, link, poster, description, timing, fullReleaseDate, genres);
    }
}
