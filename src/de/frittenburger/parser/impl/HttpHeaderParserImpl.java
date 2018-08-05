package de.frittenburger.parser.impl;
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
import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.parser.interfaces.HttpHeaderParser;

public class HttpHeaderParserImpl implements HttpHeaderParser {

	@Override
	public void parse(String line, HttpHeaders headers) {
		
		headers.getHeaders().add(line);
		int ix = line.indexOf(":");
		if(ix > 0)
		{
			String key = line.substring(0,ix).trim().toLowerCase();
			String val = line.substring(ix+1).trim();
			if(key.equals("host")) headers.setHost(val);
			
			if(key.equals("connection"))
			{
				if(val.toLowerCase().equals("keep-alive")) headers.setKeepAlive(true);
			}
			if (key.equals("content-length"))
				headers.setContentLength(Integer.parseInt(val));
			
			if (key.equals("content-type"))
			{
				String p[] = val.split(";");
				headers.setContentType(p[0].trim());
				for(int i = 1;i < p.length;i++)
				{
					int iy = p[i].indexOf("=");
					if(iy > 0)
					{
						String ckey = p[i].substring(0,iy).trim().toLowerCase();
						String cval = p[i].substring(iy+1).trim();
						if(ckey.equals("charset"))
							headers.setCharset(cval);
						
					}
				}
				
			}
			if(key.equals("referer"))
				headers.setReferer(val);
			if(key.equals("user-agent"))
				headers.setAgent(val);
			
			//Expect: 100-continue
			if(key.equals("expect"))
			{
				if(val.toLowerCase().equals("100-continue")) headers.setExpect100Continue(true);
			}
			
			if(key.equals("transfer-encoding"))
			{
				if(val.toLowerCase().equals("chunked")) headers.setChunkedTransferEncoding(true);
			}
		}
	}

	
}
