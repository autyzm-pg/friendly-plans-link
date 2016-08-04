package pl.gda.pg.eti.autyzm.backupper.api;

/**
 * Created by superuser on 22-Jun-16.
 */
public class AssertExtractorException extends RuntimeException {
    public AssertExtractorException(String message) {
        super(message);
    }

    public AssertExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertExtractorException(Throwable cause) {
        super(cause);
    }
}
