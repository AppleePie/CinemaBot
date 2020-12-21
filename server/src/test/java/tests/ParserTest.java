package tests;

import models.Genre;
import org.junit.Before;
import server.DocumentManager;
import server.Parser;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты к классу Parser.
 * Тестирование производится на локальных копиях
 * страниц с топом фильмов и страницах фильмов;
 *
 * Краткая справка по локальным копиям страниц:
 * film-page-correct.html - страница с фильмом "Побег из Шоушенка"
 * с сохранением всей изначальной информации.
 * film-page-incorrect.html - страница, где были искусственно вырезаны
 * данные обо всех характеристиках фильма(оригинальном названии,
 * продолжительности, жанрах и т.д.).
 * top_films_page_correct.html - укороченная до 1 фильма
 * страница с топом фильмов по версии imdb.
 * top_films_page_incorrect.html - страница с топом фильмов по версии imdb,
 * где вырезаны данные обо всех фильмах(== топ из нуля фильмов).
 */
public class ParserTest {
    private static Parser classParse = null;
    private static Document correctPageDocument;
    private static Document incorrectPageDocument;

    static {
        classParse = new Parser();
    }

    @Before
    public void setUp() throws IOException, URISyntaxException {
        var relativeWayToCorrectFile = "/website imdb for test/film-page-correct.html";
        var relativeWayToIncorrectFile = "/website imdb for test/film-page-incorrect.html";

        var pathToCorrectFile = Paths.get(ParserTest.class.getResource(relativeWayToCorrectFile).toURI());
        var pathToIncorrectFile = Paths.get(ParserTest.class.getResource(relativeWayToIncorrectFile).toURI());

        correctPageDocument = DocumentManagerLocalCopy.getDocumentFromPathToFile(pathToCorrectFile);
        incorrectPageDocument = DocumentManagerLocalCopy.getDocumentFromPathToFile(pathToIncorrectFile);
    }

    @Test
    public void getLinksToAllMovie_IncorrectSite() throws URISyntaxException, IOException {
        var relativeWayToTopFilmsPageIncorrect = "/website imdb for test/top_films_page_incorrect.html";
        var pathToFile = Paths.get(ParserTest.class.getResource(relativeWayToTopFilmsPageIncorrect).toURI());
        var document = DocumentManagerLocalCopy.getDocumentFromPathToFile(pathToFile);
        var allLinks = classParse.getLinksToAllMovie(document);
        var answer = new ArrayList<String>();
        assertEquals(answer, allLinks);
    }

    @Test
    public void getLinksToAllMovie_CorrectSite() throws URISyntaxException, IOException {
        var relativeWayToTopFilmsPageIncorrect = "/website imdb for test/top_films_page_correct.html";
        var pathToFile = Paths.get(ParserTest.class.getResource(relativeWayToTopFilmsPageIncorrect).toURI());
        var document = DocumentManagerLocalCopy.getDocumentFromPathToFile(pathToFile);
        var allLinks = classParse.getLinksToAllMovie(document);
        var answer = new ArrayList<String>();
        answer.add("https://www.imdb.com/correct_url");
        assertEquals(answer, allLinks);
    }

    @Test
    public void getOriginalTitle_IncorrectPage(){
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var originalTitle = classParse.getOriginalTitle();
        var answer = "Побег из Шоушенка";
        assertEquals(answer, originalTitle);
    }

    @Test
    public void getOriginalTitle_CorrectPage(){
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var originalTitle = classParse.getOriginalTitle();
        var answer = "The Shawshank Redemption";
        assertEquals(answer, originalTitle);
    }

    @Test
    public void getFilmTiming_IncorrectPage(){
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var timing = classParse.getFilmTiming();
        var answer = "";
        assertEquals(answer, timing);
    }

    @Test
    public void getFilmTiming_CorrectPage(){
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var timing = classParse.getFilmTiming();
        var answer = "2h 22min";
        assertEquals(answer, timing);
    }

    @Test
    public void getGenreAndFullReleaseDate_IncorrectPage(){
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate().get(0).text();
        var answer = " ";
        assertEquals(answer, genreAndFullReleaseDate);
    }

    @Test
    public void getGenreAndFullReleaseDate_CorrectPage(){
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate();
        var data = new ArrayList<String>();
        genreAndFullReleaseDate.forEach(x -> data.add(x.text()));
        var answer = new ArrayList<>(Arrays.asList("Drama", "14 October 1994 (USA) "));
        assertEquals(answer, data);
    }

    @Test
    public void getGenres_IncorrectPage() {
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate();
        var genres = classParse.getGenres(genreAndFullReleaseDate);
        var answer = new ArrayList<Genre>();
        assertEquals(answer, genres);
    }

    @Test
    public void getGenres_CorrectPage() {
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate();
        var genres = classParse.getGenres(genreAndFullReleaseDate);
        var answer = new ArrayList<>(Collections.singletonList(new Genre("Drama")));
        assertEquals(answer, genres);
    }

    @Test
    public void getFullReleaseDate_IncorrectPage() {
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate();
        var fullReleaseDate = classParse.getFullReleaseDate(genreAndFullReleaseDate);
        var answer = " ";
        assertEquals(answer, fullReleaseDate);
    }

    @Test
    public void getFullReleaseDate_CorrectPage() {
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var genreAndFullReleaseDate = classParse.getGenreAndFullReleaseDate();
        var fullReleaseDate = classParse.getFullReleaseDate(genreAndFullReleaseDate);
        var answer = "14 October 1994 (USA) ";
        assertEquals(answer, fullReleaseDate);
    }

    @Test
    public void getDescription_IncorrectPage() {
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var description = classParse.getDescription();
        var answer = "";
        assertEquals(answer, description);
    }

    @Test
    public void getDescription_CorrectPage() {
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var description = classParse.getDescription();
        var answer = "Two imprisoned men bond over a number of years, " +
                "finding solace and eventual redemption through acts of common decency.";
        assertEquals(answer, description);
    }

    @Test
    public void getPosterUrl_IncorrectPage() throws IOException {
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var posterUrl = classParse.getPosterUrl();
        var answer = "";
        assertEquals(answer, posterUrl);
    }

    @Test
    public void getPosterUrl_CorrectPage() throws IOException {
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var posterUrl = classParse.getPosterUrl();
        var answer = "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIz" +
                "LWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg";
        assertEquals(answer, posterUrl);
    }

    @Test
    public void getUrlToPageWithHighResolutionPoster_IncorrectPage() {
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster();
        var answer = "https://www.imdb.com";
        assertEquals(answer, urlToPage);
    }

    @Test
    public void getUrlToPageWithHighResolutionPoster_CorrectPage() {
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster();
        var answer = "https://www.imdb.com/title/tt0111161/mediaviewer/rm10105600?ref_=tt_ov_i";
        assertEquals(answer, urlToPage);
    }

    @Test
    public void getUrlToHighResolutionPoster_IncorrectPage() throws IOException {
        classParse.prepareDataForPageOfTheFilm(incorrectPageDocument);
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster();
        var urlToHighResolutionPage = classParse.getUrlToHighResolutionPoster(urlToPage);
        var answer = "";
        assertEquals(answer, urlToHighResolutionPage);
    }

    @Test
    public void getUrlToHighResolutionPoster_CorrectPage() throws IOException {
        classParse.prepareDataForPageOfTheFilm(correctPageDocument);
        var urlToPage = classParse.getUrlToPageWithHighResolutionPoster();
        var urlToHighResolutionPage = classParse.getUrlToHighResolutionPoster(urlToPage);
        var answer = "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIz" +
                "LWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg";
        assertEquals(answer, urlToHighResolutionPage);
    }
}