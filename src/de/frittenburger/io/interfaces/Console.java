package de.frittenburger.io.interfaces;

import java.io.IOException;

public interface Console {

	String getText(String text, String defaultValue) throws IOException;

	boolean getYesNo(String text, boolean defaultValue) throws IOException;

}
