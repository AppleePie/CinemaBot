package server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentManager {
    /**
     * Функция получает html-представление сайта по ссылке на сайт
     * @param link ссылка на сайт
     * @return html предсатвление сайта
     * @throws IOException может возникнуть при попытке считывания сайта
     */
    public static Document getDocumentFromLinkToSite(String link) throws IOException {
        return Jsoup.connect(link).get();
    }

    /**
     * Функция получает html-представление сайта по ссылке на файл- локальную копию сайта
     * @param pathToFile путь до файла
     * @return html предсатвление сайта
     * @throws IOException может возникнуть при попытке считывания сайта
     */
    public static Document getDocumentFromPathToFile(Path pathToFile) throws IOException {
        var readedFile = Files.readString(pathToFile);
        return Jsoup.parse(readedFile);
    }
}
