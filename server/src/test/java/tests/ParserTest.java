package tests;

import models.Film;
import org.junit.Before;
import server.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParserTest {
    private static Parser classParse = null;
    private static Document correctPageDocument;
    private static Document incorrectPageDocument;

    static {
        try {
            classParse = new Parser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws IOException, URISyntaxException {
        var relativeWayToCorrectFile = "/website imdb for test/film-page-correct.html";
        var relativeWayToIncorrectFile = "/website imdb for test/film-page-incorrect.html";

        correctPageDocument = getDocumentFromRelativeWayToFile(relativeWayToCorrectFile);
        incorrectPageDocument = getDocumentFromRelativeWayToFile(relativeWayToIncorrectFile);
    }

    public Document getDocumentFromRelativeWayToFile(String relativeWayToFile) throws IOException, URISyntaxException {
        var pathToFile = Paths.get(ParserTest.class.getResource(relativeWayToFile).toURI());
        var readedFile = Files.readString(pathToFile);
        return Jsoup.parse(readedFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDocumentForParse_EmptyLink() throws Exception {
        var link = "";
        classParse.getDocumentForParse(link);
    }

    @Test(expected = IOException.class)
    public void getDocumentForParse_IncorrectLink() throws Exception {
        var link = "https://www.imdb.comm";
        classParse.getDocumentForParse(link);
    }

    @Test
    public void getDocumentForParse_CorrectLink() throws Exception {
        var link = "http://urgu.org/";
        assertNotNull(classParse.getDocumentForParse(link));
    }

    @Test
    public void parse_EmptyDocument() {
        var document = new Document(null);
        var dataList = classParse.parse(document);
        var answer = new ArrayList<Film>();
        assertEquals(dataList, answer);
    }

    @Test
    public void parse_IncorrectPage() throws IOException, URISyntaxException {
        var relativeWayToFile = "/website imdb for test/site-small-incorrect.html";
        var document = getDocumentFromRelativeWayToFile(relativeWayToFile);

        var dataList = classParse.parse(document);
        var answer = new ArrayList<Film>();
        assertEquals(dataList, answer);
    }

    @Test
    public void parse_CorrectPage() throws IOException, URISyntaxException {
        var relativeWayToFile = "/website imdb for test/site-small-correct.html";
        var document = getDocumentFromRelativeWayToFile(relativeWayToFile);

        var dataList = classParse.parse(document);
        var answer = new Film(
                0, "The Shawshank Redemption",
                "https://www.imdb.com/title/tt0111161/?" +
                        "pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=e31d89dd" +
                        "-322d-4646-8962-327b42fe94b1&pf_rd_r=T3" +
                        "F8ZTZSF4PGG0R5YWPS&pf_rd_s=center-1&pf_" +
                        "rd_t=15506&pf_rd_i=top&ref_=chttp_tt_1",
                "https://m.media-amazon.com/images/M/" +
                        "MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM" +
                        "1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg",
                "Two imprisoned men bond over a number" +
                        " of years, finding solace and eventual " +
                        "redemption through acts of common decency.",
                "2h 22min", "14 October 1994 (USA) ", null);
        assertEquals(dataList.get(0), answer);
    }

    @Test
    public void getOriginalTitle_WithoutOriginalTitleTag() {
        var mainInformation = classParse.getMainInformation(incorrectPageDocument);

        var originalTitle = classParse.getOriginalTitle(mainInformation);
        var answer = "Побег из Шоушенка";
        assertEquals(originalTitle, answer);
    }

    @Test
    public void getOriginalTitle_WithOriginalTitleTag() {
        var mainInformation = classParse.getMainInformation(correctPageDocument);

        var originalTitle = classParse.getOriginalTitle(mainInformation);
        var answer = "The Shawshank Redemption";
        assertEquals(originalTitle, answer);
    }

    @Test
    public void getUrlToPageWithHighResolutionPoster_IncorrectPage() {
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster(incorrectPageDocument);
        var answer = "https://www.imdb.com";
        assertEquals(urlToPage, answer);
    }

    @Test
    public void getUrlToPageWithHighResolutionPoster_CorrectPage() {
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster(correctPageDocument);
        var answer = "https://www.imdb.com/title/tt0111161/mediaviewer/rm10105600?ref_=tt_ov_i";
        assertEquals(urlToPage, answer);
    }

    @Test
    public void getUrlToHighResolutionPoster_IncorrectPage() throws IOException {
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster(incorrectPageDocument);
        var urlToHighResolutionPage = classParse.getUrlToHighResolutionPoster(urlToPage);
        var answer = "";
        assertEquals(urlToHighResolutionPage, answer);
    }

    @Test
    public void getUrlToHighResolutionPoster_CorrectPage() throws IOException {
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster(correctPageDocument);
        var urlToHighResolutionPage = classParse.getUrlToHighResolutionPoster(urlToPage);
        var answer = "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIz" +
                "LWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg";
        assertEquals(urlToHighResolutionPage, answer);
    }

    @Test
    public void getPosterUrl_IncorrectPage() throws IOException {
        var posterUrl = classParse.getPosterUrl(incorrectPageDocument);
        var answer = "";
        assertEquals(posterUrl, answer);
    }

    @Test
    public void getPosterUrl_CorrectPage() throws IOException {
        var posterUrl = classParse.getPosterUrl(correctPageDocument);
        var answer = "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIz" +
                "LWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg";
        assertEquals(posterUrl, answer);
    }

    @Test
    public void getFilmTiming_IncorrectPage() {
        var mainInformation = classParse.getMainInformation(incorrectPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var timing = classParse.getFilmTiming(subtextElements);
        var answer = "";
        assertEquals(timing, answer);
    }

    @Test
    public void getFilmTiming_CorrectPage() {
        var mainInformation = classParse.getMainInformation(correctPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var timing = classParse.getFilmTiming(subtextElements);
        var answer = "2h 22min";
        assertEquals(timing, answer);
    }

    @Test
    public void getGenreAndFullReleaseDate_IncorrectPage() {
        var mainInformation = classParse.getMainInformation(incorrectPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate(subtextElements).get(0).text();
        var answer = " ";
        assertEquals(genreAndFullReleaseDate, answer);
    }

    @Test
    public void getGenreAndFullReleaseDate_CorrectPage() {
        var mainInformation = classParse.getMainInformation(correctPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate(subtextElements);
        var data = new ArrayList<String>();
        genreAndFullReleaseDate.forEach(x -> data.add(x.text()));
        var answer = new ArrayList<>(Arrays.asList("Drama", "14 October 1994 (USA) "));
        assertEquals(data, answer);
    }

    @Test
    public void getGenres_IncorrectPage() {
        var mainInformation = classParse.getMainInformation(incorrectPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate(subtextElements);

        var genres = classParse.getGenres(genreAndFullReleaseDate);
        var answer = new ArrayList<String>();
        assertEquals(genres, answer);
    }

    @Test
    public void getGenres_CorrectPage() {
        var mainInformation = classParse.getMainInformation(correctPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate(subtextElements);

        var genres = classParse.getGenres(genreAndFullReleaseDate);
        var answer = new ArrayList<>(Collections.singletonList("Drama"));
        assertEquals(genres, answer);
    }

    @Test
    public void getFullReleaseDate_IncorrectPage() {
        var mainInformation = classParse.getMainInformation(incorrectPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate(subtextElements);

        var fullReleaseDate = classParse.getFullReleaseDate(genreAndFullReleaseDate);
        var answer = " ";
        assertEquals(fullReleaseDate, answer);
    }

    @Test
    public void getFullReleaseDate_CorrectPage() {
        var mainInformation = classParse.getMainInformation(correctPageDocument);
        var subtextElements = classParse.getSubtextElements(mainInformation);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate(subtextElements);

        var fullReleaseDate = classParse.getFullReleaseDate(genreAndFullReleaseDate);
        var answer = "14 October 1994 (USA) ";
        assertEquals(fullReleaseDate, answer);
    }

    @Test
    public void getDescription_IncorrectPage() {
        var generalInformation= classParse.getGeneralInformation(incorrectPageDocument);
        var description = classParse.getDescription(generalInformation);
        var answer = "";
        assertEquals(description, answer);
    }

    @Test
    public void getDescription_CorrectPage() {
        var generalInformation= classParse.getGeneralInformation(correctPageDocument);
        var description = classParse.getDescription(generalInformation);
        var answer = "Two imprisoned men bond over a number of years, " +
                "finding solace and eventual redemption through acts of common decency.";
        assertEquals(description, answer);
    }
}