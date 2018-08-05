package de.frittenburger.parser;
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
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.parser.impl.RequestLineParserImpl;
import de.frittenburger.parser.interfaces.RequestLineParser;

public class TestRequestLineParserImpl {

	@Test
	public void test() throws IOException {
		
		RequestLineParser parser = new RequestLineParserImpl();
		HttpRequest req = new HttpRequest();

		parser.parse("GET /index.html HTTP/1.1", req);
		assertEquals("/index.html", req.getUrl());
		assertEquals("GET", req.getMethod());

	}

}
