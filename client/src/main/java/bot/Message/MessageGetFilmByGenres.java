package bot.Message;

import bot.FilmMessageFactory;
import bot.RequestHandler;
import bot.TelegramButtonCreator;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.util.ArrayList;

public class MessageGetFilmByGenres implements IBotBaseMessage, IBotMessagesWhereImportantUserMessage {
    public MessageGetFilmByGenres() throws IOException {
        fillSuitableUserMessage();
    }

    @Override
    public void fillSuitableUserMessage() throws IOException {
        var allGenres = RequestHandler.getAllAvailableGenres();
        suitableUserMessage.addAll(allGenres);
    }

    @Override
    public Pair<String, ArrayList<String>> prepareResponseToMessage(String userMessage) throws IOException {
        var response = RequestHandler.getFilmAsJson(userMessage);
        return new Pair<>(response, phrasesForTelegramKeyboard);
    }

    @Override
    public SendPhoto preparePhotoMessage(String userMessage) throws IOException {
        var responseToUserMessage = prepareResponseToMessage(userMessage);
        var messageText = responseToUserMessage.getFirst();
        var messageKeyboard = responseToUserMessage.getSecond();
        var message = FilmMessageFactory.convertBotResponseToPhotoMessage(messageText);
        TelegramButtonCreator.setKeyboardForMessage(message, messageKeyboard);
        return message;
    }

    @Override
    public SendMessage prepareTextMessage(String userMessage) {
        return null;
    }
}
