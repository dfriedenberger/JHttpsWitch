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

public class HttpRequest {

	private String url = null;
	private String method = null;
	
	
	private String requestLine = null;
	private final HttpHeaders httpHeaders = new HttpHeaders();
	private byte[] content = null;

	

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
		
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public String getRequestLine() {
		return requestLine;
	}
	public void setRequestLine(String requestLine) {
		this.requestLine = requestLine;
	}
	
	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}
		
	@Override
	public String toString() {
		return "HttpRequest [host=" + httpHeaders.getHost() + ", url=" + url + ", contentLength=" + httpHeaders.getContentLength() + "]";
	}
	
	
	
	
}
