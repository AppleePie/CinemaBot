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

    //For Mongo...
    public Document toDocument() throws IllegalAccessException {
        var document = new Document();
        for (var field : this.getClass().getFields()) {
            document.put(field.getName(), field.get(this));
        }
        return document;
    }

    public static Film fromDocument(Document document) throws IllegalAccessException {
        var film = new Film();
        for (var field: film.getClass().getFields()) {
            field.set(film, document.get(field.getName()));
        }
        return film;
    }

    @Override
    public String toString() {
        return String.format("Название: \"%s\"\nСсылка: '%s'\n", Title, Url);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final Film other = (Film) obj;
        return this.Title.equals(other.Title) && this.Url.equals(other.Url);
    }
}
