package pl.gda.pg.eti.autyzm.backupper.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class HashingUtils {
    public static byte[] hashMD5(File localFile) throws Exception {
        InputStream fis =  new FileInputStream(localFile);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) complete.update(buffer, 0, numRead);
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getChecksumMD5(File localFile) throws Exception {
        byte[] hash = hashMD5(localFile);
        String result = "";

        for (byte currentByte : hash) {
            result += Integer.toString((currentByte & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }
}
