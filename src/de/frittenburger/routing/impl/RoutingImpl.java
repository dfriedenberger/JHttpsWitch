package de.frittenburger.routing.impl;
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
import java.util.List;

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.routing.bo.RoutingConfig;
import de.frittenburger.routing.bo.Target;
import de.frittenburger.routing.interfaces.Matcher;
import de.frittenburger.routing.interfaces.Routing;

public class RoutingImpl implements Routing {

	private List<RoutingConfig> routingconfiguration;
	private Matcher matcher;

	public RoutingImpl(Matcher matcher)
	{
		this.matcher = matcher;
	}
	
	@Override
	public void load(List<RoutingConfig> routingconfiguration) {
		this.routingconfiguration = routingconfiguration;
	}

	@Override
	public Target getTarget(Protocol protocol,HttpRequest req) {


		for(RoutingConfig routing : routingconfiguration)
		{
			if(!matcher.match(routing.getHosts(),req.getHttpHeaders().getHost())) continue;
			if(!matcher.match(routing.getUrls(),req.getUrl())) continue;

			Target target = new Target();
			//Wenn ssl Pflicht ist, ein redirect
			if(routing.isSslEnabled() && protocol != Protocol.HTTPS) 
			{
				target.setRedirect(true);
				
				String host = req.getHttpHeaders().getHost();
				if(host.toLowerCase().startsWith("www."))
					host = host.substring(4);
				
				target.setUrl("https://"+host);
				return target;
			}
			
			target.setRedirect(false);
			target.setUrl(routing.getTarget());
			return target;
		}
		
		return null;
	}

	
}
