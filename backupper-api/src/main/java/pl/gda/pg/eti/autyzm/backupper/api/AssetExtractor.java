package pl.gda.pg.eti.autyzm.backupper.api;

import java.net.URI;
import java.util.Collection;

public interface AssetExtractor<S> {
    Collection<URI> extractAssets(S source);
}
