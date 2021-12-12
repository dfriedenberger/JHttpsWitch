package de.frittenburger.tracking.impl;
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import de.frittenburger.tracking.interfaces.TrackingPointCalculator;

public class TrackingPointCalculatorImpl implements TrackingPointCalculator, TrackingConstants {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	private final DataWarehouse datawareHouse;
	private final UserAgentParser userAgentParser;
	private final RefererParser refererParser;
	private final MimeTypeParser mimeTypeParser;
	private final Set<String> aggregate = new HashSet<String>();

	public TrackingPointCalculatorImpl(DataWarehouse datawareHouse,UserAgentParser userAgentParser,
			RefererParser refererParser,MimeTypeParser mimeTypeParser) {
		this.datawareHouse = datawareHouse;
		this.userAgentParser = userAgentParser;
		this.refererParser = refererParser;
		this.mimeTypeParser = mimeTypeParser;
	}

	@Override
	public void calculate(TrackingPoint p) throws SQLException {

		synchronized (aggregate) {
			aggregate.add(p.getEvent());
		}
		
		logger.debug(p);

		
		switch(p.getEvent())
		{
			case Event_Request:
				String host = p.getParameter().get(P_Host).toLowerCase();
				String originAgent = p.getParameter().get(P_Agent);
				String agent = userAgentParser.parse(originAgent);
				if(originAgent == null) originAgent = "";

				String referer = refererParser.parse(p.getParameter().get(P_Referer),host);
				String mimeType = mimeTypeParser.parse(p.getParameter().get(P_ContentType),p.getParameter().get(P_Url));
				
				Hub hubHost = new Hub("Host");
				hubHost.setBusinessKey("Name", host);
				datawareHouse.createHubIfNotExists(hubHost);
				
				Hub hubClient = new Hub("Client");
				hubClient.setBusinessKey("IpAddr", p.getParameter().get(P_InetAddress));
				
				hubClient.setBusinessKey("Agent", originAgent);
				datawareHouse.createHubIfNotExists(hubClient);

				Satellite sClientType = new Satellite("ClientType", hubClient);
				sClientType.setAttribute("Type", agent);
				datawareHouse.updateSatellite(sClientType);

				Link lnk = new Link(hubClient, hubHost);
				datawareHouse.addLink(lnk);
				
				int status = Integer.parseInt(p.getParameter().get(P_Status));

				Satellite sRequest = new Satellite("Request", lnk);
				sRequest.setAttribute("Url",p.getParameter().get(P_Url));
				sRequest.setAttribute("Status",status);
				sRequest.setAttribute("MimeType",mimeType);
				sRequest.setAttribute("Referer",referer);
				sRequest.setLOAD_DTS(p.getDate()); 
				datawareHouse.updateSatellite(sRequest);
				
				
				
				break;
			case Event_ConnectionPoolUsed:
				
				Hub hubInfrastructure = new Hub("Infrastructure");
				hubInfrastructure.setBusinessKey("Name", "ConnectionPool");
				hubInfrastructure.setSRC_REC("TrackingPointCalculatorImpl");

				datawareHouse.createHubIfNotExists(hubInfrastructure);

				Satellite sUsed = new Satellite("Used", hubInfrastructure);
				double percent = Double.parseDouble(p.getParameter().get(P_Used));
				sUsed.setAttribute("Value",percent);
				sUsed.setLOAD_DTS(p.getDate()); 
				datawareHouse.updateSatellite(sUsed);
		}
		
		
		
	}

	@Override
	public void aggregate() throws SQLException, IOException {

		aggregateBVInfrastructure();

		aggregateBVRequest();
		
	}

	private void aggregateBVRequest() throws IOException, SQLException {
		
		synchronized (aggregate) {
			if(!aggregate.remove(Event_Request)) return;
		}
	
		BusinessVault bv = new BusinessVault("Request");
		bv.defineText("Host");
		bv.defineText("Customer");
		bv.defineText("CustomerType");
		bv.defineDatetime("Date");
		bv.defineText("Url");
		bv.defineText("MimeType");
		bv.defineText("Referer");
		bv.defineInteger("Status");

		bv.load("sql/selectBV_Request.sql");
		datawareHouse.calculate(bv);
				
	}

	private void aggregateBVInfrastructure() throws IOException, SQLException {
		synchronized (aggregate) {
			if(!aggregate.remove(Event_ConnectionPoolUsed)) return;
		}
	
		BusinessVault bv = new BusinessVault("Infrastructure");
		bv.defineText("Name");
		bv.defineDatetime("Date");
		bv.defineDouble("Value");

		bv.load("sql/selectBV_Infrastructure.sql");
		datawareHouse.calculate(bv);
				
	}

}
