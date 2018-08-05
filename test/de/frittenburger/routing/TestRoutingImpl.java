package de.frittenburger.routing;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.frittenburger.core.bo.Protocol;
import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.routing.bo.RoutingConfig;
import de.frittenburger.routing.bo.Target;
import de.frittenburger.routing.impl.RoutingImpl;
import de.frittenburger.routing.interfaces.Matcher;
import de.frittenburger.routing.interfaces.Routing;

public class TestRoutingImpl {

	

	@Test
	public void testRequest() {
		
		List<String> hosts1 = Arrays.asList(new String[]{"pattern1"});
		List<String> hosts2 = Arrays.asList(new String[]{"pattern2"});
		List<String> url1 = Arrays.asList(new String[]{"url1"});
		List<String> url2 = Arrays.asList(new String[]{"url2"});

		List<RoutingConfig> routingconfiguration = new ArrayList<RoutingConfig>();
		RoutingConfig config1 = new RoutingConfig();
		config1.setSslEnabled(true);
		config1.setHosts(hosts1);
		config1.setUrls(url1);
		config1.setTarget("target1");
		routingconfiguration.add(config1);
		
		RoutingConfig config2 = new RoutingConfig();
		config2.setSslEnabled(true);
		config2.setHosts(hosts2);
		config2.setUrls(url2);
		config2.setTarget("target2");
		routingconfiguration.add(config2);
		
		Matcher matcher = mock(Matcher.class);
		when(matcher.match(hosts1, "hostname")).thenReturn(false);
		when(matcher.match(hosts2, "hostname")).thenReturn(true);
		when(matcher.match(url2, "url")).thenReturn(true);
		
		Routing routing = new RoutingImpl(matcher);
		routing.load(routingconfiguration);
		
		HttpRequest req = new HttpRequest();
		HttpHeaders headers = req.getHttpHeaders();
        req.setUrl("url");
		headers.setHost("hostname");
		
		Target target = routing.getTarget(Protocol.HTTPS, req);
		
		assertNotNull(target);
		assertEquals(false, target.isRedirect());
		assertEquals("target2", target.getUrl());

	}

	@Test
	public void testRedirect1() {
		
		List<String> hosts1 = Arrays.asList(new String[]{"pattern1"});
		List<String> hosts2 = Arrays.asList(new String[]{"pattern2"});
		List<String> url1 = Arrays.asList(new String[]{"url1"});
		List<String> url2 = Arrays.asList(new String[]{"url2"});
		
		List<RoutingConfig> routingconfiguration = new ArrayList<RoutingConfig>();
		RoutingConfig config1 = new RoutingConfig();
		config1.setSslEnabled(true);
		config1.setHosts(hosts1);
		config1.setUrls(url1);
		config1.setTarget("target1");
		routingconfiguration.add(config1);
		
		RoutingConfig config2 = new RoutingConfig();
		config2.setSslEnabled(true);
		config2.setHosts(hosts2);
		config2.setUrls(url2);
		config2.setTarget("target2");
		routingconfiguration.add(config2);
		
		Matcher matcher = mock(Matcher.class);
		when(matcher.match(hosts1, "www.example.org")).thenReturn(false);
		when(matcher.match(hosts2, "www.example.org")).thenReturn(true);
		when(matcher.match(url2, "url")).thenReturn(true);
		
		Routing routing = new RoutingImpl(matcher);
		routing.load(routingconfiguration);
		
		HttpRequest req = new HttpRequest();
		HttpHeaders headers = req.getHttpHeaders();
        req.setUrl("url");
		headers.setHost("www.example.org");
		
		Target target = routing.getTarget(Protocol.HTTP, req);
		
		assertNotNull(target);
		assertEquals(true, target.isRedirect());
		assertEquals("https://example.org", target.getUrl());

	}
	@Test
	public void testRedirect2() {
		
		List<String> hosts1 = Arrays.asList(new String[]{"pattern1"});
		List<String> hosts2 = Arrays.asList(new String[]{"pattern2"});
		List<String> url1 = Arrays.asList(new String[]{"url1"});
		List<String> url2 = Arrays.asList(new String[]{"url2"});
		
		List<RoutingConfig> routingconfiguration = new ArrayList<RoutingConfig>();
		RoutingConfig config1 = new RoutingConfig();
		config1.setSslEnabled(true);
		config1.setHosts(hosts1);
		config1.setUrls(url1);
		config1.setTarget("target1");
		routingconfiguration.add(config1);
		
		RoutingConfig config2 = new RoutingConfig();
		config2.setSslEnabled(true);
		config2.setHosts(hosts2);
		config2.setUrls(url2);
		config2.setTarget("target2");
		routingconfiguration.add(config2);
		
		Matcher matcher = mock(Matcher.class);
		when(matcher.match(hosts1, "example.org")).thenReturn(false);
		when(matcher.match(hosts2, "example.org")).thenReturn(true);
		when(matcher.match(url2, "url")).thenReturn(true);
		
		Routing routing = new RoutingImpl(matcher);
		routing.load(routingconfiguration);
		
		HttpRequest req = new HttpRequest();
		HttpHeaders headers = req.getHttpHeaders();
        req.setUrl("url");
		headers.setHost("example.org");
		
		Target target = routing.getTarget(Protocol.HTTP, req);
		
		assertNotNull(target);
		assertEquals(true, target.isRedirect());
		assertEquals("https://example.org", target.getUrl());

	}
	@Test
	public void testNotFound() {
		
		List<String> hosts1 = Arrays.asList(new String[]{"pattern1"});
		List<String> hosts2 = Arrays.asList(new String[]{"pattern2"});
		List<String> url1 = Arrays.asList(new String[]{"url1"});
		List<String> url2 = Arrays.asList(new String[]{"url2"});
		
		
		List<RoutingConfig> routingconfiguration = new ArrayList<RoutingConfig>();
		
		RoutingConfig config1 = new RoutingConfig();
		config1.setSslEnabled(true);
		config1.setHosts(hosts1);
		config1.setUrls(url1);
		config1.setTarget("target1");
		routingconfiguration.add(config1);
		
		RoutingConfig config2 = new RoutingConfig();
		config2.setSslEnabled(true);
		config2.setHosts(hosts2);
		config2.setUrls(url2);
		config2.setTarget("target2");
		routingconfiguration.add(config2);
		
		
		Matcher matcher = mock(Matcher.class);
		when(matcher.match(hosts1, "hostname")).thenReturn(false);
		when(matcher.match(hosts2, "hostname")).thenReturn(true);
		when(matcher.match(url2, "url")).thenReturn(false);
		
		
		Routing routing = new RoutingImpl(matcher);
		routing.load(routingconfiguration);
		
		HttpRequest req = new HttpRequest();
		HttpHeaders headers = req.getHttpHeaders();
        req.setUrl("url");
		headers.setHost("hostname");
		
		Target target = routing.getTarget(Protocol.HTTP, req);
		
		assertNull(target);
	

	}
}
