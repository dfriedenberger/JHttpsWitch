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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.impl.HttpRequestInputStreamReaderImpl;
import de.frittenburger.io.interfaces.HttpRequestInputStreamReader;
import de.frittenburger.parser.interfaces.HttpHeaderParser;
import de.frittenburger.parser.interfaces.RequestLineParser;
import static org.mockito.Mockito.*;

public class TestHttpRequestInputStreamReaderImpl {

	@Test
	public void testGet() throws IOException {
		
		RequestLineParser requestLineParser = mock(RequestLineParser.class);

		
		
		HttpHeaderParser httpHeaderParser = mock(HttpHeaderParser.class);
		
		
		HttpRequestInputStreamReader reader = new HttpRequestInputStreamReaderImpl(requestLineParser, httpHeaderParser);
		
		StringBuilder sb = new StringBuilder ();
		sb.append("GET /index.html HTTP/1.1\r\n");
		sb.append("Host: example.org\r\n");
		sb.append("User-Agent: MyUserAgent\r\n");
		sb.append("AnyHeaderLine\r\n");
		sb.append("\r\n");

		ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
		
		
		HttpRequest req = reader.read(in);

		
		
		verify(requestLineParser).parse("GET /index.html HTTP/1.1", req);
		verify(httpHeaderParser).parse("Host: example.org", req.getHttpHeaders());
		verify(httpHeaderParser).parse("User-Agent: MyUserAgent", req.getHttpHeaders());
		verify(httpHeaderParser).parse("AnyHeaderLine", req.getHttpHeaders());

		assertEquals("GET /index.html HTTP/1.1",req.getRequestLine());
	
	}

	@Test
	public void testPost() throws IOException {

		RequestLineParser requestLineParser = mock(RequestLineParser.class);
		HttpHeaderParser httpHeaderParser = mock(HttpHeaderParser.class);

		doAnswer(new Answer<Object>() {
			
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {

				final HttpHeaders request = (HttpHeaders) (invocation.getArguments())[1];
				request.setContentLength(43);
				return null;
			}
			
		}).when(httpHeaderParser).parse(eq("Content-Length: 43"), any(HttpHeaders.class));
		
		HttpRequestInputStreamReader reader = new HttpRequestInputStreamReaderImpl(requestLineParser, httpHeaderParser);
		
		StringBuilder sb = new StringBuilder ();
		sb.append("POST /foo.php HTTP/1.1\r\n");
		sb.append("Content-Length: 43\r\n");
		sb.append("\r\n");
		sb.append("first_name=John&last_name=Doe&action=Submit");

		ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
		HttpRequest req = reader.read(in);

		
		
		verify(requestLineParser).parse("POST /foo.php HTTP/1.1", req);
		verify(httpHeaderParser).parse("Content-Length: 43", req.getHttpHeaders());

		assertEquals("POST /foo.php HTTP/1.1",req.getRequestLine());
		assertEquals(43, req.getHttpHeaders().getContentLength());
		assertEquals(43, req.getContent().length);
		assertEquals("first_name=John&last_name=Doe&action=Submit", new String(req.getContent()));

	}
	
	
}
