package com.company;

import Models.Film;
import org.apache.http.client.fluent.Request;
import org.bson.Document;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main
{
    public static void main(String[] args) throws IOException, IllegalAccessException {
        var films = getAllFilms();

        System.out.println(getAllFilms().get(new Random().nextInt(films.toArray().length)));
    }

    private static List<Film> getAllFilms() throws IOException, IllegalAccessException {
        var jsonArray = Request.Get("http://localhost:4004/get")
                .execute()
                .returnContent()
                .asString()
                .split("\r\n");

        var films = new ArrayList<Film>();
        for (var json: jsonArray) {
            films.add(Film.fromDocument(Document.parse(json)));
        }
        return films;
    }
}
