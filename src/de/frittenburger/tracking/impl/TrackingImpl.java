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
import java.net.InetAddress;
import java.util.Date;

import org.apache.log4j.Logger;

import de.frittenburger.tracking.bo.TrackingConstants;
import de.frittenburger.tracking.bo.TrackingPoint;
import de.frittenburger.tracking.interfaces.Anonymizer;
import de.frittenburger.tracking.interfaces.Tracking;
import de.frittenburger.tracking.interfaces.TrackingQueue;

public class TrackingImpl implements Tracking, TrackingConstants {

	


	private final Logger logger = Logger.getLogger(this.getClass());

	
	private final TrackingQueue queue;
	private final Anonymizer anonymizer;

	public TrackingImpl(TrackingQueue queue,Anonymizer anonymizer)
	{
		this.queue = queue;
		this.anonymizer = anonymizer;
	}
	

	@Override
	public void denyEvent(InetAddress inetAddress) {

		String address =  anonymizer.anonymizeIpAddress(inetAddress.getHostAddress());
		TrackingPoint trackingPoint = new TrackingPoint();
		trackingPoint.setEvent(Event_DenyIpAddress);
		trackingPoint.setDate(new Date());
		trackingPoint.getParameter().put(P_InetAddress, address);
		
		logger.warn(trackingPoint);		
		queue.enqueue(trackingPoint);
		
	}

	@Override
	public void usingConnectionPoolEvent(int used) {
		
		
		TrackingPoint trackingPoint = new TrackingPoint();
		trackingPoint.setEvent(Event_ConnectionPoolUsed);
		trackingPoint.setDate(new Date());
		trackingPoint.getParameter().put(P_Used, ""+used);
		
		logger.debug(trackingPoint);		
		queue.enqueue(trackingPoint);
	}
	
	@Override
	public void busyConnectionPoolEvent() {
		
		
		TrackingPoint trackingPoint = new TrackingPoint();
		trackingPoint.setEvent(Event_ConnectionPoolBusy);
		trackingPoint.setDate(new Date());
		
		logger.warn(trackingPoint);
		queue.enqueue(trackingPoint);
	}


	@Override
	public void requestEvent(InetAddress inetAddress, String host, String url, String referer, String agent, int status,
			String contentType) {
		
		String address =  anonymizer.anonymizeIpAddress(inetAddress.getHostAddress());
		
		TrackingPoint trackingPoint = new TrackingPoint();
		trackingPoint.setEvent(Event_Request);
		trackingPoint.setDate(new Date());
		trackingPoint.getParameter().put(P_InetAddress, address);
		trackingPoint.getParameter().put(P_Host, host);
		trackingPoint.getParameter().put(P_Url, url);
		trackingPoint.getParameter().put(P_Status, ""+status);
		trackingPoint.getParameter().put(P_Referer, referer);
		trackingPoint.getParameter().put(P_ContentType, contentType);
		trackingPoint.getParameter().put(P_Agent, agent);
		
		logger.info(trackingPoint);
		queue.enqueue(trackingPoint);
		
	}


	

	

}
