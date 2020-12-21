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

public class MessageGetYears implements IBotBaseMessage, IBotMessagesWhereUnimportantUserMessage {
    public MessageGetYears() {
        fillSuitableUserMessage();
    }

    @Override
    public void fillSuitableUserMessage() {
        suitableUserMessage.add("Get films by year");
    }

    @Override
    public Pair<String, ArrayList<String>> prepareResponseToMessage() throws IOException {
        var allYears = RequestHandler.getAllReleaseYears();
        return new Pair<>("Choice year", allYears);
    }

    @Override
    public SendPhoto preparePhotoMessage() {
        return null;
    }

    @Override
    public SendMessage prepareTextMessage() throws IOException {
        var responseToUserMessage = prepareResponseToMessage();
        var messageText = responseToUserMessage.getFirst();
        var messageKeyboard = responseToUserMessage.getSecond();
        var message = FilmMessageFactory.convertBotResponseToMessage(messageText);
        TelegramButtonCreator.setKeyboardForMessage(message, messageKeyboard);
        return message;
    }
}
