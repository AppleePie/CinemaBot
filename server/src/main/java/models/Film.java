package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;

@Entity
@Table(name = "films")
@Transactional
public class Film {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;
    @Column(name = "poster")
    private String poster;
    @Column(name = "description")
    private String description;
    @Column(name = "timing")
    private String timing;
    @Column(name = "fullReleaseDate")
    private String fullReleaseDate;

    public Film() {
    }

    public Film(Integer id, String title, String url,
                String poster, String description,
                String timing, String fullReleaseDate) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.poster = poster;
        this.description = description;
        this.timing = timing;
        this.fullReleaseDate = fullReleaseDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTiming() {
        return timing;
    }
    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getFullReleaseDate() {
        return fullReleaseDate;
    }
    public void setFullReleaseDate(String fullReleaseDate) {
        this.fullReleaseDate = fullReleaseDate;
    }

    @Override
    public String toString() {
        return String.format(
                "{" +
                        "\n \"title\": \"%s\"," +
                        "\n \"url\": \"%s\"," +
                        "\n \"poster\": \"%s\"," +
                        "\n \"description\": \"%s\"," +
                        "\n \"timing\": \"%s\", " +
                        "\n \"fullReleaseDate\": \"%s\"\n" +
                        "}",
                title, url, poster, description, timing, fullReleaseDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final Film other = (Film) obj;
        return this.title.equals(other.title) && this.url.equals(other.url)
                && this.poster.equals(other.poster)
                && this.description.equals(other.description)
                && this.timing.equals(other.timing)
                && this.fullReleaseDate.equals(other.fullReleaseDate);
    }

    @Override
    public int hashCode() {
        var result = 42;
        result = 42 * result + title.hashCode();
        result = 42 * result + url.hashCode();
        result = 42 * result + poster.hashCode();
        result = 42 * result + description.hashCode();
        result = 42 * result + timing.hashCode();
        result = 42 * result + fullReleaseDate.hashCode();
        return result;
    }
}
