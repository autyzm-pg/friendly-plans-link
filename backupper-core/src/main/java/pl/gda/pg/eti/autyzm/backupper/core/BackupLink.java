package pl.gda.pg.eti.autyzm.backupper.core;

import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Created by marcin on 2016-08-31.
 */
public class BackupLink {

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
}
