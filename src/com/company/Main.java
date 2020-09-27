package com.company;

import Models.Film;
import org.bson.Document;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main
{
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;


    public static void main(String[] args) throws IOException {
        System.out.println(getFilms().get(new Random().nextInt(250)));
    }

    public static List<Film> getFilms() {
        var films = new ArrayList<Film>();
        try {
            try {
                clientSocket = new Socket("localhost", 4004);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                var serverWord = in.readLine();
                while (!"End".equals(serverWord)) {
                    var doc = Document.parse(serverWord);
                    films.add(new Film((String) doc.get("title"),(String) doc.get("url")));
                    serverWord = in.readLine();
                }
            } finally {
                System.out.println("Клиент был закрыт...");
                System.out.println();
                clientSocket.close();
                in.close();
                out.close();
                return films;
            }
        } catch (IOException e) {
            System.err.println(e);
            return films;
        }
    }
}
