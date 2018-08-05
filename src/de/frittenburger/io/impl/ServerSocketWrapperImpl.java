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
import java.net.ServerSocket;
import java.net.Socket;

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.io.interfaces.ServerSocketWrapper;
import de.frittenburger.io.interfaces.SocketWrapper;

public class ServerSocketWrapperImpl implements ServerSocketWrapper {

	private final ServerSocket serverSocket;
	private final Protocol protocol;

	public ServerSocketWrapperImpl(ServerSocket serverSocket, Protocol protocol) {
		this.serverSocket = serverSocket;
		this.protocol = protocol;
	}

	@Override
	public SocketWrapper accept() throws IOException {
		Socket socket = serverSocket.accept();
		return new SocketWrapperImpl(socket,protocol);
	}

	@Override
	public String toString() {
		return serverSocket.getInetAddress()+":"+serverSocket.getLocalPort()+" ("+protocol+")";
	}

}
