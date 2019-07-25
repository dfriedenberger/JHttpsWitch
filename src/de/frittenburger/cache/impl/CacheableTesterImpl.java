package de.frittenburger.cache.impl;

import java.util.HashSet;
import java.util.Set;

import de.frittenburger.cache.interfaces.CacheableTester;
import de.frittenburger.io.bo.HttpRequest;

public class CacheableTesterImpl implements CacheableTester {

	private final Set<String> extensions;

	
	public CacheableTesterImpl(Set<String> extensions) {
		this.extensions = extensions;
	}

	public CacheableTesterImpl() {
		this.extensions = new HashSet<>();
		
		//images
		this.extensions.add("jpg");
		this.extensions.add("jpeg");
		this.extensions.add("gif");
		this.extensions.add("png");
		
		//assets
		this.extensions.add("css");
		this.extensions.add("js");

	}

	@Override
	public boolean isCacheable(HttpRequest req) {
		
		if(!req.getMethod().toUpperCase().equals("GET")) return false;
		String url = req.getUrl().toLowerCase();
		int i = url.lastIndexOf(".");
		if(i < 0) return false;
		
		String ext = url.substring(i+1);
		return extensions.contains(ext);
	}

}
