package MongoAPI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var dataHelper = new DataHelper();
        dataHelper.ParseWebSite("https://www.imdb.com/chart/top/").forEach(System.out::println);
    }
}
