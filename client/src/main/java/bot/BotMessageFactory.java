package bot;

import bot.Message.*;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.ArrayList;

public class BotMessageFactory {
    public Pair<ArrayList<SendPhoto>, ArrayList<SendMessage>> replyToUserMessage(Update update) throws IOException {
        var listPhotoMessages = new ArrayList<SendPhoto>();
        var listMessages = new ArrayList<SendMessage>();

        var userMessage = (update.getMessage().hasText()) ? update.getMessage().getText() : "";
        var userIntention = UserIntentionDeterminer.determineUserIntentions(userMessage);

        switch (userIntention) {
            case GET_RANDOM_FILM -> {
                var messageGetRandomFilmClass = new MessageGetRandomFilm();
                listPhotoMessages.add(messageGetRandomFilmClass.preparePhotoMessage());
            }
            case GET_FILM_BY_GENRES -> {
                var messageGetFilmByGenresClass = new MessageGetFilmByGenres();
                listPhotoMessages.add(messageGetFilmByGenresClass.preparePhotoMessage(userMessage));
            }
            case GET_FILMS_BY_YEAR -> {
                var messageGetFilmsByYearsClass = new MessageGetFilmsByYear();
                listPhotoMessages.addAll(messageGetFilmsByYearsClass.preparePhotoMessage(userMessage));
            }
            case GET_GENRES -> {
                var messageGetGenresClass = new MessageGetGenres();
                listMessages.add(messageGetGenresClass.prepareTextMessage());
            }
            case GET_YEARS -> {
                var messageGetYearsClass = new MessageGetYears();
                listMessages.add(messageGetYearsClass.prepareTextMessage());
            }
            case GET_START -> {
                var messageGetStartClass = new MessageForTelegramCommandStart();
                listMessages.add(messageGetStartClass.prepareTextMessage());
            }
            case GET_HELP -> {
                var messageGetHelpClass = new MessageForTelegramCommandHelp();
                listMessages.add(messageGetHelpClass.prepareTextMessage());
            }
            case INTENTIONS_ARE_NOT_CLEAR -> {
                var messageIncorrectClass = new MessageReactionToIncorrectCommand();
                listMessages.add(messageIncorrectClass.prepareTextMessage());
            }
        }
        return new Pair<ArrayList<SendPhoto>, ArrayList<SendMessage>>(listPhotoMessages, listMessages);
    }
}
