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

import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.impl.HttpResponseOutputStreamWriterImpl;
import de.frittenburger.io.interfaces.HttpResponseOutputStreamWriter;

public class TestHttpResponseOutputStreamWriterImpl {

	
	@Test
	public void testResponse() throws IOException {
		
		HttpResponseOutputStreamWriter out = new HttpResponseOutputStreamWriterImpl();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HttpResponse res = new HttpResponse();
		res.setStatus(HttpResponse.OK);
		res.getHttpHeaders().setChunkedTransferEncoding(true);
		res.getHttpHeaders().getHeaders().add("Transfer-Encoding: chunked");
		res.getHttpHeaders().getHeaders().add("Trailer: Cache-Control");
		res.setContent("17\r\n<h1>TestHtml</h1>\r\n0\r\n".getBytes());
		res.getHttpTrailers().getHeaders().add("Cache-Control: no-cache");
		
		out.write(os, res);
		String result = new String(os.toByteArray());
		
		StringBuilder expected = new StringBuilder();
		
		expected.append("HTTP/1.1 200 Ok\r\n");
		expected.append("Transfer-Encoding: chunked\r\n");
		expected.append("Trailer: Cache-Control\r\n");
		expected.append("\r\n");
		expected.append("17\r\n");
		expected.append("<h1>TestHtml</h1>\r\n");
		expected.append("0\r\n");
		expected.append("Cache-Control: no-cache\r\n");
		expected.append("\r\n");

		
		assertEquals(expected.toString(),result);
	}

	
	@Test
	public void testUnavailable() throws IOException {
		
		HttpResponseOutputStreamWriter out = new HttpResponseOutputStreamWriterImpl();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String html = "<h1>TestHtml</h1>";
		HttpResponse res = new HttpResponse();
		res.setError(HttpResponse.SERVICE_UNAVAILABLE, html);
		
		out.write(os, res);
		
		
		String result = new String(os.toByteArray());
		
		StringBuilder expected = new StringBuilder();
		
		expected.append("HTTP/1.1 503 Service unavailable\r\n");
		expected.append("Content-Length: 17\r\n");
		expected.append("Content-Type: text/html\r\n");
		expected.append("Connection: Closed\r\n");
		expected.append("\r\n");
		expected.append("<h1>TestHtml</h1>");

		
		assertEquals(expected.toString(),result);
	}

	@Test
	public void testRedirect() throws IOException {
		
		HttpResponseOutputStreamWriter out = new HttpResponseOutputStreamWriterImpl();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		HttpResponse res = new HttpResponse();
		res.setPermanentRedirect("https://example.org");
		
		out.write(os, res);
		
		
		String result = new String(os.toByteArray());
		
		StringBuilder expected = new StringBuilder();
		
		expected.append("HTTP/1.1 301 Moved Permanently\r\n");
		expected.append("Location: https://example.org\r\n");
		expected.append("\r\n");

		
		assertEquals(expected.toString(),result);
	}
	
}
