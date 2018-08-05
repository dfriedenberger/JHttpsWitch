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

import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.interfaces.HttpResponseOutputStreamWriter;

public class HttpResponseOutputStreamWriterImpl extends HttpOutputStreamWriterImpl implements HttpResponseOutputStreamWriter {

	

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void write(OutputStream out, HttpResponse res) throws IOException {

		
		
		//status Line
		super.writeLine(out,"HTTP/1.1 "+res.getStatus()+" "+getStatusText(res.getStatus()));
		
		//headerLine
		super.writeHeaders(out, res.getHttpHeaders());
		
		//empty line
		super.writeEmptyLine(out);
		
		//data
		super.writeContent(out, res.getContent());
	
		//trailer
		if(res.getHttpHeaders().isChunkedTransferEncoding())
		{
			super.writeHeaders(out, res.getHttpTrailers());
			//empty line
			super.writeEmptyLine(out);
		}
		logger.debug("ready");
		
	}

	private String getStatusText(int status) {
		switch(status)
		{
			case 200: return "Ok"; 
			case 301: return "Moved Permanently"; 

			
			
			case 403: return "Forbidden"; 
			case 404: return "Not Found"; 
			case 503: return "Service unavailable"; 
			default: return " status="+status; 

		}
	}

}
