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
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.core.bo.ServerConfig;
import de.frittenburger.core.interfaces.ConnectionPool;
import de.frittenburger.core.interfaces.Server;
import de.frittenburger.datawarehouse.impl.DataWarehouseImpl;
import de.frittenburger.datawarehouse.impl.SqlServiceImpl;
import de.frittenburger.datawarehouse.interfaces.SqlService;
import de.frittenburger.firewall.impl.FirewallImpl;
import de.frittenburger.firewall.interfaces.Firewall;
import de.frittenburger.io.impl.ConnectionImpl;
import de.frittenburger.io.impl.HttpRequestInputStreamReaderImpl;
import de.frittenburger.io.impl.HttpResponseOutputStreamWriterImpl;
import de.frittenburger.io.impl.PersistenceServiceImpl;
import de.frittenburger.io.impl.ServerSocketWrapperImpl;
import de.frittenburger.io.interfaces.ServerSocketWrapper;
import de.frittenburger.parser.bo.UserAgent;
import de.frittenburger.parser.impl.HttpHeaderParserImpl;
import de.frittenburger.parser.impl.MimeTypeParserImpl;
import de.frittenburger.parser.impl.RefererParserImpl;
import de.frittenburger.parser.impl.RequestLineParserImpl;
import de.frittenburger.parser.impl.UserAgentParserImpl;
import de.frittenburger.parser.interfaces.HttpHeaderParser;
import de.frittenburger.parser.interfaces.RequestLineParser;
import de.frittenburger.parser.interfaces.UserAgentParser;
import de.frittenburger.routing.bo.RoutingConfig;
import de.frittenburger.routing.impl.MatcherImpl;
import de.frittenburger.routing.impl.RoutingImpl;
import de.frittenburger.routing.interfaces.Routing;
import de.frittenburger.ssl.bo.LetsEncryptConfig;
import de.frittenburger.ssl.impl.LetsEncryptKeyStoreConfigurationResolver;
import de.frittenburger.ssl.impl.ServerSocketBuilder;
import de.frittenburger.ssl.interfaces.KeyStoreConfigurationResolver;
import de.frittenburger.tracking.impl.AnonymizerImpl;
import de.frittenburger.tracking.impl.TrackingImpl;
import de.frittenburger.tracking.impl.TrackingPointCalculatorImpl;
import de.frittenburger.tracking.impl.TrackingQueueImpl;
import de.frittenburger.tracking.interfaces.Tracking;
import de.frittenburger.tracking.interfaces.TrackingQueue;

public class ServerBuilder {

	private Logger logger = LogManager.getLogger(this.getClass());

	public static ConnectionPool clientpool = new ConnectionPoolImpl();
	public static Firewall firewall = new FirewallImpl();
	public static UserAgentParser userAgentParser = new UserAgentParserImpl();
	public static SqlService sqlService = new SqlServiceImpl();
	public static TrackingQueue trackingQueue = new TrackingQueueImpl(
			new TrackingPointCalculatorImpl(new DataWarehouseImpl(sqlService),userAgentParser,new RefererParserImpl(),new MimeTypeParserImpl()),
			new PersistenceServiceImpl());
	public static Tracking tracking = new TrackingImpl(trackingQueue,new AnonymizerImpl());
	public static Routing routing = new RoutingImpl(new MatcherImpl());

	private static RequestLineParser requestLineParser = new RequestLineParserImpl();

	private static HttpHeaderParser httpHeaderParser = new HttpHeaderParserImpl();
	
	
	
	public static void init(List<RoutingConfig> routingconfiguration,List<UserAgent> useragents, String trackingPath,int poolSize) throws IOException, SQLException, ReflectiveOperationException {
		
		sqlService.open(trackingPath+"/tracking.db");

		routing.load(routingconfiguration);
		
		((UserAgentParserImpl)userAgentParser).load(useragents);
		
		trackingQueue.init(trackingPath,false);
		((TrackingQueueImpl)trackingQueue).start();
		
		for(int i = 0;i < poolSize;i++)
		{
			ConnectionImpl clientHandler = new ConnectionImpl(new StreamHandlerImpl(new TargetHandlerImpl(), firewall, tracking,
					routing, new HttpRequestInputStreamReaderImpl(requestLineParser, httpHeaderParser), new HttpResponseOutputStreamWriterImpl()));
			clientHandler.start();
			clientpool.addConnection(clientHandler);
		}
		
		
	}
	
	Protocol protocol = Protocol.RAW;
	private ServerConfig serverConfiguration = null;
	private KeyStoreConfigurationResolver keyStoreConfigurationResolver = null;

	public ServerBuilder configure(ServerConfig serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
		this.protocol = serverConfiguration.getProtocol();
		return this;
	}
	
	
	

	public ServerBuilder configureLetsEncrypt(LetsEncryptConfig letsEncryptConfig) {

		this.keyStoreConfigurationResolver = new LetsEncryptKeyStoreConfigurationResolver(letsEncryptConfig);

		return this;
	}
	
	
	public Server build() throws UnknownHostException, IOException, GeneralSecurityException, NamingException, IllegalArgumentException, SecurityException, ReflectiveOperationException {

		String host = serverConfiguration.getHost();
		int port = serverConfiguration.getPort();
		
		ServerSocket serverSocket = null;
		 
		switch(protocol)
		{
		case HTTP:			
			// Start HttpSocket on Port 80
			logger.debug("build Server on " + host + ":" + port);
			serverSocket = Builder.custom(ServerSocketBuilder.class)
					.configure(port,host).build();
			
			break;
		case HTTPS:
			// Start HttpsSocket on Port 443
			logger.debug("build SSL Server on " + host + ":"+port);
			serverSocket = Builder.custom(ServerSocketBuilder.class).configure(port,host)
					.setSSLEnabled(true)
					.configureCertificates(keyStoreConfigurationResolver).build();
			
			break;
		default:
			throw new RuntimeException("not implemented "+protocol);
		}
		
		ServerSocketWrapper serverSocketWrapper = new ServerSocketWrapperImpl(serverSocket,protocol);
		
		return new ServerImpl(serverSocketWrapper,new SocketHandlerImpl(clientpool,firewall,tracking));
		
	}

	




	
}
