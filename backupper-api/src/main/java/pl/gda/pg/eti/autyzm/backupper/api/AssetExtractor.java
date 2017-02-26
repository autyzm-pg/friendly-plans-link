package pl.gda.pg.eti.autyzm.backupper.api;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

public interface AssetExtractor<S> {
    Collection<URI> extractAssets(S source);
}
