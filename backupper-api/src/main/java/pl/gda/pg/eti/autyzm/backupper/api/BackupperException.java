package pl.gda.pg.eti.autyzm.backupper.api;

/**
 * Created by superuser on 22-Jun-16.
 */
public class BackupperException extends RuntimeException {
    public BackupperException(String message) {
        super(message);
    }

    public BackupperException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackupperException(Throwable cause) {
        super(cause);
    }
}
