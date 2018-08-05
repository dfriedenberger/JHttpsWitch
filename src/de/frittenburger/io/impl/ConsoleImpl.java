package de.frittenburger.io.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.frittenburger.io.interfaces.Console;

public class ConsoleImpl implements Console {

	final BufferedReader br;
	public ConsoleImpl(InputStream in)
	{
		br = new BufferedReader(new InputStreamReader(in));
	}
	@Override
	public String getText(String text, String defaultValue) throws IOException {

		System.out.print(text+" ["+defaultValue+"]: ");
		System.out.flush();
		String line = br.readLine();
		if(line.trim().isEmpty())
			return defaultValue;
		return line.trim();
	}

	@Override
	public boolean getYesNo(String text, boolean defaultValue) throws IOException {
		System.out.print(text+" ["+(defaultValue?"Y":"N")+"]: ");
		System.out.flush();
		String line = br.readLine();
		if(line.trim().isEmpty())
			return defaultValue;
		switch(line.trim().toLowerCase())
		{
			case "j":
			case "y":
				return true;
		}
		return false;
	}

}
