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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.parser.interfaces.HttpHeaderParser;

public class HttpInputStreamReaderImpl {

	private final Logger logger = Logger.getLogger(this.getClass());
	private HttpHeaderParser httpHeaderParser;

	public HttpInputStreamReaderImpl(HttpHeaderParser httpHeaderParser) {
		this.httpHeaderParser = httpHeaderParser;
	}

	public String readLine(InputStream in) throws IOException {
		
		String line = null;
		while (true) {
			
			int i = in.read();
			
			if (i < 0)
				break;
			
			
			if (line == null)
				line = "";
			if (i == '\r')
				continue;
			if (i == '\n')
				break;
			line += (char) i;
		}
		
		if(line == null)
			throw new IOException("Connection broke / End of Stream ");
		
		return line;
	}

	public byte[] readBody(InputStream in, int len) throws IOException {

		byte data[] = new byte[len];

		int send = 0;
		while (send < len) {
			int r = in.read(data, send, len - send);
			if (r < 0)
				throw new IOException("Read " + r + " len: " + len + " send: " + send);
			send += r;
			logger.debug("r:" + r + " Read " + send + " of " + len + " Bytes");
		}
		
		logger.debug("Read " + send + " of " + len + " Bytes");
		return data;
	}

	public void readHeaders(InputStream in,HttpHeaders headers) throws IOException {
		
		while (true) {
			String line = readLine(in);
			logger.debug(line);
			if (line.equals(""))
				break;
			httpHeaderParser.parse(line, headers);
		}
		
	}

	public byte[] readChunkedBody(InputStream in) throws IOException {
		
		ByteArrayOutputStream data = new ByteArrayOutputStream( );
		
		
		while(true)
		{
			String line = readLine(in);
			data.write(line.getBytes());
			data.write("\r\n".getBytes());

			int len = Integer.parseInt(line,16);
			if(len == 0) break;
			byte d[] = readBody(in,len);
			data.write(d);
			
			String b = readLine(in);
			if(!b.equals(""))throw new IOException(b+" not expected");
			data.write("\r\n".getBytes());
		}

		return data.toByteArray();
	}

}
