package bot;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.StringJoiner;

public class FilmMessageFactory {
    public static SendPhoto convertBotResponseToMessage(String response){
        var filmData = jsonFilmParser.jsonParseToMap(response);
        var title = filmData.get("title");

        var genresData = filmData
                .get("genres")
                .toString()
                .replaceAll("[\\[\\]]", "")
                .split(",");
        var genresJoiner = new StringJoiner(", ");
        for (String genre: genresData) {
            genre = genre.replaceAll("\"", "").replaceAll("-", "_");
            genresJoiner.add(String.format("#%s", genre));
        }
        var genres = genresJoiner.toString();

        var description = filmData.get("description");
        var timing = filmData.get("timing");
        var fullReleaseDate = filmData.get("fullReleaseDate");
        var linkToIMDB = String.format("<a href=\"%s\">%s on IMDB...</a>",filmData.get("url"), title);

        var poster = new InputFile(filmData.get("poster").toString());
        var captionToPoster = String.format(
                "<b>%s</b>\n\n<i>Genres:</i> %s\n\n%s\n\n<i>Timing:</i> %s\n<i>Release date:</i> %s\n\n%s",
                title, genres, description, timing, fullReleaseDate, linkToIMDB);
        
        var message = new SendPhoto();
        message.setPhoto(poster);
        message.setParseMode(ParseMode.HTML);
        message.setCaption(captionToPoster);
        return message;
    }
}
