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

import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.parser.interfaces.ResponseLineParser;

public class ResponseLineParserImpl implements ResponseLineParser {

	@Override
	public void parse(String line, HttpResponse res) throws IOException {
		
		if(line == null) return;
		String p[] = line.split(" ");
		
		
		//String proto = p[0];
		String status = p[1];
		//String text = p[2]; - n
		
		res.setStatus(Integer.parseInt(status));
	}

}
