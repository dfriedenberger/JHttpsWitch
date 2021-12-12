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
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.core.interfaces.ConnectionPool;
import de.frittenburger.core.interfaces.SocketHandler;
import de.frittenburger.firewall.interfaces.Firewall;
import de.frittenburger.io.interfaces.Connection;
import de.frittenburger.io.interfaces.SocketWrapper;
import de.frittenburger.tracking.interfaces.Tracking;

public class SocketHandlerImpl implements SocketHandler {

	private final Logger logger = LogManager.getLogger(this.getClass());
	private final ConnectionPool connectionpool;
	private final Firewall firewall;
	private final Tracking tracking;

	public SocketHandlerImpl(ConnectionPool clientpool,Firewall firewall,Tracking tracking)
	{
		this.connectionpool = clientpool;
		this.firewall = firewall;
		this.tracking = tracking;
	}

	@Override
	public void handle(SocketWrapper socket) throws IOException {
		

		InetAddress addr = socket.getInetAddress();
		
		// here we can decide to denied connection
		if(firewall.deny(addr))
		{
			tracking.denyEvent(addr);
			socket.close();
			return;
		}
		
		// get Client for Request
		Connection jhc = connectionpool.getConnection();
		int used = connectionpool.getUsedConnectionCnt();
		logger.debug("ConnectionPool used="+used+" size="+connectionpool.size());
		tracking.usingConnectionPoolEvent((used * 100) / connectionpool.size());
		if (jhc == null) {
			tracking.busyConnectionPoolEvent();
			socket.close();
			return;
		} 
		jhc.newConnection(socket);
		
	}

	
	
}
