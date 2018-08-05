package de.frittenburger.parser.impl;
/*
 *  Copyright notice
 *
 *  (c) 2016 Dirk Friedenberger <projekte@frittenburger.de>
 *
 *  All rights reserved
 *
 *  This script is part of the JHttpSwitch project. The JHttpSwitch is
 *  free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  The GNU General Public License can be found at
 *  http://www.gnu.org/copyleft/gpl.html.
 *
 *  This script is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  This copyright notice MUST APPEAR in all copies of the script!
 */
import java.io.IOException;

import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.parser.interfaces.RequestLineParser;

public class RequestLineParserImpl implements RequestLineParser {

	@Override
	public void parse(String line, HttpRequest req) throws IOException {
		
		if(line == null) return;
		String p[] = line.split(" ");
		if(p.length != 3) 
		{
			throw new IOException(line +" <= not parsable");
		}
		
		String method = p[0];
		String url = p[1];
		//String proto = p[2];
		
		//Parse
		int i = url.indexOf("?");
		//String param = "";
		if(i >= 0) 
		{
			//param = url.substring(i);
			url = url.substring(0,i);
		}
		
		req.setUrl(url);
		req.setMethod(method);
	}

}
