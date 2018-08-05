package de.frittenburger.tracking;
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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.frittenburger.datawarehouse.bo.BusinessVault;
import de.frittenburger.datawarehouse.bo.Hub;
import de.frittenburger.datawarehouse.bo.Link;
import de.frittenburger.datawarehouse.bo.Satellite;
import de.frittenburger.datawarehouse.interfaces.DataWarehouse;
import de.frittenburger.parser.interfaces.MimeTypeParser;
import de.frittenburger.parser.interfaces.RefererParser;
import de.frittenburger.parser.interfaces.UserAgentParser;
import de.frittenburger.tracking.bo.TrackingConstants;
import de.frittenburger.tracking.bo.TrackingPoint;
import de.frittenburger.tracking.impl.TrackingPointCalculatorImpl;
import de.frittenburger.tracking.interfaces.TrackingPointCalculator;

public class TestTrackingPointCalculatorImpl implements TrackingConstants {

	@Test
	public void test() throws SQLException, IOException {
		
		
		DataWarehouse dataWarehouse = mock(DataWarehouse.class);
		
		doAnswer(new Answer<Void>() {
		    public Void answer(InvocationOnMock invocation) {
		    	Hub hub = (Hub)invocation.getArguments()[0];
		    	hub.setId(UUID.randomUUID().toString());
		    	return null;
		    }
		}).when(dataWarehouse).createHubIfNotExists(any(Hub.class));
		
		doAnswer(new Answer<Void>() {
		    public Void answer(InvocationOnMock invocation) {
		    	Link lnk = (Link)invocation.getArguments()[0];
		    	lnk.setId(UUID.randomUUID().toString());
		    	return null;
		    }
		}).when(dataWarehouse).addLink(any(Link.class));
		
		
		UserAgentParser userAgentParser = mock(UserAgentParser.class);
		when(userAgentParser.parse("agent")).thenReturn("TestClientType");

		RefererParser refererParser = mock(RefererParser.class);
		when(refererParser.parse("referer","host")).thenReturn("TestReferer");

		MimeTypeParser mimeTypeParser = mock(MimeTypeParser.class);
		when(mimeTypeParser.parse("contentType","url")).thenReturn("MimeType");

		TrackingPointCalculator calculator = new TrackingPointCalculatorImpl(dataWarehouse,userAgentParser,refererParser,mimeTypeParser);
		
		TrackingPoint trackingPoint = new TrackingPoint();
		trackingPoint.setEvent(Event_Request);
		trackingPoint.setDate(new Date());
		trackingPoint.getParameter().put(P_InetAddress, "address");
		trackingPoint.getParameter().put(P_Host, "host");
		trackingPoint.getParameter().put(P_Url, "url");
		trackingPoint.getParameter().put(P_Status, "200");
		trackingPoint.getParameter().put(P_Referer, "referer");
		trackingPoint.getParameter().put(P_ContentType, "contentType");
		trackingPoint.getParameter().put(P_Agent, "agent");
		
		calculator.calculate(trackingPoint);
		
		calculator.aggregate();

		verify(dataWarehouse,times(2)).createHubIfNotExists(any(Hub.class));
		verify(dataWarehouse).addLink(any(Link.class));
		verify(dataWarehouse,times(2)).updateSatellite(any(Satellite.class));
		verify(dataWarehouse).calculate(any(BusinessVault.class));

	}

}
