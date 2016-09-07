package pl.gda.pg.eti.autyzm.backupper.core;

import javax.swing.*;
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

    public BackupLink(){}

    public BackupLink(String name){
        this.setName(name);
    }

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


}
