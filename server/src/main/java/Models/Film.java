package Models;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Film() {
    }

    public Film(Integer id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("{ \"title\": \"%s\", \"url\": \"%s\"}", title, url);
    }

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
