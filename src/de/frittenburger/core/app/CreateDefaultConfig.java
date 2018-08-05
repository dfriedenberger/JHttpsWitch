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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.core.bo.ServerConfig;
import de.frittenburger.parser.bo.UserAgent;
import de.frittenburger.routing.bo.RoutingConfig;
import de.frittenburger.ssl.bo.LetsEncryptConfig;

public class CreateDefaultConfig {

	
	
	public static void genServerConfig(String ip,String filename) throws IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		List<ServerConfig> serverconfiguration = new ArrayList<ServerConfig>();
		
		ServerConfig http = new ServerConfig();
		http.setHost(ip);
		http.setPort(80);
		http.setProtocol(Protocol.HTTP);
		serverconfiguration.add(http);
		
		ServerConfig https = new ServerConfig();
		https.setHost(ip);
		https.setPort(443);
		https.setProtocol(Protocol.HTTPS);
		serverconfiguration.add(https);
		
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), serverconfiguration);

	}
	public static void genLetsEncryptConfig(String path,String filename) throws IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		LetsEncryptConfig letsEncryptConfig = new LetsEncryptConfig(); 
		letsEncryptConfig.setLivePath(path);
		
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), letsEncryptConfig);
		
	}
	
	public static void genRoutingConfig(Map<String, List<String>> targets,String filename) throws IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		List<RoutingConfig> routingconfiguration = new ArrayList<RoutingConfig>();
		for(String target : targets.keySet())
		{
			RoutingConfig config = new RoutingConfig();
			config.setSslEnabled(true);
			config.getHosts().addAll(targets.get(target));
			config.getUrls().add("*");
			config.setTarget(target);
			routingconfiguration.add(config);
		}
	
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), routingconfiguration);

	}
	
	
	public static void genUserAgent(String filename) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		UserAgent[] agents = new UserAgent[] {

				UserAgent.create(new String[]{
						"[Bb]ot|index|[Ss]pider|[Cc]rawl|wget|slurp",
						"Mediapartners-Google|AppEngine-Google",
						"nagios-plugins|facebookexternalhit|feedzirra|Nutch",
						"Serverstate User Agent",
						"Riddler|Seobility|360Spider",
						"InternetSeer.com",
						"^Java/[0-9._]+$",
						"Morfeus Fucking Scanner",
						"BingPreview"
				}, true, "Bot"),

				UserAgent.create(new String[]{
						"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) Chrome/[0-9.]+ Safari/[0-9.]+$"
				}, false ,"Google Chrome"),
				
				UserAgent.create(new String[]{
						"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) Version/[0-9.]+ Safari/[0-9.]+$"
				}, false ,"Safari"),
				
				UserAgent.create(new String[]{
						"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) Chrome/[0-9.]+ Mobile Safari/[0-9.]+"
				}, false ,"Mobile Chrome"),
				
				UserAgent.create(new String[]{
						"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) [a-zA-Z]+/[0-9.]+ Mobile[/0-9a-zA-Z.]* Safari/[0-9.]+"
				}, false ,"Mobile Safari"),
				
				UserAgent.create(new String[]{
						"^Mozilla/5.0 \\(.+\\) Gecko(/[0-9]+){0,1} Firefox/[0-9a-z.]+$",
						"^Mozilla/5.0 \\(.+\\) Gecko(/[0-9]+){0,1} Firefox/[0-9a-z.]+ \\((.NET CLR [0-9.; ]+)+\\)$",
						"^Mozilla/5.0 \\(.+\\) Gecko(/[0-9]+){0,1} Firefox/[0-9a-z.]+ Paros/[0-9.]+$",
						"^Mozilla/5.0 \\(.+\\) Gecko/[0-9]+ Ubuntu/[0-9.]+ \\([a-zA-Z]+\\) Firefox/[0-9a-z.]+$",
						"^Mozilla/5.0 \\(.+\\) Gecko/[0-9]+ Ubuntu/[0-9.]+ \\([a-zA-Z]+\\) Firefox/[0-9a-z.]+$",
				}, false ,"Firefox"),

				UserAgent.create(new String[]{
						"^Opera/[0-9.]+ \\(.+\\) Presto/[0-9.]+ Version/[0-9.]+$"
				}, false ,"Opera"),
				
				UserAgent.create(new String[]{
						"^Mozilla/[0-9.]+ \\(compatible; MSIE [0-9.]+; Windows"
				}, false ,"Internet Explorer"),
				
				UserAgent.create(new String[]{
						"Mozilla/5.0 \\(.+\\) like Gecko$"
				}, false ,"Internet Explorer"),

				UserAgent.create(new String[]{
						"^(Mozilla.*(Gecko|KHTML|MSIE|Presto|Trident)|Opera).*$"
				}, false , "Browsers")

			};
		
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), Arrays.asList(agents));

		
	}
	
	public static void main(String[] args) throws IOException {

				
		//Server
		genServerConfig("127.0.0.1","config/server.json.template");
		
		//letsEncrypt
		genLetsEncryptConfig("/etc/letsencrypt/live","config/letsencrypt.json.template");
		

		//Routing
		Map<String,List<String>> targets = new HashMap<String,List<String>>();
		 
		targets.put("http://localhost:8080", Arrays.asList(new String[]{"example.org","www.example.org"}));
		targets.put("http://localhost:9090", Arrays.asList(new String[]{"*"}));
		genRoutingConfig(targets,"config/routing.json.template");
	
		//UserAgents
		genUserAgent("config/useragent.json.template");
		
		
	}
	

}
