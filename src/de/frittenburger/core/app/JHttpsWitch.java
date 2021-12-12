package de.frittenburger.core.app;
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
import java.io.File;
import java.util.List;

import org.apache.logging.log4j.core.LoggerContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.core.bo.ServerConfig;
import de.frittenburger.core.impl.Builder;
import de.frittenburger.core.impl.ServerBuilder;
import de.frittenburger.core.interfaces.Server;
import de.frittenburger.parser.bo.UserAgent;
import de.frittenburger.routing.bo.RoutingConfig;
import de.frittenburger.ssl.bo.LetsEncryptConfig;

public class JHttpsWitch {

	public static void main(String[] args) {

		try {
			
		     
			//DOMConfigurator.configureAndWatch("config/log4j.xml", 60 * 1000); //Version log4j v1.2
			LoggerContext.getContext().setConfigLocation(new File("config/log4j.xml").toURI());
			
			
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			List<ServerConfig> serverconfiguration = objectMapper.readValue(new File("config/server.json"), new TypeReference<List<ServerConfig>>(){});
			List<RoutingConfig> routingconfiguration = objectMapper.readValue(new File("config/routing.json"), new TypeReference<List<RoutingConfig>>(){});			
			LetsEncryptConfig letsEncryptConfig = objectMapper.readValue(new File("config/letsencrypt.json"), LetsEncryptConfig.class);
			
			List<UserAgent> useragents = objectMapper.readValue(new File("config/useragent.json"), new TypeReference<List<UserAgent>>(){});			

			
			ServerBuilder.init(routingconfiguration,useragents,"tracking", 60);
			for(ServerConfig config : serverconfiguration)
			{
				Server server = Builder.custom(ServerBuilder.class)
						.configure(config)
						.configureLetsEncrypt(letsEncryptConfig).build();
				server.start();
			}
			
			
			
			while (true) {
				//Do cycle stuff
				Thread.sleep(1000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
