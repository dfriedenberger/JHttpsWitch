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
import java.net.URL;
import java.security.GeneralSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.core.interfaces.Client;
import de.frittenburger.core.interfaces.TargetHandler;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.impl.HttpResponseBuilder;
import de.frittenburger.routing.bo.Target;

public class TargetHandlerImpl implements TargetHandler {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	
	@Override
	public HttpResponse handle(Target target, HttpRequest req) throws RuntimeException, IOException, ReflectiveOperationException, GeneralSecurityException {
		
		
		logger.info(target);

		if(target.isRedirect())
		{
			logger.debug("Redirect to "+target.getUrl());
			HttpResponse response = new HttpResponse();
			response.setPermanentRedirect(target.getUrl());
			return response;
		}
		
		//Connect
		logger.debug("Connect to "+target.getUrl());

	
		//parse Url
	
		
		Client client = Builder.custom(ClientBuilder.class).configure(new URL(target.getUrl())).build();
		
		// Nun auf entfernten Server zugreifen
		try
		{
			client.connect();
			
			client.write(req);
			
			return client.read();
		} 
		catch(IOException e)
		{
			logger.error(e);
			return Builder.custom(HttpResponseBuilder.class).configureException().build();
		}
		finally
		{
			client.disconnect();
		}
		
	}

}
