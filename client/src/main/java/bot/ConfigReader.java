package bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class ConfigReader {
    public static ArrayList<String> fillConfigArray(Properties config, String arrayName){
        var parts = config.getProperty(arrayName).split(";");
        return new ArrayList<>(Arrays.asList(parts));
    }

    public static HashMap<String, String> fillConfigMap(Properties config, String mapName){
        var configMap = new HashMap<String, String>();
        var parts = config.getProperty(mapName).split(";");
        for (String part:parts) {
            var keyAndValue = part.split(":");
            configMap.put(keyAndValue[0], keyAndValue[1]);
        }
        return configMap;
    }
}
