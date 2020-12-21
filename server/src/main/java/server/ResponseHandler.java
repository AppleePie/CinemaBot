package server;

public class ResponseHandler {
    public static Option analyzeRequest(String userInput) {
        if (userInput.equals("allGenres")) {
            return Option.AllAvailableGenres;
        }
        if (userInput.equals("allYears")) {
            return Option.AllReleaseYears;
        }
        if (userInput.equals("random")) {
            return Option.RandomFilm;
        }
        if (userInput.chars().allMatch(Character::isDigit)) {
            return Option.FilmsInYear;
        }
        return Option.FilmWithGenre;
    }

    public static String createResponse(String userInput, Option option) {
        return switch(option) {
            case AllAvailableGenres ->  String.join(ConfigHelper.DELIMITER, ImdbInformationManager.getAllAvailableGenresAsStrings());
            case AllReleaseYears -> String.join(ConfigHelper.DELIMITER, ImdbInformationManager.getAllAvailableReleaseYearsAsStrings());
            case FilmWithGenre -> ImdbInformationManager.findFilmWithGenre(userInput);
            case FilmsInYear -> ImdbInformationManager.findFilmsWithYear(userInput);
            case RandomFilm -> ImdbInformationManager.findRandomFilm();
        };
    }
}
