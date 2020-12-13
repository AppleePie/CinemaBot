package server;

import models.Genre;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Document documentForParseFilmPage;
    private Element mainInformationFilmPage;
    private Elements subtextElementsFilmPage;


    public Parser() throws IOException {
        ConfigHelper.initValues();
    }

    public List<String> getLinksToAllMovie(Document document){
        var linksToAllMovie = new ArrayList<String>();
        var elementsWithMovieTag = document.select(ConfigHelper.MOVIE_TAG_IN_THE_TOP);
        for (Element element : elementsWithMovieTag) {
            var tdElement = element.child(0);
            var url = ConfigHelper.WEBSITE;
            url += tdElement.attr(ConfigHelper.LINK_ATTR);
            linksToAllMovie.add(url);
        }
        return linksToAllMovie;
    }

    public void prepareDataForPageOfTheFilm(Document document){
        documentForParseFilmPage = document;
        mainInformationFilmPage = getMainInformation();
        subtextElementsFilmPage = getSubtextElements();
    }

    /**
     * Функция для получения области, выделенной тэгом- оболочкой
     * @return возвращает область с основной информацией о фильме
     */
    public Element getMainInformation(){
        return documentForParseFilmPage.select(ConfigHelper.TAG_FOR_BASIC_INFORMATION_ABOUT_THE_MOVIE).get(0);
    }

    /**
     * Функция для получения значения тэга с названием фильма
     * @return возвращает оригинальное название фильма
     */
    public String getOriginalTitle(){
        var originalTitle = mainInformationFilmPage.select(ConfigHelper.TAG_OF_THE_ORIGINAL_MOVIE_TITLE).text();
        if (originalTitle.equals("")) {
            originalTitle = mainInformationFilmPage.select(ConfigHelper.TITLE_TAG).text();
            originalTitle = originalTitle.substring(0, originalTitle.length() - 7);
        } else
            originalTitle = originalTitle.substring(0, originalTitle.length() - 17);
        return originalTitle;
    }

    /**
     * Функция для получения области, выделенной тэгом- оболочкой
     * @return возвращает блок с данным об возрастном рейтинге, хронометраже и пр.
     */
    public Elements getSubtextElements(){
        return mainInformationFilmPage.select(ConfigHelper.SUBTEXT_TAG).get(0).children();
    }

    /**
     * Функция для выборки по элементу time из всей области
     * @return возвращает хронометраж фильма
     */
    public String getFilmTiming(){
        return subtextElementsFilmPage.select(ConfigHelper.TIME_TAG).text();
    }

    /**
     * Функция для выделения информации по тэгу ссылок
     * @return возвращает жанры и полную дату релиза
     */
    public List<TextNode> getGenreAndFullReleaseDate(){
        return subtextElementsFilmPage.select(ConfigHelper.LINK_TAG).textNodes();
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
     * @return возвращает область с подробной информацией о фильме: описание, актёры, режиссёры и т.д.
     */
    public Element getGeneralInformation(){
        return documentForParseFilmPage.select(ConfigHelper.TAG_FOR_DETAILED_INFORMATION_ABOUT_THE_MOVIE).get(0);
    }

    /**
     * Функция для получения значения тэга с названием описанием фильма
     * @return возвращает описание фильма
     */
    public String getDescription(){
        var generalInformation= getGeneralInformation();
        return generalInformation.select(ConfigHelper.DESCRIPTION_TAG).text();
    }

    /**
     * Функция для получения ссылки на постер фильма
     * @return ссылку на постер фильма
     * @throws IOException - может возникнуть при попытке считать html представление страницы
     */
    public String getPosterUrl() throws IOException {
        var urlToPageWithHighResolutionPoster = getUrlToPageWithHighResolutionPoster();
        return getUrlToHighResolutionPoster(urlToPageWithHighResolutionPoster);
    }

    /**
     * Функция получает ссылку на страницу с постером фильма в высоком разрешении
     * @return возвращает ссылку на страницу с постером
     */
    public String getUrlToPageWithHighResolutionPoster(){
        var urlToPageWithPoster = ConfigHelper.WEBSITE;
        urlToPageWithPoster += documentForParseFilmPage
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
        var pageWithPoster = DocumentManager.getDocumentFromLinkToSite(urlToPageWithHighResolutionPoster);
        var partPageWithPoster = pageWithPoster.select(ConfigHelper.MULTIMEDIA_TAG);
        var dataWithImgTag = partPageWithPoster.select(ConfigHelper.IMAGE_TAG);
        return dataWithImgTag.attr(ConfigHelper.TAG_THE_ADDRESS_OF_THE_FILE);
    }
}