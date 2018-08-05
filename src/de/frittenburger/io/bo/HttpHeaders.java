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
import java.util.ArrayList;
import java.util.List;

public class HttpHeaders {
	
	private String host = null;
	private String referer = null;
	private String agent = null;
	private int contentLength = -1;
	private byte[] content = null;
	private String contentType = null;
	private String charset = null;

	private boolean keepAlive = false;
	private boolean expect100Continue = false;
	private List<String> headers = new ArrayList<String>();
	private boolean chunkedTransferEncoding = false;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public boolean isKeepAlive() {
		return keepAlive;
	}
	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
	public boolean isExpect100Continue() {
		return expect100Continue;
	}
	public void setExpect100Continue(boolean expect100Continue) {
		this.expect100Continue = expect100Continue;
	}
	public List<String> getHeaders() {
		return headers;
	}
	public void setChunkedTransferEncoding(boolean b) {
		this.chunkedTransferEncoding = true;
	}
	public boolean isChunkedTransferEncoding() {
		return chunkedTransferEncoding;
	}
	
}
