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
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.frittenburger.ssl.bo.KeyStoreConfig;
import de.frittenburger.ssl.bo.LetsEncryptConfig;
import de.frittenburger.ssl.interfaces.KeyStoreConfigurationResolver;

public class LetsEncryptKeyStoreConfigurationResolver implements KeyStoreConfigurationResolver {

	private final Logger logger = Logger.getLogger(this.getClass());
	private final LetsEncryptConfig letsEncryptConfig;

	public LetsEncryptKeyStoreConfigurationResolver(LetsEncryptConfig letsEncryptConfig) {
		this.letsEncryptConfig = letsEncryptConfig;
	}

	@Override
	public List<KeyStoreConfig> resolve() throws IOException {
		
		List<KeyStoreConfig> config = new ArrayList<KeyStoreConfig>();
		
		String rootPath = letsEncryptConfig.getLivePath();
		
		 File root = new File( rootPath );
		 
		 if(!root.exists())
		 {
			 logger.error("Path "+rootPath+" don't exists");
			 return config;
		 }
		 
	     File[] domains = root.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		 });
	     
	     for(File domain : domains)
	     {
	    	 File privKey = new File(domain,"privkey.pem");
	    	 File certs = new File(domain,"fullchain.pem");
	    	 
	    	 if(!privKey.exists()) continue;
	    	 if(!certs.exists()) continue;
	    	 
	    	 KeyStoreConfig conf = new KeyStoreConfig();
	    	 conf.setCertificateFile(certs.getPath());
	    	 conf.setPrivateKeyFile(privKey.getPath());

	    	 logger.info("resolved "+conf);

	    	 config.add(conf);
	     }
	     
		
		
		return config;
	}

}
