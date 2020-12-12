package models;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Genre")
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer genreId;
    @Column(name = "genre")
    private String value;

    public Genre() { }

    public Genre(String value) {
        this.value = value;
    }

    public Integer getId() { return genreId; }
    public void setId(Integer id) { this.genreId = id; }

    public String getValue() { return value; }
    public void setValue(String genre) { this.value = genre; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Genre genre = (Genre) obj;
        return this.value.equals(genre.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, value);
    }
}
