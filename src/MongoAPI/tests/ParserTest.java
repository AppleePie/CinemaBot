package MongoAPI.tests;

import MongoAPI.Film;
import MongoAPI.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParserTest {
    private static final Parser classParse = new Parser();

    @Test(expected = IllegalArgumentException.class)
    public void GetDocumentForParse_EmptyLink_Test() throws Exception {
        var link = "";
        classParse.GetDocumentForParse(link);
    }

    @Test(expected = IOException.class)
    public void GetDocumentForParse_IncorrectLink_Test() throws Exception {
        var link = "https://www.imdb.comm";
        classParse.GetDocumentForParse(link);
    }

    @Test
    public void GetDocumentForParse_CorrectLink_Test() throws Exception {
        var link = "http://urgu.org/";
        assertNotNull(classParse.GetDocumentForParse(link));
    }

    @Test
    public void Parse_EmptyDocument_Test() throws Exception {
        var document = new Document(null);
        var dataList = classParse.Parse(document);
        var answer = new ArrayList<Film>();
        assertEquals(dataList, answer);
    }

    @Test
    public void Parse_IncorrectDocument_Test() throws Exception {
        var path = Paths.get("website imdb for test/site-small-incorrect.html");
        var text = Files.readString(path);
        var document = Jsoup.parse(text);
        var dataList = classParse.Parse(document);
        var answer = new ArrayList<Film>();
        assertEquals(dataList, answer);
    }

    @Test
    public void Parse_CorrectDocument_Test() throws Exception {
        var path = Paths.get("website imdb for test/site-small-correct.html");
        var text = Files.readString(path);
        var document = Jsoup.parse(text);
        var dataList = classParse.Parse(document);
        var answer = new ArrayList<Film>();
        answer.add(new Film("www.imdb.comhttps://www.imdb.com/" +
                "title/tt0111161/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=e31d89dd" +
                "-322d-4646-8962-327b42fe94b1&pf_rd_r=T3F8ZTZSF4PGG0R5YWPS&pf" +
                "_rd_s=center-1&pf_rd_t=15506&pf_rd_i=top&ref_=chttp_tt_1",
                "Побег из Шоушенка"));
        assertEquals(dataList, answer);
    }
}