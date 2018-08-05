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
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Security;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.X509KeyManager;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;


public class KeyManagerBuilder {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	private final List<X509Certificate> certificates = new ArrayList<X509Certificate>();
	private PrivateKey privateKey = null;
    
	
	static
	{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	private KeyManagerBuilder()  {}

	public static KeyManagerBuilder custom() {
		return new KeyManagerBuilder();
	}

	public KeyManagerBuilder setCertificates(String certificateFile) throws GeneralSecurityException, IOException {
		
		JcaX509CertificateConverter converter = new JcaX509CertificateConverter().setProvider( "BC" );

		PEMParser pemParser = new PEMParser(new FileReader(certificateFile));

		while(true)
		{
			
			X509CertificateHolder certHolder = (X509CertificateHolder) pemParser.readObject();
			if(certHolder == null) break;
			X509Certificate cert = converter.getCertificate(certHolder);
			certificates.add(cert);
		}
		
		pemParser.close();
		
		return this;
	}

	public KeyManagerBuilder setPrivateKey(String privateKeyFile) throws IOException, GeneralSecurityException {
		
		
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

		PEMParser pemParser = new PEMParser(new FileReader(privateKeyFile));
		PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
		privateKey = converter.getPrivateKey(privateKeyInfo);
		pemParser.close();
		return this;
	}

	public Map<String,X509KeyManager> build() throws GeneralSecurityException, IOException, InvalidNameException {
		
		//Create Keystore
	 
	
		String domain = null;
	
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(null);
		 
		//resolve Domain
		X509Certificate certificate = certificates.get(0);
		Principal dn = certificate.getSubjectDN();
		LdapName ldapDN = new LdapName(dn.getName());
		for(Rdn rdn: ldapDN.getRdns()) {
			if(!rdn.getType().equalsIgnoreCase("CN")) continue;
			logger.debug("CN : " + rdn.getValue());
		    domain = (String) rdn.getValue();
		}
			
		int index = 0;
		for(X509Certificate cert : certificates)
		{
			String certificateAlias = Integer.toString(index++);
			keystore.setCertificateEntry(certificateAlias, cert);
		}
	
		
		char[] password = "password".toCharArray();	
		keystore.setKeyEntry("key-alias", privateKey, password, new Certificate[] {certificate});

		// KeyManagerFaktory initialisieren
		logger.debug("build X509KeyManager for domain "+domain);
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keystore, password);

		
		KeyManager[] kms = keyManagerFactory.getKeyManagers();
		if(kms.length != 1)
			throw new RuntimeException(kms.length + " KeyManager found");
		
		Map<String,X509KeyManager> m = new HashMap<String,X509KeyManager>();
		m.put(domain, (X509KeyManager)kms[0]);
		return m;
	}

}
