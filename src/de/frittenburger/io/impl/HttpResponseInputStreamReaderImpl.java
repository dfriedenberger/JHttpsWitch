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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.interfaces.HttpResponseInputStreamReader;
import de.frittenburger.parser.interfaces.HttpHeaderParser;
import de.frittenburger.parser.interfaces.ResponseLineParser;

public class HttpResponseInputStreamReaderImpl extends HttpInputStreamReaderImpl
	implements HttpResponseInputStreamReader {
	

	private final Logger logger = LogManager.getLogger(this.getClass());
	private ResponseLineParser responseLineParser;
	
	public HttpResponseInputStreamReaderImpl(ResponseLineParser responseLineParser, HttpHeaderParser httpHeaderParser)
	{
		super(httpHeaderParser);
		this.responseLineParser = responseLineParser;
	}
	
	
	@Override
	public HttpResponse read(InputStream in) throws IOException {
		HttpResponse res = new HttpResponse();
		// read request line
		String requestline = super.readLine(in);
		logger.debug(requestline);
		res.setResponseLine(requestline);
		responseLineParser.parse(requestline, res);

		// Read HttpHeader
		super.readHeaders(in,res.getHttpHeaders());
	
		
	
		
		// Read Body if Post or etc
		int len = res.getHttpHeaders().getContentLength();
		if (len > 0) {
			byte data[] = super.readBody(in, len);
			res.setContent(data);
		} 
		
		//get chunked
		if(res.getHttpHeaders().isChunkedTransferEncoding())
		{
			byte data[] = super.readChunkedBody(in);
			res.setContent(data);
		
			
			// Read Tailer
			super.readHeaders(in,res.getHttpTrailers());
		}
		
		return res;
	}

}
