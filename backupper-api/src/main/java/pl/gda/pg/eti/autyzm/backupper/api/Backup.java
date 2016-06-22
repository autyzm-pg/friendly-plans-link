package pl.gda.pg.eti.autyzm.backupper.api;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Created by superuser on 22-Jun-16.
 */
public class Backup {
    private String name;
    private LocalDateTime timestamp;
    private File homeDir;

    public Backup(String name, LocalDateTime timestamp, File homeDir) {
        this.name = name;
        this.timestamp = timestamp;
        this.homeDir = homeDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public File getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(File homeDir) {
        this.homeDir = homeDir;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Backup))
            return false;

        Backup backup = (Backup) o;

        if (!getName().equals(backup.getName()))
            return false;
        if (!getTimestamp().equals(backup.getTimestamp()))
            return false;
        return getHomeDir().equals(backup.getHomeDir());

    }

    @Override public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getTimestamp().hashCode();
        result = 31 * result + getHomeDir().hashCode();
        return result;
    }

    @Override public String toString() {
        return "Backup{" +
            "name='" + name + '\'' +
            ", timestamp=" + timestamp +
            ", homeDir=" + homeDir +
            '}';
    }
}
