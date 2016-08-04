package pl.gda.pg.eti.autyzm.backupper.api;

import java.net.URI;
import java.util.Collection;

public interface AssertExtractor<S> {
    Collection<URI> extractAsserts(S source);
}
