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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrackingPoint {
	private String event;
	private Date date;
	private Map<String,String> parameter = new HashMap<String,String>();
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Map<String, String> getParameter() {
		return parameter;
	}
	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}
	@Override
	public String toString() {
		return "TrackingPoint [event=" + event + ", date=" + date + ", parameter=" + parameter + "]";
	}
	
	
}
