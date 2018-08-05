package de.frittenburger.io.impl;
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
import java.io.InputStream;

import org.apache.log4j.Logger;

import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.interfaces.HttpConstants;
import de.frittenburger.io.interfaces.HttpRequestInputStreamReader;
import de.frittenburger.parser.interfaces.HttpHeaderParser;
import de.frittenburger.parser.interfaces.RequestLineParser;

public class HttpRequestInputStreamReaderImpl extends HttpInputStreamReaderImpl
		implements HttpRequestInputStreamReader , HttpConstants {

	private final Logger logger = Logger.getLogger(this.getClass());

	RequestLineParser requestLineParser = null;
	HttpHeaderParser httpHeaderParser = null;

	public HttpRequestInputStreamReaderImpl(RequestLineParser requestLineParser, HttpHeaderParser httpHeaderParser) {
		super(httpHeaderParser);
		this.requestLineParser = requestLineParser;
	}

	@Override
	public HttpRequest read(InputStream in) throws IOException {

		HttpRequest req = new HttpRequest();
		// read request line
		String requestline = super.readLine(in);
		logger.debug(requestline);
		req.setRequestLine(requestline);
		requestLineParser.parse(requestline, req);

		// Read HttpHeader
		super.readHeaders(in,req.getHttpHeaders());
		

		// Read Body if Post or etc
		int len = req.getHttpHeaders().getContentLength();
		if (len > 0) {
			byte data[] = super.readBody(in, len);
			req.setContent(data);
		}

		return req;
	}

}
