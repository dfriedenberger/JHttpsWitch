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
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.io.interfaces.HttpConstants;
import de.frittenburger.io.interfaces.SocketWrapper;

public class SocketWrapperImpl implements SocketWrapper, HttpConstants  {

	private final Logger logger = LogManager.getLogger(this.getClass());

	private final Socket socket;
	private final Protocol protocol;

	public SocketWrapperImpl(Socket socket, Protocol protocol) {
		this.socket = socket;
		try {
			logger.debug("configure socket timeout to " + SO_TIMEOUT);
			this.socket.setSoTimeout(SO_TIMEOUT);
		} catch (SocketException e) {
			logger.error(e);
		}
		this.protocol = protocol;
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@Override
	public Protocol getProtocol() {
		return protocol;
	}
	
	

	@Override
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	
	@Override
	public String toString() {
		return socket.getInetAddress()+":"+socket.getPort() + " (" + protocol + ")";
	}

	@Override
	public void connect(InetSocketAddress addr) throws IOException {
		logger.debug("connect with timeout " + CONNECT_TIMEOUT);
		socket.connect(addr,CONNECT_TIMEOUT);
	}
	
}
