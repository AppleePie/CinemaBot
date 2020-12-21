package bot.Message;

import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.util.ArrayList;

public interface IBotMessagesWhereUnimportantUserMessage {
    Pair<String, ArrayList<String>> prepareResponseToMessage() throws IOException;
    SendPhoto preparePhotoMessage() throws IOException;
    SendMessage prepareTextMessage() throws IOException;
}
