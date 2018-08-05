package de.frittenburger.io;
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

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.core.interfaces.StreamHandler;
import de.frittenburger.io.impl.ConnectionImpl;
import de.frittenburger.io.interfaces.SocketWrapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public class TestConnectionImpl {

	@Test
	public void test() throws IOException, InterruptedException, RuntimeException, ReflectiveOperationException {
		
		InputStream in = new ByteArrayInputStream(new byte[0]);
		OutputStream out = new ByteArrayOutputStream();
		InetAddress addr = InetAddress.getByName("127.0.0.1");
		StreamHandler streamHandler = mock(StreamHandler.class);
		
		SocketWrapper socket = mock(SocketWrapper.class);
		when(socket.getInputStream()).thenReturn(in);
		when(socket.getOutputStream()).thenReturn(out);
		when(socket.getProtocol()).thenReturn(Protocol.HTTPS);
		when(socket.getInetAddress()).thenReturn(addr);

		ConnectionImpl handler = new ConnectionImpl(streamHandler);
		
		handler.start();
		Thread.sleep(100); //wait  (ob.wait)
		handler.lock().newConnection(socket);
		
		for(int i = 0;i < 30;i++)
		{
			if(handler.isAvailable()) break;
			Thread.sleep(100);
		}
		
		assertTrue(handler.isAvailable());
		verify(streamHandler).handle(Protocol.HTTPS,addr,in, out);
	}

}
