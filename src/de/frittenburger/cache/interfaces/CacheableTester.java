package de.frittenburger.cache.interfaces;

import de.frittenburger.io.bo.HttpRequest;

public interface CacheableTester {

	boolean isCacheable(HttpRequest req);

}
