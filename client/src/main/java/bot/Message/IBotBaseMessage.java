package bot.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public interface IBotBaseMessage {
    ArrayList<String> suitableUserMessage = new ArrayList<>();
    ArrayList<String> phrasesForTelegramKeyboard = new ArrayList<>(
            Arrays.asList("Get random film", "Get film by genre", "Get films by year"));

    void fillSuitableUserMessage() throws IOException;
}
