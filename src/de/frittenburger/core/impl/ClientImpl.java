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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.core.interfaces.Client;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.interfaces.HttpRequestOutputStreamWriter;
import de.frittenburger.io.interfaces.HttpResponseInputStreamReader;
import de.frittenburger.io.interfaces.SocketWrapper;

public class ClientImpl implements Client {
	
    private  final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	private SocketWrapper socket;
	private InetSocketAddress addr;
	private OutputStream out;
	private InputStream in;

	private HttpRequestOutputStreamWriter httpRequestOutputStreamWriter;
	private HttpResponseInputStreamReader httpResponseInputStreamReader;

	public ClientImpl(SocketWrapper socket, String host, int port,
			HttpRequestOutputStreamWriter httpRequestOutputStreamWriter,
			HttpResponseInputStreamReader httpResponseInputStreamReader)  {
		this.socket = socket;
		this.addr = new InetSocketAddress(host, port);
		this.httpRequestOutputStreamWriter = httpRequestOutputStreamWriter;
		this.httpResponseInputStreamReader = httpResponseInputStreamReader;
	}

	@Override
	public void connect() throws IOException {
		
		logger.debug("Connect to "+addr);
		socket.connect(addr);
		out = socket.getOutputStream();
		in = socket.getInputStream();
		
	}

	@Override
	public void write(HttpRequest req) throws IOException {
		httpRequestOutputStreamWriter.write(req,out);
	}

	@Override
	public HttpResponse read() throws IOException {
		return httpResponseInputStreamReader.read(in);
	}

	@Override
	public void disconnect() {
		
		try {
			if(in != null)
				in.close();
		} catch (Exception e) {
			logger.warn(e);
		}
		finally
		{
			in = null;
		}
		
		
		try {
			if(out != null)
				out.close();
		} catch (Exception e) {
			logger.warn(e);
		}
		finally
		{
			out = null;
		}

		try {
			if(socket != null)
				socket.close();
		} catch (Exception e) {
			logger.warn(e);
		}
		finally
		{
			socket = null;
		}
		logger.debug("Disconnect from "+addr);
		
	}

}
