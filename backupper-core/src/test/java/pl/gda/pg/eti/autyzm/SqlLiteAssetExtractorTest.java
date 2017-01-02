package pl.gda.pg.eti.autyzm;

import org.junit.Before;
import org.junit.Test;
import pl.gda.pg.eti.autyzm.backupper.core.SqlLiteAssetExtractor;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SqlLiteAssetExtractorTest {
    private Collection<URI> expectedURI;
    private SqlLiteAssetExtractor sqlLiteAssetExtractor;
    private File dbFile;

    @Before
    public void setUp(){
        expectedURI = new ArrayList<URI>() {{
            add(URI.create("/storage/emulated/0/Download/mycie_zebow.png"));
            add(URI.create("/storage/emulated/0/Download/szczoteczka.jpg"));
            add(URI.create("/storage/emulated/0/Download/pasta.png"));
            add(URI.create("/storage/emulated/0/Download/szklanka-wody.png"));
            add(URI.create("/storage/emulated/0/Download/szlaczki.png"));
            add(URI.create("/storage/emulated/0/Download/teczka.jpg"));
            add(URI.create("/storage/emulated/0/Download/kredki.jpg"));
            add(URI.create("/storage/emulated/0/Music/banki.mp3"));
        }};

        dbFile = new File("src/test/resources/testDbFile/commments2.db");
        sqlLiteAssetExtractor = new SqlLiteAssetExtractor();

    }

    @Test
    public void extractPathTest() {
        List<URI> URIlist = sqlLiteAssetExtractor.extractAssets(dbFile);
        assertTrue("Should contains expected uris", URIlist.containsAll(expectedURI));
    }

    @Test
    public void extractDistinctPathsTest() {
        List<URI> URIlist = sqlLiteAssetExtractor.extractAssets(dbFile);
        assertEquals("Should contains only expected uris", expectedURI.size(), URIlist.size());
    }
}
