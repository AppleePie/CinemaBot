package server;

import models.Film;
import models.Genre;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public Parser() throws IOException {
        ConfigHelper.initValues();
    }

    /**
     * Функция получает html-представление сайта по ссылке
     * @param link - ссылка на сайт
     * @return html-предсатвление сайта
     * @throws IOException - может возникнуть при попытке считывания сайта
     */
    public Document getDocumentForParse(String link) throws IOException {
        return Jsoup.connect(link).get();
    }

    /**
     * Функция парсит страницу с топом-250 фильмов по версии imdb
     * @param document - html-представление всей страницы
     * @return лист экземпляров класса Film
     */
    public List<Film> parse(Document document) {
        var films = new ArrayList<Film>();
        var elements = document.select(ConfigHelper.MOVIE_TAG_IN_THE_TOP);
        for (var id = 0; id < elements.size(); id++) {
            var tdElement = elements.get(id).child(0);
            var url = ConfigHelper.WEBSITE;
            url += tdElement.attr(ConfigHelper.LINK_ATTR);
            try {
                var film = parseFilmPage(id, url);
                films.add(film);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return films;
    }

    /**
     * Функция парсит страницу фильма для получения основной информации о фильме
     * и дальнейшего создание экземлпяра класса Film в конструкторе
     * @param id - id фильма в базе данных
     * @param link - ссылка на страницу фильма на платформе imdb
     * @return новый экземляр класса Film
     * @throws IOException - может возникнуть при попытке считать html представление страницы
     */
    public Film parseFilmPage(int id, String link) throws IOException {
        var documentForParse = getDocumentForParse(link);

        var mainInformation = getMainInformation(documentForParse);

        var originalTitle = getOriginalTitle(mainInformation);
        var poster = getPosterUrl(documentForParse);
        var subtextElements = getSubtextElements(mainInformation);
        var timing = getFilmTiming(subtextElements);

        var genreAndFullReleaseDate = getGenreAndFullReleaseDate(subtextElements);

        var genres = getGenres(genreAndFullReleaseDate);
        var fullReleaseDate = getFullReleaseDate(genreAndFullReleaseDate);

        var generalInformation= getGeneralInformation(documentForParse);
        var description = getDescription(generalInformation);

        var newFilm = createNewFilm(id, originalTitle, link, poster, description, timing, fullReleaseDate, genres);

        return newFilm;
    }

    /**
     * Функция для получения области, выделенной тэгом- оболочкой
     * @param documentForParse - html-представление всей страницы
     * @return возвращает область с основной информацией о фильме
     */
    public Element getMainInformation(Document documentForParse){
        return documentForParse.select(ConfigHelper.TAG_FOR_BASIC_INFORMATION_ABOUT_THE_MOVIE).get(0);
    }

    /**
     * Функция для получения значения тэга с названием фильма
     * @param mainInformation - область с основной информацией о фильме
     * @return возвращает оригинальное название фильма
     */
    public String getOriginalTitle(Element mainInformation){
        var originalTitle = mainInformation.select(ConfigHelper.TAG_OF_THE_ORIGINAL_MOVIE_TITLE).text();
        if (originalTitle.equals("")) {
            originalTitle = mainInformation.select(ConfigHelper.TITLE_TAG).text();
            originalTitle = originalTitle.substring(0, originalTitle.length() - 7);
        } else
            originalTitle = originalTitle.substring(0, originalTitle.length() - 17);
        return originalTitle;
    }

    /**
     * Функция для получения области, выделенной тэгом- оболочкой
     * @param mainInformation - область с основной информацией о фильме
     * @return возвращает блок с данным об возрастном рейтинге, хронометраже и пр.
     */
    public Elements getSubtextElements(Element mainInformation){
        return mainInformation.select(ConfigHelper.SUBTEXT_TAG).get(0).children();
    }

    /**
     * Функция для выборки по элементу time из всей области
     * @param subtextElements - область с основными характеристиками фильма
     * @return возвращает хронометраж фильма
     */
    public String getFilmTiming(Elements subtextElements){
        return subtextElements.select(ConfigHelper.TIME_TAG).text();
    }

    /**
     * Функция для выделения информации по тэгу ссылок
     * @param subtextElements - область с основными характеристиками фильма
     * @return возвращает жанры и полную дату релиза
     */
    public List<TextNode> getGenreAndFullReleaseDate(Elements subtextElements){
        return subtextElements.select(ConfigHelper.LINK_TAG).textNodes();
    }

    /**
     * Функция для получения всех жанров
     * @param genreAndFullReleaseDate - лист с жанрами и полной датой релиза
     * @return возвращает все жанры фильма
     */
    public List<Genre> getGenres(List<TextNode> genreAndFullReleaseDate){
        var genres = new ArrayList<Genre>();
        for (var i = 0; i < genreAndFullReleaseDate.size() - 1; i++)
            genres.add(new Genre(genreAndFullReleaseDate.get(i).toString()));
        return genres;
    }

    /**
     * Функция для получения полной даты релиза
     * @param genreAndFullReleaseDate - лист с жанрами и полной датой релиза
     * @return возвращает полную дату релиза
     */
    public String getFullReleaseDate(List<TextNode> genreAndFullReleaseDate){
        return genreAndFullReleaseDate.get(genreAndFullReleaseDate.size() - 1).toString();
    }

    /**
     * Функция для получения подробной информации о фильме
     * @param documentForParse - html-представление всей страницы
     * @return возвращает область с подробной информацией о фильме: описание, актёры, режиссёры и т.д.
     */
    public Element getGeneralInformation(Document documentForParse){
        return documentForParse.select(ConfigHelper.TAG_FOR_DETAILED_INFORMATION_ABOUT_THE_MOVIE).get(0);
    }

    /**
     * Функция для получения значения тэга с названием описанием фильма
     * @param generalInformation - подробная информация о фильме
     * @return возвращает описание фильма
     */
    public String getDescription(Element generalInformation){
        return generalInformation.select(ConfigHelper.DESCRIPTION_TAG).text();
    }

    /**
     * Функция для получения ссылки на постер фильма
     * @param documentForParse - html-представление всей страницы
     * @return ссылку на постер фильма
     * @throws IOException - может возникнуть при попытке считать html представление страницы
     */
    public String getPosterUrl(Document documentForParse) throws IOException {
        var urlToPageWithHighResolutionPoster = getUrlToPageWithHighResolutionPoster(documentForParse);
        var posterUrl = getUrlToHighResolutionPoster(urlToPageWithHighResolutionPoster);
        return posterUrl;
    }

    /**
     * Функция получает ссылку на страницу с постером фильма в высоком разрешении
     * @param documentForParse - html-представление всей страницы
     * @return возвращает ссылку на страницу с постером
     */
    public String getUrlToPageWithHighResolutionPoster(Document documentForParse){
        var urlToPageWithPoster = ConfigHelper.WEBSITE;
        urlToPageWithPoster += documentForParse
                .select(ConfigHelper.POSTER_TAG)
                .select(ConfigHelper.LINK_TAG)
                .attr(ConfigHelper.LINK_ATTR);
        return urlToPageWithPoster;
    }

    /**
     * Функция получает ссылку на постер
     * @param urlToPageWithHighResolutionPoster - ссылка на страницу с постером
     * @return возвращает ссылку на постер
     * @throws IOException - может возникнуть при попытке считать html представление страницы
     */
    public String getUrlToHighResolutionPoster(String urlToPageWithHighResolutionPoster) throws IOException {
        var pageWithPoster = getDocumentForParse(urlToPageWithHighResolutionPoster);
        var partPageWithPoster = pageWithPoster.select(ConfigHelper.MULTIMEDIA_TAG);
        var dataWithImgTag = partPageWithPoster.select(ConfigHelper.IMAGE_TAG);
        var posterUrl = dataWithImgTag.attr(ConfigHelper.TAG_THE_ADDRESS_OF_THE_FILE);
        return posterUrl;
    }
    /**
     * Функция для создания нового фильма
     * @param id - id фильма
     * @param originalTitle - оригинальное название фильма
     * @param link - ссылка на страницу фильма на платформе imdb
     * @param description - описание фильма
     * @param timing - хронометраж фильма
     * @param fullReleaseDate - полная дата релиза фильма
     * @param genres
     * @return возвращает новый экземлпяр класса Film
     */
    public Film createNewFilm(
            int id,
            String originalTitle,
            String link,
            String poster,
            String description,
            String timing,
            String fullReleaseDate,
            List<Genre> genres) {
        var newFilm = new Film(id, originalTitle, link, poster, description, timing, fullReleaseDate, genres);
        return newFilm;
    }
}