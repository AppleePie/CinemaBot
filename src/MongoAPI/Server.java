package MongoAPI;

import org.bson.Document;

import java.io.*;
import java.net.ServerSocket;
import java.util.List;

public class Server {
    private static DataHelper dataHelper;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) throws IOException {
        dataHelper = new DataHelper();
        dataHelper.initDB("films");
        getRequest(dataHelper.getAllData());
    }

    public static void getRequest(List<Document> documents) {
        try {
            try {
                server = new ServerSocket(4004);
                System.out.println("Сервер запущен!");
                try (var clientSocket = server.accept()) {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    for (var document: documents) {
                        var d = document.toJson();
                        out.write(d + "\n");
                    }
                    out.write("End!");
                    out.flush();
                } finally {
                    in.close();
                    out.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
