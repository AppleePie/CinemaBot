package bot.Message;

import bot.FilmMessageFactory;
import bot.RequestHandler;
import bot.TelegramButtonCreator;
import bot.UserIntention;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.util.ArrayList;

public class MessageGetRandomFilm implements IBotBaseMessage, IBotMessagesWhereUnimportantUserMessage {
    public MessageGetRandomFilm() {
        fillSuitableUserMessage();
    }

    @Override
    public void fillSuitableUserMessage() {
        suitableUserMessage.add("Get Random film");
    }

    @Override
    public Pair<String, ArrayList<String>> prepareResponseToMessage() throws IOException {
        var response = RequestHandler.getFilmAsJson("random");
        return new Pair<>(response, phrasesForTelegramKeyboard);
    }

    @Override
    public SendPhoto preparePhotoMessage() throws IOException {
        var responseToUserMessage = prepareResponseToMessage();
        var messageText = responseToUserMessage.getFirst();
        var messageKeyboard = responseToUserMessage.getSecond();
        var message = FilmMessageFactory.convertBotResponseToPhotoMessage(messageText);
        TelegramButtonCreator.setKeyboardForMessage(message, messageKeyboard);
        return message;
    }

    @Override
    public SendMessage prepareTextMessage() {
        return null;
    }
}
