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
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.StandardConstants;


	
	public final class SNIHostnameMatcher extends SNIMatcher { 
		 private String hostname; 
		 
		 public SNIHostnameMatcher() { 
		  super(StandardConstants.SNI_HOST_NAME); 
		 } 
		 
		 @Override 
		 public boolean matches(SNIServerName name) { 
		  SNIHostName hostname = (SNIHostName) name; 
		  String str = hostname.getAsciiName(); 
		  boolean accept = !str.trim().equals(""); 
		  if (accept) { 
		   this.hostname = str; 
		  } 
		  return accept; 
		 } 
		 
		 public boolean isSniEnabled() { 
		  return hostname != null; 
		 } 
		 
		 public String getSniHostname() { 
		  return hostname; 
		 } 
		}
	