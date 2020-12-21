package bot;

import java.io.IOException;
import java.util.*;

public class UserIntentionDeterminer {
    private static ArrayList<String> GENRES;
    private static ArrayList<String> YEARS;

    static {
        try {
            GENRES = RequestHandler.getAllAvailableGenres();
            YEARS = RequestHandler.getAllReleaseYears();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserIntention determineUserIntentions(String userMessage) {
        var messageOneCorrespondence = new HashMap<String, UserIntention>() {{
            put("Get random film", UserIntention.GET_RANDOM_FILM);
            put("Get film by genre", UserIntention.GET_GENRES);
            put("Get films by year", UserIntention.GET_YEARS);
            put("/start", UserIntention.GET_START);
            put("/help", UserIntention.GET_HELP);
        }};
        if (messageOneCorrespondence.containsKey(userMessage))
            return messageOneCorrespondence.get(userMessage);
        else if (GENRES.contains(userMessage))
            return UserIntention.GET_FILM_BY_GENRES;
        else if (YEARS.contains(userMessage))
            return UserIntention.GET_FILMS_BY_YEAR;
        else return UserIntention.INTENTIONS_ARE_NOT_CLEAR;
    }
}
