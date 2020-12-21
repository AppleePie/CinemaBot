package bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TelegramButtonCreator {
    public static synchronized void setKeyboardForMessage(
            SendPhoto sendMessage, ArrayList<String> opinionForKeyboard) {
        sendMessage.setReplyMarkup(createKeyboardForChoiceFunctionSelectFilm(opinionForKeyboard));
    }

    public static synchronized void setKeyboardForMessage(
            SendMessage sendMessage, ArrayList<String> opinionForKeyboard) {
        sendMessage.setReplyMarkup(createKeyboardForChoiceFunctionSelectFilm(opinionForKeyboard));
    }


    public static synchronized ReplyKeyboardMarkup createKeyboardForChoiceFunctionSelectFilm(
            ArrayList<String> opinionForKeyboard) {
        var choiceFunctionSelectFilmKeyboardRow = getKeyboardRow(opinionForKeyboard);
        return new ReplyKeyboardMarkup(
                choiceFunctionSelectFilmKeyboardRow, true, false, true);
    }

    private static List<KeyboardRow> getKeyboardRow(ArrayList<String> options) {
        var choiceFunctionSelectFilmKeyboardRow = new ArrayList<KeyboardRow>();
        for (String phrase : options) {
            var keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(phrase));
            choiceFunctionSelectFilmKeyboardRow.add(keyboardRow);
        }
        return choiceFunctionSelectFilmKeyboardRow;
    }
}
