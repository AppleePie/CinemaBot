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

public class MessageGetFilmsByYear implements IBotBaseMessage, IBotMoreMessagesReturnUserMessageImportant {
    public MessageGetFilmsByYear() throws IOException {
        fillSuitableUserMessage();
    }

    @Override
    public void fillSuitableUserMessage() throws IOException {
        var allYears = RequestHandler.getAllReleaseYears();
        suitableUserMessage.addAll(allYears);
    }

    @Override
    public Pair<String, ArrayList<String>> prepareResponseToMessage(String userMessage) throws IOException {
        var response = RequestHandler.getFilmAsJson(userMessage);
        return new Pair<>(response, phrasesForTelegramKeyboard);
    }

    @Override
    public ArrayList<SendPhoto> preparePhotoMessage(String userMessage) throws IOException {
        var responseToUserMessage = prepareResponseToMessage(userMessage);
        var messageText = responseToUserMessage.getFirst().split(";");
        var messageKeyboard = responseToUserMessage.getSecond();

        var listMessages = new ArrayList<SendPhoto>();
        for (String text: messageText) {
            var message = FilmMessageFactory.convertBotResponseToPhotoMessage(text);
            TelegramButtonCreator.setKeyboardForMessage(message, messageKeyboard);
            listMessages.add(message);
        }
        return listMessages;
    }

    @Override
    public ArrayList<SendMessage> prepareTextMessage(String userMessage) {
        return null;
    }
}
