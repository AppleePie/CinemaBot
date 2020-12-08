package models;

import javax.persistence.*;

@Entity(name = "Genre")
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private Integer genreId;
    @Column(name = "genre")
    private String genre;

    public Genre() { }

    public Genre(String value) {
        genre = value;
    }

    public Integer getId() { return genreId; }
    public void setId(Integer id) { this.genreId = id; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }
}
