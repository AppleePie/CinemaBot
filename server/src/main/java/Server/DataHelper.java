package Server;

import Models.Film;

import java.sql.*;
import java.util.List;
import java.util.Random;

public class DataHelper {
    private PreparedStatement state = null;
    private Connection connection = null;

    public void initDB() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(ConfigHelper.DB_URL, ConfigHelper.DB_USER, ConfigHelper.DB_PASS);
        Statement state = connection.createStatement();

        state.execute("DROP DATABASE IF EXISTS films");
        state.execute("CREATE DATABASE films");

        connection = DriverManager.getConnection(ConfigHelper.DB_URL + "films", ConfigHelper.DB_USER, ConfigHelper.DB_PASS);

        PreparedStatement ps =  connection.prepareStatement("CREATE TABLE Films(id int primary key, title varchar, url text)");
        ps.executeUpdate();
    }

    public void insertMany(List<Film> films) throws SQLException {
        for (int index = 1; index < films.size() + 1; index++) {
            insert(index, films.get(index - 1));
        }
    }

    private void insert(int id, Film film) throws SQLException {
        state = connection.prepareStatement("INSERT INTO Films(id, title, url) VALUES (?, ?, ?)");

        state.setInt(1, id);
        state.setString(2, film.getTitle());
        state.setString(3, film.getUrl());

        state.executeUpdate();
    }

    public Film readRandomFilm() throws SQLException {
        final int randomId = new Random().nextInt(250);

        state = connection.prepareStatement("SELECT * FROM Films WHERE id = ?");
        state.setInt(1, randomId);
        final ResultSet randomFilm = state.executeQuery();

        Film film = null;
        if (randomFilm.next()) {
            final String title = randomFilm.getString(2);
            final String url = randomFilm.getString(3);
            film = new Film(title, url);
        }

        return film;
    }
}