package pl.gda.pg.eti.autyzm.backupper.api;

public class AssetExtractorException extends RuntimeException {
    public AssetExtractorException(String message) {
        super(message);
    }

    public AssetExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssetExtractorException(Throwable cause) {
        super(cause);
    }
}
