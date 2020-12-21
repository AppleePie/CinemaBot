package bot.Message;

import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.util.ArrayList;

public interface IBotMessagesWhereImportantUserMessage {
    Pair<String, ArrayList<String>> prepareResponseToMessage(String userMessage) throws IOException;
    SendPhoto preparePhotoMessage(String userMessage) throws IOException;
    SendMessage prepareTextMessage(String userMessage);
}
