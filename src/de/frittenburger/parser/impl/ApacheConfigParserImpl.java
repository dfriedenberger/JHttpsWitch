package de.frittenburger.parser.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.frittenburger.io.interfaces.PersistenceService;
import de.frittenburger.parser.interfaces.ApacheConfigParser;

public class ApacheConfigParserImpl implements ApacheConfigParser {

	private final PersistenceService persistenceService;
	
	public ApacheConfigParserImpl(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}
	
	@Override
	public Map<String, String> parse(String filename) throws IOException {

		
		 Pattern start = Pattern.compile("<VirtualHost\\s+(.+):(\\d+)>");
		 Pattern server = Pattern.compile("ServerName\\s+([^\\s]+)");
		 Pattern sslengine = Pattern.compile("SSLEngine\\s+([^\\s]+)");

		 Pattern stop = Pattern.compile("</VirtualHost>");

		 int port = -1;
		 boolean ssl = false;
		 List<String> servernames = null;
		 boolean virtualhost = false;
		Map<String, String> map = new HashMap<String,String>();
		for(String line : persistenceService.readLines(filename))
		{
	
		        Matcher matcherStart = start.matcher(line);
		        if(matcherStart.find()) {
		            port = Integer.parseInt(matcherStart.group(2));
		            ssl = false;
		            servernames = new ArrayList<String>();
		            virtualhost = true;
		            continue;
		        }
		        if(!virtualhost) continue;
		        
		        Matcher matcherStop = stop.matcher(line);
		        if(matcherStop.find()) {
		        	
		        	for(String servername : servernames)
		        	{
		        		map.put(servername, (ssl ? "https" : "http") + "://localhost:"+port);
		        	}
		        	
		            virtualhost = false;
		            continue;
		        }
		        
		        Matcher matcherServer = server.matcher(line);
		        if(matcherServer.find()) {
		        	servernames.add(matcherServer.group(1));
		        	continue;
		        }
		        
		        Matcher matcherSslengine = sslengine.matcher(line);
		        if(matcherSslengine.find()) {
		        	if(matcherSslengine.group(1).toLowerCase().equals("on"))
		        		ssl = true;
		        	continue;
		        }

			
		}
		//
		
		return map;
	}

}
