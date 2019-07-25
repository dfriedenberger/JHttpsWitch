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

import de.frittenburger.cache.interfaces.Cache;
import de.frittenburger.core.bo.Protocol;
import de.frittenburger.core.impl.StreamHandlerImpl;
import de.frittenburger.core.interfaces.TargetHandler;
import de.frittenburger.core.interfaces.StreamHandler;
import de.frittenburger.firewall.interfaces.Firewall;
import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.interfaces.HttpRequestInputStreamReader;
import de.frittenburger.io.interfaces.HttpResponseOutputStreamWriter;
import de.frittenburger.routing.bo.Target;
import de.frittenburger.routing.interfaces.Routing;
import de.frittenburger.tracking.interfaces.Tracking;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.security.GeneralSecurityException;


		
public class TestStreamHandlerImpl {

	@Test
	public void test() throws IOException, RuntimeException, ReflectiveOperationException, GeneralSecurityException {
		
		HttpRequest req = new HttpRequest();
		
		HttpHeaders headers = req.getHttpHeaders();
		
		headers.setHost("host");
        req.setUrl("url");
        headers.setReferer("referer");
        headers.setAgent("agent");

		HttpResponse res = new HttpResponse();
		res.getHttpHeaders().setContentType("contentType");
		res.setStatus(200);
        
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        
        
		InputStream in = new ByteArrayInputStream(new byte[0]);
		OutputStream out = new ByteArrayOutputStream();
		TargetHandler requestHandler = mock(TargetHandler.class);
		
	    Target target =  mock(Target.class); 
		
		Routing routing = mock(Routing.class);
		when(routing.getTarget(Protocol.HTTPS,req)).thenReturn(target);
		
		when(requestHandler.handle(eq(target), eq(req))).thenReturn(res);
		Firewall firewall = mock(Firewall.class);
		when(firewall.deny("host", "url")).thenReturn(false);
		
		Tracking tracking = mock(Tracking.class);
		
		Cache cache = mock(Cache.class);

		
		HttpRequestInputStreamReader httpInputStreamReader = mock(HttpRequestInputStreamReader.class);
		when(httpInputStreamReader.read(in)).thenReturn(req);
		HttpResponseOutputStreamWriter httpOutputStreamWriter = mock(HttpResponseOutputStreamWriter.class);
		
		StreamHandler handler = new StreamHandlerImpl(requestHandler, firewall, tracking, routing,cache, httpInputStreamReader,httpOutputStreamWriter);
		handler.handle(Protocol.HTTPS,addr,in, out);
		
		verify(tracking).requestEvent(addr,"host", "url", "referer", "agent",200,"contentType");
		verify(requestHandler).handle(eq(target), eq(req));

	}

}
