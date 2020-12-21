package bot.Message;

import bot.FilmMessageFactory;
import bot.TelegramButtonCreator;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.ArrayList;

public class MessageReactionToIncorrectCommand implements IBotBaseMessage, IBotMessagesWhereUnimportantUserMessage {
    @Override
    public void fillSuitableUserMessage() { }

    @Override
    public Pair<String, ArrayList<String>> prepareResponseToMessage() {
        return new Pair<>("I don't understand this command",
                phrasesForTelegramKeyboard);
    }

    @Override
    public SendPhoto preparePhotoMessage() {
        return null;
    }

    @Override
    public SendMessage prepareTextMessage() {
        var responseToUserMessage = prepareResponseToMessage();
        var messageText = responseToUserMessage.getFirst();
        var messageKeyboard = responseToUserMessage.getSecond();
        var message = FilmMessageFactory.convertBotResponseToMessage(messageText);
        TelegramButtonCreator.setKeyboardForMessage(message, messageKeyboard);
        return message;
    }
}
