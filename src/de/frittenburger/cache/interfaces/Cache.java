package de.frittenburger.cache.interfaces;

import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;

public interface Cache {

	boolean contains(HttpRequest req);

	HttpResponse get(HttpRequest req);

	void put(HttpRequest req, HttpResponse res);

}
