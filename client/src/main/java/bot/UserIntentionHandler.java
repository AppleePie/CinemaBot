package bot;

import org.glassfish.grizzly.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class UserIntentionHandler {
    private static final Properties config = new Properties();
    private static Map<String, String> REACTION_TO_USER_INTENTION;
    private static ArrayList<String> PHRASES;
    private static ArrayList<String> GENRES;

    private void initProperties() throws IOException {
        final InputStream propertiesSource = this.getClass().getResourceAsStream("/configUserMessage.ini");
        config.load(propertiesSource);
    }

    public UserIntentionHandler() throws IOException {
        initProperties();
        REACTION_TO_USER_INTENTION = ConfigReader.fillConfigMap(config,"REACTION_TO_USER_INTENTION");
        PHRASES = ConfigReader.fillConfigArray(config,"PHRASES");
        GENRES = ConfigReader.fillConfigArray(config,"GENRES");
    }

    public Pair<String, ArrayList<String>> prepareResponseToUserIntent(String userIntention) throws IOException {
        var response = "";
        ArrayList<String> arrayForTelegramKeyboardOptions;
        if (REACTION_TO_USER_INTENTION.containsKey(userIntention)) {
            response = REACTION_TO_USER_INTENTION.get(userIntention);
            if (userIntention.equals("GetByGenre")) {
                arrayForTelegramKeyboardOptions = GENRES;
            }
            else
                arrayForTelegramKeyboardOptions = PHRASES;
        }
        else {
            response = (userIntention.equals("GetRandom")
                        ? RequestHandler.getFilm("random")
                        : RequestHandler.getFilm(userIntention));
            arrayForTelegramKeyboardOptions = PHRASES;
        }
        var responseToUserAndArrayForTelegramKeyboard = new Pair<>(response, arrayForTelegramKeyboardOptions);
        return responseToUserAndArrayForTelegramKeyboard;
    }
}
