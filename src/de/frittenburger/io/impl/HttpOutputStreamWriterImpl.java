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
import java.io.OutputStream;

import org.apache.log4j.Logger;

import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.io.interfaces.HttpConstants;

public class HttpOutputStreamWriterImpl implements HttpConstants  {
	private final Logger logger = Logger.getLogger(this.getClass());

	public void writeLine(OutputStream out, String line) throws IOException {
		
		logger.debug(line);
		out.write((line+ "\r\n").getBytes());	
		
	}

	public void writeHeaders(OutputStream out, HttpHeaders httpHeaders) throws IOException {
		
		for(String header : httpHeaders.getHeaders())
		{
			writeLine(out,header);
		}
		
	}

	public void writeEmptyLine(OutputStream out) throws IOException {
		writeLine(out,"");
		out.flush();		
	}

	public void writeContent(OutputStream out, byte[] data) throws IOException {
		
		if(data == null) return;
		int len = data.length;
		if (len <= 0) return;
		
		
		logger.debug("write "+len+" Bytes");
		int offset = 0;
		while(offset < len)
		{
			int rlen = len - offset;
			if(rlen > SENDSIZE) rlen = SENDSIZE;
			logger.debug("Send "+rlen+" Bytes");
			out.write(data,offset,rlen);
			offset += rlen;
		}
		out.flush();
		
	}

}
