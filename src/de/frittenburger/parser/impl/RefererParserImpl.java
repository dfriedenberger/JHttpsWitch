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
import de.frittenburger.parser.interfaces.RefererParser;

public class RefererParserImpl implements RefererParser {

	@Override
	public String parse(String referer,String host) {
			
			if(referer == null) return "Direct";
			if(referer.isEmpty()) return "Direct";
			String rhost = parseHost(referer);
			if(rhost.equals(host)) return "Intern";
			return rhost;
			
	}

	private String parseHost(String referer) {
		
		for(String part : referer.split("/"))
		{
			//get first part seems like an host name
			if(part.isEmpty()) continue;
			if(part.equals("http:")) continue;
			if(part.equals("https:")) continue;
			return part;
		}
		return referer;
		
	}

}
