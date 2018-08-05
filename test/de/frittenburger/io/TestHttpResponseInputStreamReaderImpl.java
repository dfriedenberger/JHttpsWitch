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
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.impl.HttpResponseInputStreamReaderImpl;
import de.frittenburger.io.interfaces.HttpResponseInputStreamReader;
import de.frittenburger.parser.interfaces.HttpHeaderParser;
import de.frittenburger.parser.interfaces.ResponseLineParser;

public class TestHttpResponseInputStreamReaderImpl {

	@Test
	public void test() throws IOException {

	    ResponseLineParser responseLineParser = mock(ResponseLineParser.class);

		HttpHeaderParser httpHeaderParser = mock(HttpHeaderParser.class);
        doAnswer(new Answer<Object>() {
			
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {

				final HttpHeaders request = (HttpHeaders) (invocation.getArguments())[1];
				request.setContentLength(10);
				return null;
			}
			
		}).when(httpHeaderParser).parse(eq("Content-Length: 10"), any(HttpHeaders.class));
		
		HttpResponseInputStreamReader reader = new HttpResponseInputStreamReaderImpl(responseLineParser, httpHeaderParser);
		
		StringBuilder sb = new StringBuilder ();
		sb.append("HTTP/1.1 200 OK\r\n");
		sb.append("Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n");
		sb.append("AnyLine\r\n");
		sb.append("Content-Length: 10\r\n");
		sb.append("\r\n");
		sb.append("HelloWorld");

		
		ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
		
		
		HttpResponse res = reader.read(in);

		
		
		verify(responseLineParser).parse("HTTP/1.1 200 OK", res);
		verify(httpHeaderParser).parse("Date: Mon, 27 Jul 2009 12:28:53 GMT", res.getHttpHeaders());
		verify(httpHeaderParser).parse("AnyLine", res.getHttpHeaders());
		verify(httpHeaderParser).parse("Content-Length: 10", res.getHttpHeaders());
        
		assertEquals("HTTP/1.1 200 OK",res.getResponseLine());
	    assertEquals("HelloWorld",new String(res.getContent()));
	
	}
	
	@Test
	public void testChunkedTransfer() throws IOException {
	 ResponseLineParser responseLineParser = mock(ResponseLineParser.class);

		HttpHeaderParser httpHeaderParser = mock(HttpHeaderParser.class);
     doAnswer(new Answer<Object>() {
			
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {

				final HttpHeaders request = (HttpHeaders) (invocation.getArguments())[1];
				request.setChunkedTransferEncoding(true);
				return null;
			}
			
		}).when(httpHeaderParser).parse(eq("Transfer-Encoding: chunked"), any(HttpHeaders.class));
		
		HttpResponseInputStreamReader reader = new HttpResponseInputStreamReaderImpl(responseLineParser, httpHeaderParser);
		
		StringBuilder sb = new StringBuilder ();
		sb.append("HTTP/1.1 200 OK\r\n");
		sb.append("Transfer-Encoding: chunked\r\n");
		sb.append("Content-Type: text/html; charset=iso-8859-1\r\n");
		sb.append("Trailer: Cache-Control\r\n");
		sb.append("\r\n");
		sb.append("4\r\n");
		sb.append("Wiki\r\n");
		sb.append("a\r\n");
		sb.append("pedia12345\r\n");
		sb.append("0\r\n");
		sb.append("Cache-Control: no-cache\r\n");
		sb.append("\r\n");
		
		ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
		
		
		HttpResponse res = reader.read(in);

		
		
		verify(responseLineParser).parse("HTTP/1.1 200 OK", res);
		verify(httpHeaderParser).parse("Transfer-Encoding: chunked", res.getHttpHeaders());
		verify(httpHeaderParser).parse("Content-Type: text/html; charset=iso-8859-1", res.getHttpHeaders());
		verify(httpHeaderParser).parse("Trailer: Cache-Control", res.getHttpHeaders());
		verify(httpHeaderParser).parse("Cache-Control: no-cache", res.getHttpTrailers());

		assertEquals("HTTP/1.1 200 OK",res.getResponseLine());
	    assertEquals("4\r\nWiki\r\na\r\npedia12345\r\n0\r\n",new String(res.getContent()));
	}
}
