package de.frittenburger.io.impl;
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

import de.frittenburger.io.bo.HttpResponse;

public class HttpResponseBuilder {

	private int error;
	private HtmlTemplate responseText;

	public HttpResponseBuilder configureServiceNotAvailable(String host) throws IOException {
		
		this.error = HttpResponse.SERVICE_UNAVAILABLE;
		this.responseText = HtmlTemplate.load("templates/serviceUnavailable.htm");
		responseText.replace("{host}", host);
		return this;
		
	}
	
	public HttpResponseBuilder configureTargetNotFound(String host) throws IOException {
		
		this.error = HttpResponse.SERVICE_UNAVAILABLE;
		this.responseText = HtmlTemplate.load("templates/targetNotFoundHost.htm");
		responseText.replace("{host}", host);
		return this;
		
	}
	
	public HttpResponseBuilder configureFirewallBlocked() throws IOException {
		
		this.error = HttpResponse.FORBIDDEN;
		this.responseText = HtmlTemplate.load("templates/firewallBlockedHostOrUrl.htm");
		return this;
		
	}
	
	public HttpResponseBuilder configureException() throws IOException {
		this.error = HttpResponse.SERVICE_UNAVAILABLE;
		this.responseText = HtmlTemplate.load("templates/exception.htm");
		return this;
	}

	
	public HttpResponse build() {
		
		HttpResponse response = new HttpResponse();
		response.setError(error, responseText.toString());	
		return response;
		
	}

	


	

}
