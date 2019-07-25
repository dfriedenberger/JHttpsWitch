package de.frittenburger.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.frittenburger.cache.interfaces.CacheMap;
import de.frittenburger.io.bo.HttpResponse;

public class CacheMapImpl implements CacheMap {

	private final Map<String,HttpResponse> map = new HashMap<>();
	private final List<String> keys = new ArrayList<>();
	
	@Override
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	@Override
	public HttpResponse get(String key) {
		keys.remove(key);
		keys.add(key);
		return map.get(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public HttpResponse remove() {
		String rm = keys.remove(0);
		return map.remove(rm);
	}

	@Override
	public void put(String key, HttpResponse res) {
		map.put(key,res);
		keys.add(key);
	}

}
