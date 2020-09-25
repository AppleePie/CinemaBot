package com.company;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Parser{
    public Document GetDocumentForParse(String link) throws IOException {
        return Jsoup.connect(link).get();
    }

    public ArrayList<Data> Parse(@NotNull Document document){
        var dataList = new ArrayList<Data>();
        var elements = document.getElementsByAttributeValue("class", "titleColumn");

        elements.forEach(element -> {
            var tdElement = element.child(0);
            var url = "www.imdb.com";
            url += tdElement.attr("href");
            var text = tdElement.text();
            dataList.add(new Data(url, text));
        });
        return dataList;
    }
}