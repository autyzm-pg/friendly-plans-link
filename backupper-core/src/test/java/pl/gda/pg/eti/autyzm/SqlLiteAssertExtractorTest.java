package pl.gda.pg.eti.autyzm;

import org.junit.Before;
import org.junit.Test;
import pl.gda.pg.eti.autyzm.backupper.core.SqlLiteAssertExtractor;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SqlLiteAssertExtractorTest {

    Collection<URI> expectedURI;
    SqlLiteAssertExtractor sqlLiteAssertExtractor;
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
        sqlLiteAssertExtractor = new SqlLiteAssertExtractor();

    }

    @Test
    public void extractPathTest(){
        List URIlist = sqlLiteAssertExtractor.extractAsserts(dbFile);
        assertTrue("Should contains expected uris", URIlist.containsAll(expectedURI));
    }

    @Test
    public void extractDistinctPathsTest(){
        List URIlist = sqlLiteAssertExtractor.extractAsserts(dbFile);
        assertEquals("Should contains only expected uris", expectedURI.size(), URIlist.size());
    }



}
