package pl.gda.pg.eti.autyzm.backupper.core;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PrimitiveIterator;

/**
 * Created by marcin on 2016-08-31.
 */
public class BackupLink {

    static ArrayList<BackupLink> listOfLocalBuckups;

    private String name;

    private LocalDateTime dateOfCreation;

    private Path pathToFolder;

    public Path getPathToFolder() {
        return pathToFolder;
    }

    public void setPathToFolder(Path pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public void listOfLocalBuckups_Clear(){
        BackupLink.listOfLocalBuckups.clear();
    }

    public void listOfLocalBuckups_Add(BackupLink backupLink){
        BackupLink.listOfLocalBuckups.add(backupLink);
    }

    // initialization of the list
    public static void populate(){
        // ...
        Path currentDirectory = Paths.get(".", "data");
    }
}
