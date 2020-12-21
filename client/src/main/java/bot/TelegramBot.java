package bot;

import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
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
        var botMessageFactoryClass = new BotMessageFactory();
        var pairPhotoPostAndMessages = new Pair<ArrayList<SendPhoto>, ArrayList<SendMessage>>();
        try {
            pairPhotoPostAndMessages = botMessageFactoryClass.replyToUserMessage(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var listPhotoMessages = pairPhotoPostAndMessages.getFirst();
        var listMessages = pairPhotoPostAndMessages.getSecond();
        listPhotoMessages.forEach(this::sendResponse);
        listMessages.forEach(this::sendResponse);
    }

    private void sendResponse(SendMessage response) {
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(SendPhoto response) {
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
