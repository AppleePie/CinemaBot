package tests;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentManagerLocalCopy {
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
