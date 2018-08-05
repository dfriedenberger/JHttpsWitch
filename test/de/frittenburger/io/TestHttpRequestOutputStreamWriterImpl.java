package de.frittenburger.io;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.impl.HttpRequestOutputStreamWriterImpl;
import de.frittenburger.io.interfaces.HttpRequestOutputStreamWriter;

public class TestHttpRequestOutputStreamWriterImpl {

	@Test
	public void test() throws IOException {
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HttpRequestOutputStreamWriter writer = new HttpRequestOutputStreamWriterImpl();
		
		
		HttpRequest req = new HttpRequest();
		req.setRequestLine("GET /index.html HTTP/1.1");
		req.getHttpHeaders().getHeaders().add("Host: example.org");
		req.setContent(new String("Hello World").getBytes());
		writer.write(req, os);
		
	    String result = new String(os.toByteArray());
		StringBuilder expected = new StringBuilder();
		
		expected.append("GET /index.html HTTP/1.1\r\n");
		expected.append("Host: example.org\r\n");
		expected.append("\r\n");
		expected.append("Hello World");

		
		assertEquals(expected.toString(),result);
	}

}
