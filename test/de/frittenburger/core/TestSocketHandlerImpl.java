package de.frittenburger.core;
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
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.InetAddress;

import de.frittenburger.core.impl.SocketHandlerImpl;
import de.frittenburger.core.interfaces.ConnectionPool;
import de.frittenburger.core.interfaces.SocketHandler;
import de.frittenburger.firewall.interfaces.Firewall;
import de.frittenburger.io.interfaces.Connection;
import de.frittenburger.io.interfaces.SocketWrapper;
import de.frittenburger.tracking.interfaces.Tracking;

public class TestSocketHandlerImpl {

	@Test
	public void testHandleNewConnection() throws IOException {
		
		InetAddress inetAddress = InetAddress.getByName("127.0.0.1");

		Connection client = mock(Connection.class); 
		
		ConnectionPool clientPool = mock(ConnectionPool.class);
		when(clientPool.getConnection()).thenReturn(client);
		when(clientPool.size()).thenReturn(1);
		when(clientPool.getUsedConnectionCnt()).thenReturn(1);
		
		Firewall firewall = mock(Firewall.class);
		when(firewall.deny(inetAddress)).thenReturn(false);
		
		
		Tracking tracking = mock(Tracking.class);

		SocketHandler handler = new SocketHandlerImpl(clientPool,firewall,tracking);
		
		
		SocketWrapper socket = mock(SocketWrapper.class);
		when(socket.getInetAddress()).thenReturn(inetAddress);
		
		
		handler.handle(socket);

		verify(client).newConnection(socket);
		verify(tracking).usingConnectionPoolEvent(100);

		
	}

	
	@Test
	public void testHandleBusy() throws IOException {
		
		InetAddress inetAddress = InetAddress.getByName("127.0.0.1");

		ConnectionPool clientPool = mock(ConnectionPool.class);
		when(clientPool.getConnection()).thenReturn(null);
		when(clientPool.size()).thenReturn(1);

		Firewall firewall = mock(Firewall.class);
		when(firewall.deny(inetAddress)).thenReturn(false);
		
		
		Tracking tracking = mock(Tracking.class);

		SocketHandler handler = new SocketHandlerImpl(clientPool,firewall,tracking);
		
		
		SocketWrapper socket = mock(SocketWrapper.class);
		when(socket.getInetAddress()).thenReturn(inetAddress);
		
		
		handler.handle(socket);

		verify(socket).close();
		verify(tracking).busyConnectionPoolEvent();

		
	}

	@Test
	public void testHandleDeny() throws IOException {
		
		InetAddress inetAddress = InetAddress.getByName("127.0.0.1");


		Firewall firewall = mock(Firewall.class);
		when(firewall.deny(inetAddress)).thenReturn(true);
		
		
		Tracking tracking = mock(Tracking.class);

		SocketHandler handler = new SocketHandlerImpl(null,firewall,tracking);
		
		
		SocketWrapper socket = mock(SocketWrapper.class);
		when(socket.getInetAddress()).thenReturn(inetAddress);
		
		handler.handle(socket);

		verify(socket).close();
		verify(tracking).denyEvent(inetAddress);

	}

	
	
}
