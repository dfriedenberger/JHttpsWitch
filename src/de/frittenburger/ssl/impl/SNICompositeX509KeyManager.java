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
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509KeyManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.cert.X509Certificate;

public class SNICompositeX509KeyManager implements X509KeyManager {

	private final Map<String,X509KeyManager> keyManagers;
	private String defaultHost;
	private Logger logger;

	  /**
	   * Creates a new {@link SNICompositeX509KeyManager}.
	   *
	   * @param keyManagers the X509 key managers, ordered with the most-preferred managers first.
	   */
	  public SNICompositeX509KeyManager(Map<String,X509KeyManager> keyManagers,String defaultHost) {
			logger = LogManager.getLogger(this.getClass());

	    this.keyManagers = keyManagers;
	    this.defaultHost = defaultHost;
	  }

	  
	  /**
	   * Chooses the first non-null client alias returned from the delegate
	   * {@link X509TrustManagers}, or {@code null} if there are no matches.
	   */
	  public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
	       throw new UnsupportedOperationException("Method chooseClientAlias() not yet implemented.");
	  }

	  /**
	   * Chooses the first non-null server alias returned from the delegate
	   * {@link X509TrustManagers}, or {@code null} if there are no matches.
	   */
	  public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
		  
			if (!keyType.equals("RSA") || !(socket instanceof SSLSocket))
				return null;

			String domain = getDomainFromSocket((SSLSocket) socket);
			
			X509KeyManager keyManager = keyManagers.get(domain);

			
			String alias = keyManager.chooseServerAlias(keyType, issuers, socket);
			
			return new DomainAlias(domain,alias).toString();
		
	  }

	  private String getDomainFromSocket(SSLSocket socket) {
		  
		  
			SSLParameters params = socket.getSSLParameters();

			Iterator<SNIMatcher> it = params.getSNIMatchers().iterator();
			if (it.hasNext()) {
				SNIHostnameMatcher matcher = (SNIHostnameMatcher) it.next();

				if (matcher.isSniEnabled()) {
					String hostname = matcher.getSniHostname().toLowerCase();
					if(keyManagers.containsKey(hostname))
					{
						logger.info("server name identification: "+hostname);
						return hostname;
					}
				}
			}
		    return defaultHost;
	}


	/**
	   * Returns the first non-null private key associated with the
	   * given alias, or {@code null} if the alias can't be found.
	   */
	  public  PrivateKey getPrivateKey(String alias) {
		  DomainAlias dAlias = DomainAlias.fromString(alias);
		  X509KeyManager keyManager = keyManagers.get(dAlias.getDomain());
		  return keyManager.getPrivateKey(dAlias.getAlias());
	  }

	  /**
	   * Returns the first non-null certificate chain associated with the
	   * given alias, or {@code null} if the alias can't be found.
	   */
	  public  X509Certificate[] getCertificateChain(String alias) {
		  DomainAlias dAlias = DomainAlias.fromString(alias);
		  X509KeyManager keyManager = keyManagers.get(dAlias.getDomain());
		  return keyManager.getCertificateChain(dAlias.getAlias());
	  }

	  /**
	   * Get all matching aliases for authenticating the client side of a
	   * secure socket, or {@code null} if there are no matches.
	   */
	  public String[] getClientAliases(String keyType, Principal[] issuers) {
	        throw new UnsupportedOperationException("Method getClientAliases() not yet implemented.");
	  }

	  /**
	   * Get all matching aliases for authenticating the server side of a
	   * secure socket, or {@code null} if there are no matches.
	   */
	  public String[] getServerAliases(String keyType, Principal[] issuers) {
	        throw new UnsupportedOperationException("Method getServerAliases() not yet implemented.");

	        /*
		  List<String> aliases = new ArrayList<String>();
		  
		  for(String domain : keyManagers.keySet())
		  {
			  X509KeyManager keyManager = keyManagers.get(domain);
			  
			  
			  
		  }
		  	      return aliases.toArray(new String[0]);

			*/  
			  
		  
		  
	  }

	
	 

	}