package com.company;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        var classParse = new Parser();
        var url = "https://www.imdb.com/chart/top/";
        var document = classParse.GetDocumentForParse(url);
        var dataList = classParse.Parse(document);
        dataList.forEach(System.out::println);
    }
}
