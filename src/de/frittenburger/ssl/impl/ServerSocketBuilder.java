package de.frittenburger.ssl.impl;
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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.X509KeyManager;

import org.apache.log4j.Logger;

import de.frittenburger.ssl.bo.KeyStoreConfig;
import de.frittenburger.ssl.interfaces.KeyStoreConfigurationResolver;


public class ServerSocketBuilder {

	private final Logger logger = Logger.getLogger(this.getClass());
	private int port = 80;
	private String host = "localhost";
	private boolean sslEnabled = false;
	private SSLContext sslContext = null;

	public ServerSocketBuilder configure(int port, String host)
	{
		this.port = port;
		this.host = host;
		return this;
	}
	
	public ServerSocketBuilder setSSLEnabled(boolean sslEnabled) throws NoSuchAlgorithmException {
		this.sslEnabled = sslEnabled;
		this.sslContext = SSLContext.getInstance("SSLv3");
		return this;
	}

	public ServerSocketBuilder configureCertificates(KeyStoreConfigurationResolver keyStoreConfigurationResolver)
			throws GeneralSecurityException, IOException, NamingException {
		// SSL-Konfiguration
		Map<String, X509KeyManager> keyManagers = new HashMap<String, X509KeyManager>();

		String defaultHostname = null;
		for (KeyStoreConfig keyStoreConfig : keyStoreConfigurationResolver.resolve()) {
			Map<String, X509KeyManager> map = KeyManagerBuilder.custom()
					.setCertificates(keyStoreConfig.getCertificateFile())
					.setPrivateKey(keyStoreConfig.getPrivateKeyFile()).build();

			if (defaultHostname == null) // First entry is default host
				defaultHostname = map.keySet().iterator().next();

			keyManagers.putAll(map);

		}

		sslContext.init(new X509KeyManager[] { new SNICompositeX509KeyManager(keyManagers, defaultHostname) }, null,
				null);

		return this;
	}

	public ServerSocket build() throws UnknownHostException, IOException {

		if (sslEnabled) {
			logger.debug("build ssl socket on "+host+":"+port);
			SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
			SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port, 0,
					InetAddress.getByName(host));

			// enable SNI
			Collection<SNIMatcher> matchers = Arrays.asList(new SNIHostnameMatcher());

			SSLParameters params = sslServerSocket.getSSLParameters();
			params.setSNIMatchers(matchers);
			sslServerSocket.setSSLParameters(params);

			return sslServerSocket;
		}
		logger.debug("build socket on "+host+":"+port);
		return new ServerSocket(port, 0, InetAddress.getByName(host));

	}

}
