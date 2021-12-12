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
import java.net.InetAddress;

import org.apache.log4j.Logger;

import de.frittenburger.core.interfaces.TargetHandler;
import de.frittenburger.core.interfaces.WebsocketHandler;
import de.frittenburger.core.bo.Protocol;
import de.frittenburger.core.interfaces.StreamHandler;
import de.frittenburger.firewall.interfaces.Firewall;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.impl.HttpResponseBuilder;
import de.frittenburger.io.interfaces.HttpRequestInputStreamReader;
import de.frittenburger.io.interfaces.HttpResponseOutputStreamWriter;
import de.frittenburger.routing.bo.Target;
import de.frittenburger.routing.interfaces.Routing;
import de.frittenburger.tracking.interfaces.Tracking;

public class StreamHandlerImpl implements StreamHandler {

	private final Logger logger = Logger.getLogger(this.getClass());

	private final HttpRequestInputStreamReader httpInputStreamReader;
	private final HttpResponseOutputStreamWriter httpOutputStreamWriter;
	
	private final TargetHandler requestHandler;
	private final WebsocketHandler websocketHandler;
	private final Firewall firewall;
	private final Tracking tracking;
	private final Routing routing;


	public StreamHandlerImpl(TargetHandler requestHandler,WebsocketHandler websocketHandler,Firewall firewall,Tracking tracking,Routing routing,HttpRequestInputStreamReader httpInputStreamReader,HttpResponseOutputStreamWriter httpOutputStreamWriter)
	{
		this.requestHandler = requestHandler;
		this.websocketHandler = websocketHandler;
		this.firewall = firewall;
		this.tracking = tracking;
		this.routing = routing;
		this.httpInputStreamReader = httpInputStreamReader;
		this.httpOutputStreamWriter = httpOutputStreamWriter;
	}
	
	@Override
	public void handle(Protocol protocol,InetAddress inetAddress,InputStream in, OutputStream out) throws IOException, IllegalArgumentException, SecurityException, ReflectiveOperationException {
		
		while (true) {
		
			//Read Request
			logger.debug("Waiting for request");
			HttpResponse res = null;
			HttpRequest req = httpInputStreamReader.read(in);
			logger.info(req);

        	//RemoteRequest
	        try {
	        	
	        	Target target = routing.getTarget(protocol,req);
	        	
				if(firewall.deny(req.getHttpHeaders().getHost(),req.getUrl()))
				{
					logger.error("access denied");
					res = Builder.custom(HttpResponseBuilder.class).configureFirewallBlocked().build();
				}
				else if(target == null)
				{
					logger.error("target not found");
					res = Builder.custom(HttpResponseBuilder.class).configureTargetNotFound(req.getHttpHeaders().getHost()).build();
				}
				else if(req.getHttpHeaders().isWebsocket())
				{
					logger.info("handle as Websocket");

					websocketHandler.handle(target,req,in,out);
					//websocket is closed
					break;
				}
				else
				{
					res = requestHandler.handle(target,req);
				}
				
	        } catch (Exception e) {
	        	
	        	logger.error(e);
				res = Builder.custom(HttpResponseBuilder.class).configureException().build();
	        }
	        
	        httpOutputStreamWriter.write(out,res);
	        //logger.debug("Response written "+time());

	        
	        //Tracking
        	tracking.requestEvent(inetAddress,req.getHttpHeaders().getHost(),
        			req.getUrl(),
        			req.getHttpHeaders().getReferer(),
        			req.getHttpHeaders().getAgent(),
        			res.getStatus(),res.getHttpHeaders().getContentType());
	        
			if(!req.getHttpHeaders().isKeepAlive()) break;
			if(!res.getHttpHeaders().isKeepAlive()) break;
		}
	}

}
