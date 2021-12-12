package de.frittenburger.core.impl;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.core.interfaces.Client;
import de.frittenburger.io.impl.HttpRequestOutputStreamWriterImpl;
import de.frittenburger.io.impl.HttpResponseInputStreamReaderImpl;
import de.frittenburger.io.interfaces.SocketWrapper;
import de.frittenburger.parser.impl.HttpHeaderParserImpl;
import de.frittenburger.parser.impl.ResponseLineParserImpl;
import de.frittenburger.ssl.impl.SocketBuilder;

public class ClientBuilder {

    private final Logger logger = LogManager.getLogger(this.getClass());

	private String host = null;
	private int port = -1;
	private Protocol protocol;
	

	public ClientBuilder configure(URL url) throws IOException {
		this.host = url.getHost();
		this.port = url.getPort();
		
		switch(url.getProtocol())
		{
		case "http":
			this.protocol = Protocol.HTTP;
			break;
		case "https":
			this.protocol = Protocol.HTTPS;
			break;
		default: 
			throw new MalformedURLException(url.toString());
		}
		
	
		
		return this;
	}

	public Client build() throws IOException, GeneralSecurityException, RuntimeException, RuntimeException, ReflectiveOperationException {
		
		SocketWrapper socket = null;
		switch(protocol)
		{
		case HTTP:			
			// Start HttpSocket on Port 80
			logger.debug("build Client for " + host + ":" + port);
			socket = Builder.custom(SocketBuilder.class).build();
			break;
		case HTTPS:
			// Start HttpsSocket on Port 443
			logger.debug("build SSL Client for " + host + ":"+port);
		    socket = Builder.custom(SocketBuilder.class).setSSLEnabled(true).build();
			break;
		default:
			throw new RuntimeException("not implemented "+protocol);
		}

		return new ClientImpl(socket,host,port, new HttpRequestOutputStreamWriterImpl(), 
				new HttpResponseInputStreamReaderImpl(new ResponseLineParserImpl(), new HttpHeaderParserImpl()));
		
	}

}
