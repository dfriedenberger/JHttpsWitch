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
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.security.GeneralSecurityException;

import org.junit.Test;

import de.frittenburger.core.impl.Builder;
import de.frittenburger.core.impl.ClientBuilder;
import de.frittenburger.core.impl.TargetHandlerImpl;
import de.frittenburger.core.interfaces.Client;
import de.frittenburger.core.interfaces.TargetHandler;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.routing.bo.Target;

public class TestTargetHandlerImpl {

	@Test
	public void testRedirect() throws RuntimeException, IOException, ReflectiveOperationException, GeneralSecurityException {
		
		
		TargetHandler handler = new TargetHandlerImpl();
		Target target = new Target();
		target.setRedirect(true);
		target.setUrl("https://www.example.org");

		HttpRequest req = new HttpRequest();
		
		HttpResponse res = handler.handle(target, req);
		
		assertEquals(301,res.getStatus()); //Moved Permanent
		assertEquals(1,res.getHttpHeaders().getHeaders().size());
		assertEquals("Location: https://www.example.org",res.getHttpHeaders().getHeaders().get(0));
		
	}
	
	@Test
	public void testInternalHandling() throws RuntimeException, IOException, ReflectiveOperationException, GeneralSecurityException {
		
		TargetHandler handler = new TargetHandlerImpl();
		Target target = new Target();
		target.setRedirect(false);
		target.setUrl("https://localhost:7777");
		HttpResponse res = new HttpResponse();
		Client client = mock(Client.class);
		when(client.read()).thenReturn(res);
		ClientBuilder clientBuilder = mock(ClientBuilder.class);
		Builder.registerOne(clientBuilder);
		when(clientBuilder.configure(any(URL.class))).thenReturn(clientBuilder);
		when(clientBuilder.build()).thenReturn(client);

		
		HttpRequest req = new HttpRequest();
		HttpResponse result = handler.handle(target, req);
		
		assertEquals(res, result);
	    verify(client).connect();
	    verify(client).write(req);
	    verify(client).read();
	    verify(client).disconnect();

	}
	
	@Test
	public void testConnectExceptionHandling() throws RuntimeException, IOException, ReflectiveOperationException, GeneralSecurityException {
		
		TargetHandler handler = new TargetHandlerImpl();
		Target target = new Target();
		target.setRedirect(false);
		target.setUrl("https://localhost:7777");
	
		Client client = mock(Client.class);
		
		doThrow(new ConnectException()).when(client).connect();
		
		ClientBuilder clientBuilder = mock(ClientBuilder.class);
		Builder.registerOne(clientBuilder);
		when(clientBuilder.configure(any(URL.class))).thenReturn(clientBuilder);
		when(clientBuilder.build()).thenReturn(client);

		
		HttpRequest req = new HttpRequest();
		HttpResponse result = handler.handle(target, req);
		
		assertEquals(503, result.getStatus());
	    verify(client).connect();
	 
	}
}
