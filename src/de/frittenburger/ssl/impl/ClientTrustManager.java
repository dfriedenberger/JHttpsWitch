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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class ClientTrustManager  implements X509TrustManager {

	
	private final Logger logger = Logger.getLogger(this.getClass());

	
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		for(int i = 0;i < chain.length;i++)
    	{
    		String issuerDn = chain[i].getIssuerDN().getName();
    		String subjectDn = chain[i].getSubjectDN().getName();
    		logger.info(i+" subject("+subjectDn+") issuer("+issuerDn+")");
    	}
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
