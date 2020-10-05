package MongoAPI;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataHelper {
    private MongoCollection<Document> filmCollection;
    private final Properties dataHelperProperties = new Properties();

    public void initDB(String collectionName) throws IOException {
        initDataHelperProperties();

        final MongoClient client = new MongoClient(new MongoClientURI(dataHelperProperties.getProperty("DATABASE")));

        //TODO
        var flag = true;
        var database = client.getDatabase("myDB");
            database.getCollection(collectionName);
            database.getCollection(collectionName).drop();
        filmCollection = database.getCollection(collectionName);

        var doc = new Document();
            doc.put("title", "test");
            doc.put("url", "test");
        filmCollection.insertOne(doc);

        var url = dataHelperProperties.getProperty("WEBSITE_URL");
        if (flag) {
            filmCollection.insertMany(parseWebSite(url));
        }
    }

    private void initDataHelperProperties() throws IOException {
        final var rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        final String serverConfigPath = rootPath + "DB.properties";
        dataHelperProperties.load(new FileInputStream(serverConfigPath));
    }

    private List<Document> parseWebSite(String url) throws IOException {
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