package de.frittenburger.parser;
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
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.frittenburger.io.interfaces.PersistenceService;
import de.frittenburger.parser.impl.ApacheConfigParserImpl;
import de.frittenburger.parser.interfaces.ApacheConfigParser;

public class TestApacheConfigParserImpl {

	@Test
	public void test() throws IOException {
		
		
		List<String> lines = new ArrayList<String>();
		
		
		lines.add("# comment");
		lines.add("  ");
		lines.add("<VirtualHost *:7777>");
		lines.add("  ServerName repository1.immerarchiv.de");
		lines.add("  ServerName repository1.frittenburger.de");
		lines.add("  DocumentRoot /var/www/repository");
		lines.add("</VirtualHost>");
		lines.add("<VirtualHost *:7443>");
		lines.add("  ServerName spanisch-toolkit.frittenburger.de");
		lines.add("  DocumentRoot /var/www/spanisch-toolkit");
		lines.add("  SSLEngine on");
		lines.add("  SSLCertificateFile /etc/apache2/mycert/server.crt");
		lines.add("  SSLCertificateKeyFile /etc/apache2/mycert/server.key");
		lines.add("</VirtualHost>");

		
		PersistenceService persistenceService = mock(PersistenceService.class);

		when(persistenceService.readLines("apacheconfigfile")).thenReturn(lines);

		
		ApacheConfigParser parser = new ApacheConfigParserImpl(persistenceService);
		
		Map<String,String> virtualhost = parser.parse("apacheconfigfile");
		
		
		assertNotNull(virtualhost);
		assertTrue(virtualhost.containsKey("repository1.immerarchiv.de"));
		assertTrue(virtualhost.containsKey("repository1.frittenburger.de"));
		assertTrue(virtualhost.containsKey("spanisch-toolkit.frittenburger.de"));

		assertEquals("http://localhost:7777",virtualhost.get("repository1.immerarchiv.de"));
		assertEquals("http://localhost:7777",virtualhost.get("repository1.frittenburger.de"));
		assertEquals("https://localhost:7443",virtualhost.get("spanisch-toolkit.frittenburger.de"));

	}

}
