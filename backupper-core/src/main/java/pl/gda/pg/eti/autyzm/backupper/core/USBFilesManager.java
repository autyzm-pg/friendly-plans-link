package pl.gda.pg.eti.autyzm.backupper.core;

import java.io.*;
import java.nio.file.Files;


public class USBFilesManager {

    public void copyFile(String srcPath, String dstPath) {
        try {
            File srcFile = new File(srcPath);
            String srcFileName = srcFile.getName();
            dstPath += srcFileName;
            File dstFile = new File(dstPath);

            Files.copy(srcFile.toPath(), dstFile.toPath());
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

}
