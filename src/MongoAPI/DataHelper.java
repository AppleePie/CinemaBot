package MongoAPI;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    private final MongoClient client = new MongoClient();
    private MongoCollection<Document> filmCollection;

    public void initDB(String collectionName) {
        var database = client.getDatabase("myDB");
        filmCollection = database.getCollection(collectionName);
    }

    public List<Film> ParseWebSite(String url) throws IOException {
        var classParse = new Parser();
        var document = classParse.GetDocumentForParse(url);
        return classParse.Parse(document);
    }

    public void insert(Film film) throws IllegalAccessException {
        filmCollection.insertOne(film.toDocument());
    }

    public List<Film> getAllData() throws IllegalAccessException {
        var films = new ArrayList<Film>();
        var documents = filmCollection.find();
        for (var document: documents) {
            films.add(Film.fromDocument(document));
        }

        return films;
    }
}