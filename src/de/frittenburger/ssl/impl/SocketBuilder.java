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
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;


public class SocketBuilder {

	private final Logger logger = Logger.getLogger(this.getClass());
	private boolean sslEnabled = false;
	private SSLContext sslContext = null;

	public SocketBuilder setSSLEnabled(boolean sslEnabled) throws NoSuchAlgorithmException {
		this.sslEnabled = sslEnabled;
		this.sslContext = SSLContext.getInstance("SSL");
		return this;
	}

	public Socket build() throws UnknownHostException, IOException, GeneralSecurityException {


		if (sslEnabled) {
			logger.debug("build ssl socket");
			sslContext.init(null,  new TrustManager[]{new ClientTrustManager()}, new SecureRandom());
			return sslContext.getSocketFactory().createSocket();
		}
		logger.debug("build socket");
		return new Socket();

	}

}
