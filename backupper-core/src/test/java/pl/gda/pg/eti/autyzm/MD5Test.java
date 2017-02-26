package pl.gda.pg.eti.autyzm;

import org.junit.Before;
import org.junit.Test;
import pl.gda.pg.eti.autyzm.backupper.core.HashingUtils;
import pl.gda.pg.eti.autyzm.backupper.core.SqlLiteAssetExtractor;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MD5Test {
    private final String expectedHash = "7d9e02d66c4c77541efcdf28712a31a9";
    private String calculatedHash;
    private File fileToTest;
    private HashingUtils hasher;

    @Before
    public void setUp() {
        fileToTest = new File("src/test/resources/testMD5/test.file");
        hasher = new HashingUtils();
    }

    @Test
    public void hashingTest() {
        try {
            calculatedHash = HashingUtils.getChecksumMD5(fileToTest);
        } catch (Exception e) {
            assertTrue("Couldn't hash the file.", false);
        }

        assertTrue("Calculated MD5 hash didn't match the expected hash.", expectedHash.equals(calculatedHash));
    }
}
