package bot;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final RequestHandler requestHandler = new RequestHandler();
        System.out.println(requestHandler.getFilm());
    }
}