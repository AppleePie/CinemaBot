package Server;

import org.bson.Document;

import java.sql.*;
import java.util.List;
import java.util.Random;

public class DataHelper {
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String USER = "postgres";
    private static final String PASS = "";

    PreparedStatement state = null;
    Connection connection = null;

    public void initDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement state = connection.createStatement();

        state.execute("DROP DATABASE IF EXISTS films");
        state.execute("CREATE DATABASE films");

        connection = DriverManager.getConnection(DB_URL + "films", USER, PASS);

        PreparedStatement ps =  connection.prepareStatement("CREATE TABLE Films(id int primary key, title varchar, url text)");
        ps.executeUpdate();
    }
    

    public void insertMany(List<Document> films) throws SQLException {
        for (int index = 1; index < films.size() + 1; index++) {
            insert(index, films.get(index - 1));
        }
    }

    private void insert(int id, Document inputData) throws SQLException {
        state = connection.prepareStatement("INSERT INTO Films(id, title, url) VALUES (?, ?, ?)");

        state.setInt(1, id);
        state.setString(2, inputData.getString("title"));
        state.setString(3, inputData.getString("url"));

        state.executeUpdate();
    }

    public Document readRandomFilm() throws SQLException {
        final int randomId = new Random().nextInt(250);

        state = connection.prepareStatement("SELECT * FROM Films WHERE id = ?");
        state.setInt(1, randomId);
        final ResultSet randomFilm = state.executeQuery();

        final Document filmDoc = new Document();
        if (randomFilm.next()) {
            filmDoc.append("title", randomFilm.getString(2));
            filmDoc.append("url", randomFilm.getString(3));
        }

        return filmDoc;
    }
}