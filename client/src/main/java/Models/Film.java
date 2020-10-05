package Models;

import org.bson.Document;

public class Film {
    public String Title;
    public String Url;

    private Film() { }

    public Film(String title, String url) {
        Title = title;
        Url = url;
    }

    //for MongoDB
    public static Film fromDocument(Document document) throws IllegalAccessException {
        var film = new Film();
        for (var field: film.getClass().getFields()) {
            var val = document.get(field.getName().toLowerCase());
            field.set(film, val);
        }
        return film;
    }

    @Override
    public String toString() { return String.format("Title: \"%s\"\nURL: '%s'\n", Title, Url); }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final Film other = (Film) obj;
        return this.Title.equals(other.Title) && this.Url.equals(other.Url);
    }
}
