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

    public void initDB(String collectionName) throws IOException {
        var flag = true;
        var database = client.getDatabase("myDB");
        //TODO
            database.getCollection(collectionName);
            database.getCollection(collectionName).drop();
        filmCollection = database.getCollection(collectionName);

        var url = "https://www.imdb.com/chart/top/";
        if (flag) {
            filmCollection.insertMany(ParseWebSite(url));
        }
    }

    private List<Document> ParseWebSite(String url) throws IOException {
        var classParse = new Parser();
        var document = classParse.GetDocumentForParse(url);
        return classParse.Parse(document);
    }

    public List<Document> getAllData() {
        var films = new ArrayList<Document>();
        var documents = filmCollection.find();
        for (var document: documents) {
            films.add(document);
        }

        return films;
    }
}