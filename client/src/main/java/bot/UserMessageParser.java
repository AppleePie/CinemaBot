package bot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class UserMessageParser {
    private static final Properties config = new Properties();
    private static String REACTION_TO_INCORRECT_COMMAND;
    private static Map<String, String> USER_INTENTIONS;
    private static Map<String, String> TELEGRAM_COMMANDS;
    private static ArrayList<String> PHRASES;
    private static ArrayList<String> GENRES;

    public UserMessageParser() throws IOException {
        initProperties();
        USER_INTENTIONS = ConfigReader.fillConfigMap(config, "USER_INTENTIONS");
        TELEGRAM_COMMANDS = ConfigReader.fillConfigMap(config, "TELEGRAM_COMMANDS");
        PHRASES = ConfigReader.fillConfigArray(config, "PHRASES");
        GENRES = ConfigReader.fillConfigArray(config, "GENRES");
        REACTION_TO_INCORRECT_COMMAND = config.getProperty("REACTION_TO_INCORRECT_COMMAND");
    }

    public String parseUserMessage(Update update) {
        if (checkUserResponseForValidity(update)) {
            var userIntention = "";
            var userMessage = update.getMessage().getText();
            if (isATelegramCommand(userMessage)) {
                userIntention = recognizeTelegramCommands(userMessage);
            } else {
                userIntention = recognizeUserIntention(userMessage);
            }
            return userIntention;
        } else
            return REACTION_TO_INCORRECT_COMMAND;
    }

    private boolean isATelegramCommand(String userMessage) {
        return TELEGRAM_COMMANDS.containsValue(userMessage);
    }

    private String recognizeTelegramCommands(String userMessage) {
        var userIntention = "";
        for (String key : TELEGRAM_COMMANDS.keySet())
            if (userMessage.equals(TELEGRAM_COMMANDS.get(key)))
                userIntention = USER_INTENTIONS.get(key);
        return userIntention;
    }

    private String recognizeUserIntention(String userMessage) {
        var userIntention = "";
        if (PHRASES.contains(userMessage)) {
            if (userMessage.contains("random"))
                userIntention = USER_INTENTIONS.get("USER_WANTS_RANDOM_FILM");
            else
                userIntention = USER_INTENTIONS.get("USER_WANTS_FILM_BY_GENRE");
        }
        else
            userIntention = userMessage;
        return userIntention;
    }

    private boolean checkUserResponseForValidity(Update userResponse) {
        return userResponse.getMessage() != null
                && userResponse.getMessage().hasText()
                && ((PHRASES.contains(userResponse.getMessage().getText())
                || GENRES.contains(userResponse.getMessage().getText()))
                || TELEGRAM_COMMANDS.containsValue(userResponse.getMessage().getText()));
    }

    private void initProperties() throws IOException {
        final InputStream propertiesSource = this.getClass().getResourceAsStream("/configUserMessage.ini");
        config.load(propertiesSource);
    }
}
