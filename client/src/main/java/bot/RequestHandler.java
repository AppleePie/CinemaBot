package bot;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class RequestHandler {
    private static String IP;

    static {
        try {
            IP = InetAddress.getByName(ConfigHelper.HOST).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getAllReleaseYears() throws IOException {
        return getAllStrsFor("allYears");
    }

    public static ArrayList<String> getAllAvailableGenres() throws IOException {
        return getAllStrsFor("allGenres");
    }

    public static String getFilmAsJson(String option) throws IOException {
        return getResponseFor(option);
    }

    private static ArrayList<String> getAllStrsFor(String request) throws IOException {
        String[] allStrs = getResponseFor(request).split(ConfigHelper.DELIMITER);
        return new ArrayList<>(Arrays.asList(allStrs));
    }

    private static String getResponseFor(String criteria) throws IOException {
        return Request.Get(String.format("http://%s:%d/get?parts=%s", IP, ConfigHelper.PORT, criteria))
                .execute()
                .returnContent()
                .asString();
    }
}
