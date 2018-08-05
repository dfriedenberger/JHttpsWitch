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

import de.frittenburger.core.impl.ServerImpl;
import de.frittenburger.core.interfaces.Server;
import de.frittenburger.core.interfaces.SocketHandler;
import de.frittenburger.io.interfaces.ServerSocketWrapper;
import de.frittenburger.io.interfaces.SocketWrapper;

import static org.mockito.Mockito.*;

import java.io.IOException;

public class TestServerImpl {

	@Test
	public void test() throws IOException, InterruptedException {
		
		
		SocketHandler sockethandler = mock(SocketHandler.class);
		
		SocketWrapper clientsocket = mock(SocketWrapper.class);
		ServerSocketWrapper serversocket = mock(ServerSocketWrapper.class);
		
		when(serversocket.accept())
			.thenReturn(clientsocket)
			.thenThrow(new RuntimeException());

		Server server = new ServerImpl(serversocket,sockethandler);
		server.start();
		
		
		((Thread)server).join();
		verify(sockethandler).handle(clientsocket);
	}

	
	@Test
	public void testIOException() throws IOException, InterruptedException {
		
		
		SocketHandler sockethandler = mock(SocketHandler.class);
		
		ServerSocketWrapper serversocket = mock(ServerSocketWrapper.class);
		
		when(serversocket.accept())
			.thenThrow(new IOException())
			.thenThrow(new RuntimeException());

		Server server = new ServerImpl(serversocket,sockethandler);
		server.start();
		
		
		((Thread)server).join();
		verify(sockethandler, never()).handle(any());
	}

	
}
