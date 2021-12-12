package de.frittenburger.core.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.routing.bo.Target;

public interface WebsocketHandler {

	void handle(Target target, HttpRequest req, InputStream in, OutputStream out) throws IOException, GeneralSecurityException, RuntimeException, ReflectiveOperationException;

}
