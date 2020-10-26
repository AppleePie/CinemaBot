package Models;

import org.bson.Document;

public class Film {
    private String title;
    public String getTitle() { return title; }

    private String url;
    public String getUrl() {  return url; }

    public Film(String title, String url) {
        this.title = title;
        this.url = url;
    }

    private Film() {}

    public static Film fromDocument(Document document) throws IllegalAccessException {
        var film = new Film();
        for (var field: film.getClass().getFields()) {
            var val = document.get(field.getName().toLowerCase());
            field.set(film, val);
        }
        return film;
    }

    @Override
    public String toString() { return String.format("{ \"title\": \"%s\", \"url\": \"%s\"}", title, url); }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final Film other = (Film) obj;
        return this.title.equals(other.title) && this.url.equals(other.url);
    }

    @Override
    public int hashCode() {
        var result = 42;
        result = 42 * result + title.hashCode();
        result = 42 * result + url.hashCode();
        return result;
    }
}
