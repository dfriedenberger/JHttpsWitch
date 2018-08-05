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

public class DomainAlias {
     
	private static final String separator = "___";
	private String domain;
	private String alias;

	public DomainAlias(String domain,String alias)
	{
		this.domain = domain;
		this.alias = alias;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public String toString()
	{
		return domain + separator + alias;
	}

	public static DomainAlias fromString(String str) {
		int ix = str.indexOf(separator);
		if(ix < 1)
			throw new RuntimeException("parsing domain alias "+str);
		return new DomainAlias(str.substring(0,ix),str.substring(ix + separator.length()));
	}

	
	
}
