package bot;

import org.json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class jsonFilmParser {
    public static Map jsonParseToMap(String jsonString){
        var filmData = new HashMap<>();
        var data = new JSONObject(jsonString);
        var stringValues = new ArrayList<>(Arrays.asList(
                "title", "url", "poster",
                "description", "timing", "fullReleaseDate"));
        for (String key: stringValues) {
            filmData.put(key, data.getString(key));
        }
        var arrayValue = "genres";
        filmData.put(arrayValue, data.getJSONArray(arrayValue));
        return filmData;
    }
}
