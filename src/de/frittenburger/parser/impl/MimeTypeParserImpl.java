package de.frittenburger.parser.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.frittenburger.parser.interfaces.MimeTypeParser;

public class MimeTypeParserImpl implements MimeTypeParser {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	private final Pattern pattern;
	private final Map<String,String> mime = new HashMap<String,String>();
	public MimeTypeParserImpl()
	{
		this.pattern = Pattern.compile("\\.([^.^/^\\\\]+)$");
		mime.put("html", "text/html");
		mime.put("js", "application/javascript");
		mime.put("css", "text/css");
		mime.put("json", "application/json");

		mime.put("jpeg", "image/jpeg");
		mime.put("jpg", "image/jpeg");
		mime.put("gif", "image/gif");
		mime.put("png", "image/png");
		mime.put("ico", "image/x-icon");
		
	}
	@Override
	public String parse(String contentType, String url) {

		if(contentType != null && !contentType.isEmpty())
			return contentType;
		
        Matcher matcher = pattern.matcher(url);

		if(!matcher.find())
			return "text/html";
		
		String ext = matcher.group(1).toLowerCase();
		
		if(mime.containsKey(ext))
			return mime.get(ext);
		
		logger.warn("No mimetype found for contentType="+contentType+" ext="+ext+" url="+url);
		
		return "filetype_"+ext;
	}

}
