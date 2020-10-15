package Server;

import org.bson.Document;

import java.sql.*;
import java.util.List;
import java.util.Random;

public class DataHelper {
    private PreparedStatement state = null;
    private Connection connection = null;

    public void initDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(ConfigHelper.DB_URL, ConfigHelper.DB_USER, ConfigHelper.DB_PASS);
        Statement state = connection.createStatement();

        state.execute("DROP DATABASE IF EXISTS films");
        state.execute("CREATE DATABASE films");

        connection = DriverManager.getConnection(ConfigHelper.DB_URL + "films", ConfigHelper.DB_USER, ConfigHelper.DB_PASS);

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