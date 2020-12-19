package bot;

import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    private static final Properties config = new Properties();
    private static String TOKEN;
    private static String USERNAME;

    public TelegramBot() throws IOException {
        initProperties();
        TOKEN = config.getProperty("TOKEN");
        USERNAME = config.getProperty("USERNAME");
    }

    private void initProperties() throws IOException {
        final InputStream propertiesSource = this.getClass().getResourceAsStream("/configTelegramBot.ini");
        config.load(propertiesSource);
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        UserMessageParser userMessageParser = null;
        UserIntentionHandler userIntentionHandler = null;

        try {
            userMessageParser = new UserMessageParser();
            userIntentionHandler = new UserIntentionHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }

        var userIntention = userMessageParser.parseUserMessage(update);
        Pair<String, ArrayList<String>> responseToUserIntent = null;
        try {
            responseToUserIntent = userIntentionHandler.prepareResponseToUserIntent(userIntention);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var dataForMessage = responseToUserIntent.getFirst();
        var responseTelegramKeyboardOptions = responseToUserIntent.getSecond();
        var chatId = update.getMessage().getChatId().toString();
        sendReplyMessage(chatId, dataForMessage, responseTelegramKeyboardOptions);
    }

    private void sendResponse(SendPhoto response) {
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendReplyMessage(
            String chatId, String dataForMessage,
            ArrayList<String> opinionForKeyboard) {
        var message = FilmMessageFactory.convertBotResponseToMessage(dataForMessage);
        setKeyboardForMessage(message, opinionForKeyboard);
        message.setChatId(chatId);
        sendResponse(message);
    }

    public synchronized void setKeyboardForMessage(SendPhoto sendMessage, ArrayList<String> opinionForKeyboard) {
        var choiceFunctionSelectFilmKeyboardRow = getKeyboardRow(opinionForKeyboard);
        var keyboardForChoiceFunctionSelectFilm = new ReplyKeyboardMarkup(
                choiceFunctionSelectFilmKeyboardRow, true, false, true);
        sendMessage.setReplyMarkup(keyboardForChoiceFunctionSelectFilm);
    }

    private List<KeyboardRow> getKeyboardRow(ArrayList<String> options) {
        var choiceFunctionSelectFilmKeyboardRow = new ArrayList<KeyboardRow>();
        for (String phrase : options) {
            var keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(phrase));
            choiceFunctionSelectFilmKeyboardRow.add(keyboardRow);
        }
        return choiceFunctionSelectFilmKeyboardRow;
    }
}
