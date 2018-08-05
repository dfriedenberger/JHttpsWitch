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

import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.interfaces.HttpRequestOutputStreamWriter;

public class HttpRequestOutputStreamWriterImpl extends HttpOutputStreamWriterImpl 
	implements HttpRequestOutputStreamWriter {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void write(HttpRequest req, OutputStream out) throws IOException {

		//status Line
		super.writeLine(out,req.getRequestLine());
		
		//headerLine
		super.writeHeaders(out,req.getHttpHeaders());
	
		//empty line
		super.writeEmptyLine(out);
		
		//data
		super.writeContent(out,req.getContent());
	
		
		logger.debug("ready");
		
		
	}

	

}
