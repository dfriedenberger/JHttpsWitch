package de.frittenburger.tracking.bo;
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
public interface TrackingConstants {
	
	static final String Event_DenyIpAddress      = "DenyIpAddress";
	static final String Event_Request            = "Request";
	static final String Event_ConnectionPoolUsed = "ConnectionPoolUsed";
	static final String Event_ConnectionPoolBusy = "ConnectionPoolBusy";

	
	static final String P_InetAddress = "inetaddress";
	static final String P_Host = "host";
	static final String P_Url = "url";
	static final String P_Referer = "referer";
	static final String P_Agent = "agent";
	static final String P_Used = "used";
	static final String P_Status = "status";
	static final String P_ContentType = "contenttype";

}
