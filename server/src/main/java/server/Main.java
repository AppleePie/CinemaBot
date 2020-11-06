package server;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        final Server server = new Server();
        server.startServer();
    }
}