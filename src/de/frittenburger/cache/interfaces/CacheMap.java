package de.frittenburger.cache.interfaces;

import de.frittenburger.io.bo.HttpResponse;

public interface CacheMap {

	boolean containsKey(String key);

	HttpResponse get(String key);

	int size();

	HttpResponse remove();

	void put(String key, HttpResponse res);

}
