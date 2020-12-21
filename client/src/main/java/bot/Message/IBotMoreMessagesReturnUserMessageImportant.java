package bot.Message;

import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.util.ArrayList;

public interface IBotMoreMessagesReturnUserMessageImportant {
    Pair<String, ArrayList<String>> prepareResponseToMessage(String userMessage) throws IOException;
    ArrayList<SendPhoto> preparePhotoMessage(String userMessage) throws IOException;
    ArrayList<SendMessage> prepareTextMessage(String userMessage) throws IOException;
}
