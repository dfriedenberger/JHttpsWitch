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

import org.junit.Test;

import de.frittenburger.parser.impl.MimeTypeParserImpl;
import de.frittenburger.parser.interfaces.MimeTypeParser;

public class TestMimeTypeParserImpl {

	@Test
	public void test() {
		MimeTypeParser parser = new MimeTypeParserImpl();
		assertEquals("text/html",parser.parse("text/html","irgendwas"));	
		assertEquals("text/html",parser.parse(null,"/xxxx/xx.html"));	
		assertEquals("text/html",parser.parse(null,"/xxxx/xxxx"));	
		assertEquals("text/html",parser.parse(null,"/xxxx.aaaa/xxxx"));	
		assertEquals("text/html",parser.parse(null,"\\xxxx.aaaa\\xxxx"));	

		assertEquals("application/javascript",parser.parse(null,"/xxxx/xx.js"));	
		assertEquals("application/json",parser.parse(null,"/xxxx/xx.json"));	
		assertEquals("text/css",parser.parse(null,"/xxxx/xx.css"));	
		assertEquals("image/jpeg",parser.parse(null,"/xxxx/xx.jpeg"));	
		assertEquals("image/jpeg",parser.parse(null,"/xxxx/xx.JPG"));	
		assertEquals("image/png",parser.parse(null,"/xxxx/xx.png"));	
		assertEquals("image/gif",parser.parse(null,"/xxxx/xx.gif"));	

	}

}
