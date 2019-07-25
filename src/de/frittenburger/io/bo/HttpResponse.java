package de.frittenburger.io.bo;
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

import java.nio.charset.StandardCharsets;

public class HttpResponse {

	
	public static final int OK                  = 200;
	
	public static final int MOVED_PERMANENTLY   = 301;

	public static final int FORBIDDEN           = 403;
	public static final int NOT_FOUND           = 404;
	public static final int SERVICE_UNAVAILABLE = 503;

	public static String HTML ="text/html";

	private int status = 500;
	
	private String responseLine = null;
	private final HttpHeaders httpHeaders = new HttpHeaders();
	private byte[] content = null;
	private final HttpHeaders httpTrailers = new HttpHeaders();


	
	public void setError(int error,String html) {
		setStatus(error);
		content = html.getBytes(StandardCharsets.UTF_8);
		
		httpHeaders.getHeaders().add("Content-Length: "+content.length);
		httpHeaders.getHeaders().add("Content-Type: "+HTML);
		httpHeaders.getHeaders().add("Connection: Closed");
		
	}

	public void setPermanentRedirect(String url) {
		setStatus(MOVED_PERMANENTLY);
		httpHeaders.getHeaders().add("Location: "+url);		
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}


	public String getResponseLine() {
		return responseLine;
	}

	public void setResponseLine(String responseLine) {
		this.responseLine = responseLine;
	}
	
	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}
	
	@Override
	public String toString() {
		return "HttpResponse [status=" + status + " contentLength=" + httpHeaders.getContentLength() + "]";
	}

	public HttpHeaders getHttpTrailers() {
		return httpTrailers;
	}

}
