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

import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.parser.impl.HttpHeaderParserImpl;
import de.frittenburger.parser.interfaces.HttpHeaderParser;

public class TestHttpHeaderParserImpl {


	@Test
	public void testParseHost() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("Host: example.org", headers);
		assertEquals("example.org", headers.getHost());
		
		assertEquals(1,headers.getHeaders().size());
		assertEquals("Host: example.org",headers.getHeaders().get(0));
		
		
	}

	
	@Test
	public void testParseAgent() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)", headers);
		assertEquals("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)", headers.getAgent());
	
		assertEquals(1,headers.getHeaders().size());
		assertEquals("User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)",headers.getHeaders().get(0));
	}
	
	@Test
	public void testParseReferer() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("Referer: http://localhost/test.php", headers);
		assertEquals("http://localhost/test.php", headers.getReferer());
		assertEquals(1,headers.getHeaders().size());
		assertEquals("Referer: http://localhost/test.php",headers.getHeaders().get(0));
		
	}
	
	
	@Test
	public void testParseKeepAlive() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("Connection: keep-alive", headers);
		assertTrue(headers.isKeepAlive());
		assertEquals(1,headers.getHeaders().size());
		assertEquals("Connection: keep-alive",headers.getHeaders().get(0));
	}
	
	@Test
	public void testIsExpect100Continue() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("Expect: 100-Continue", headers);
		assertTrue(headers.isExpect100Continue());
		assertEquals(1,headers.getHeaders().size());
		assertEquals("Expect: 100-Continue",headers.getHeaders().get(0));
	}
	
	@Test
	public void testIsChunkedTransferEncoding() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("Transfer-Encoding: chunked", headers);
		assertTrue(headers.isChunkedTransferEncoding());
		assertEquals(1,headers.getHeaders().size());
		assertEquals("Transfer-Encoding: chunked",headers.getHeaders().get(0));
	}
	
	@Test
	public void testContentType() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("Content-Type: text/html; charset=UTF-8", headers);
		assertEquals("UTF-8",headers.getCharset());
		assertEquals("text/html",headers.getContentType());
		assertEquals(1,headers.getHeaders().size());
		assertEquals("Content-Type: text/html; charset=UTF-8",headers.getHeaders().get(0));
		
	}
	
	@Test
	public void testDefaults() {
		
		HttpHeaderParser parser = new HttpHeaderParserImpl();
		HttpHeaders headers = new HttpHeaders();

		parser.parse("AnyHeader: AnyValue", headers);
		
		assertFalse(headers.isExpect100Continue());
		assertFalse(headers.isChunkedTransferEncoding());
		assertFalse(headers.isKeepAlive());

		assertEquals(1,headers.getHeaders().size());
		assertEquals("AnyHeader: AnyValue",headers.getHeaders().get(0));
	}
	
}
