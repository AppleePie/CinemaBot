package tests;

import org.junit.Test;
import server.DocumentManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DocumentManagerTest {
    @Test(expected = IllegalArgumentException.class)
    public void getDocumentFromLinkToSite_EmptyLink() throws Exception {
        var link = "";
        DocumentManager.getDocumentFromLinkToSite(link);
    }

    @Test(expected = IOException.class)
    public void getDocumentFromLinkToSite_IncorrectLink() throws Exception {
        var link = "https://www.imdb.comm";
        DocumentManager.getDocumentFromLinkToSite(link);
    }

    @Test
    public void getDocumentFromLinkToSite_CorrectLink() throws Exception {
        var link = "http://urgu.org/";
        assertNotNull(DocumentManager.getDocumentFromLinkToSite(link));
    }

    @Test(expected = AccessDeniedException.class)
    public void getDocumentFromPathToFile_EmptyPath() throws IOException, URISyntaxException {
        var link = "";
        var pathToFile = Paths.get(DocumentManagerTest.class.getResource(link).toURI());
        DocumentManager.getDocumentFromPathToFile(pathToFile);
    }

    @Test(expected = NullPointerException.class)
    public void getDocumentFromPathToFile_IncorrectLink() throws IOException, URISyntaxException {
        var link = "UnCoRrEcTfIlE";
        var pathToFile = Paths.get(DocumentManagerTest.class.getResource(link).toURI());
        DocumentManager.getDocumentFromPathToFile(pathToFile);
    }

    @Test
    public void getDocumentFromPathToFile_CorrectLink() throws Exception {
        var link = "/website imdb for test/film-page-correct.html";
        var pathToFile = Paths.get(DocumentManagerTest.class.getResource(link).toURI());
        assertNotNull(DocumentManager.getDocumentFromPathToFile(pathToFile));
    }
}
