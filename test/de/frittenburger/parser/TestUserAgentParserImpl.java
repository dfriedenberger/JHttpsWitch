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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.frittenburger.parser.bo.UserAgent;
import de.frittenburger.parser.impl.UserAgentParserImpl;
import de.frittenburger.parser.interfaces.UserAgentParser;

public class TestUserAgentParserImpl {

	
	private static final List<UserAgent> agents = Arrays.asList(new UserAgent[] {

			UserAgent.create(new String[]{
					"[Bb]ot|index|[Ss]pider|[Cc]rawl|wget|slurp",
					"Mediapartners-Google|AppEngine-Google",
					"nagios-plugins|facebookexternalhit|feedzirra|Nutch",
					"Serverstate User Agent",
					"Riddler|Seobility|360Spider",
					"InternetSeer.com",
					"^Java/[0-9._]+$",
					"Morfeus Fucking Scanner",
					"BingPreview"
			}, true, "Bot"),

			UserAgent.create(new String[]{
					"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) Chrome/[0-9.]+ Safari/[0-9.]+$"
			}, false ,"Google Chrome"),
			
			UserAgent.create(new String[]{
					"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) Version/[0-9.]+ Safari/[0-9.]+$"
			}, false ,"Safari"),
			
			UserAgent.create(new String[]{
					"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) Chrome/[0-9.]+ Mobile Safari/[0-9.]+"
			}, false ,"Mobile Chrome"),
			
			UserAgent.create(new String[]{
					"^Mozilla/5.0 \\(.+\\) AppleWebKit/[0-9.]+ \\(KHTML, like Gecko\\) [a-zA-Z]+/[0-9.]+ Mobile[/0-9a-zA-Z.]* Safari/[0-9.]+"
			}, false ,"Mobile Safari"),
			
			UserAgent.create(new String[]{
					"^Mozilla/5.0 \\(.+\\) Gecko(/[0-9]+){0,1} Firefox/[0-9a-z.]+$",
					"^Mozilla/5.0 \\(.+\\) Gecko(/[0-9]+){0,1} Firefox/[0-9a-z.]+ \\((.NET CLR [0-9.; ]+)+\\)$",
					"^Mozilla/5.0 \\(.+\\) Gecko(/[0-9]+){0,1} Firefox/[0-9a-z.]+ Paros/[0-9.]+$",
					"^Mozilla/5.0 \\(.+\\) Gecko/[0-9]+ Ubuntu/[0-9.]+ \\([a-zA-Z]+\\) Firefox/[0-9a-z.]+$",
					"^Mozilla/5.0 \\(.+\\) Gecko/[0-9]+ Ubuntu/[0-9.]+ \\([a-zA-Z]+\\) Firefox/[0-9a-z.]+$",
			}, false ,"Firefox"),

			UserAgent.create(new String[]{
					"^Opera/[0-9.]+ \\(.+\\) Presto/[0-9.]+ Version/[0-9.]+$"
			}, false ,"Opera"),
			
			UserAgent.create(new String[]{
					"^Mozilla/[0-9.]+ \\(compatible; MSIE [0-9.]+; Windows"
			}, false ,"Internet Explorer"),
			
			UserAgent.create(new String[]{
					"Mozilla/5.0 \\(.+\\) like Gecko$"
			}, false ,"Internet Explorer"),

			UserAgent.create(new String[]{
					"^(Mozilla.*(Gecko|KHTML|MSIE|Presto|Trident)|Opera).*$"
			}, false , "Browsers")

		});
	
	
	@Test
	public void test() {
		
		UserAgentParser parser = new UserAgentParserImpl();
		((UserAgentParserImpl)parser).load(agents);
		assertEquals("Empty",parser.parse(null));
		assertEquals("Empty",parser.parse(""));
		assertEquals("Unknown",parser.parse("xyz"));
		assertEquals("Google Chrome",parser.parse("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"));
		assertEquals("Firefox",parser.parse("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1"));		

	}

}
