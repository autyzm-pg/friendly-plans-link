package pl.gda.pg.eti.autyzm;

import org.junit.Test;
import pl.gda.pg.eti.autyzm.backupper.core.USBFilesManager;


public class TestUSBFilesManager {

    @Test
    public void testCopyingFileFromDevice() {
        USBFilesManager usbFilesManager = new USBFilesManager();
        usbFilesManager.copyFile("/Users/mikolevy/Documents/TMP/test-link/tester", "/Users/mikolevy/Documents/TMP/test-link-dst/");
    }
}
