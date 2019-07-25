package de.frittenburger.cache.impl;

import java.util.function.Function;

import org.apache.log4j.Logger;

import de.frittenburger.cache.interfaces.Cache;
import de.frittenburger.cache.interfaces.CacheMap;
import de.frittenburger.cache.interfaces.CacheableTester;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;

public class CacheImpl implements Cache {

	private final Logger logger = Logger.getLogger(this.getClass());

	private final CacheableTester tester;
	private final CacheMap map;

	private final Function<HttpRequest,String> req2keyMapper = new ReqKeyMapperImpl();
	private final long maxSize;
	private final int HTTP_RESPONSE_HEADSIZE = 64;
	private long currentSize = 0;

	public CacheImpl(long maxSize,CacheMap map,CacheableTester tester) {
		this.tester = tester;
		this.map = map;
		this.maxSize = maxSize;
	}
	
	@Override
	public boolean contains(HttpRequest req) {
		String key = req2keyMapper.apply(req);
		return map.containsKey(key);
	}

	@Override
	public HttpResponse get(HttpRequest req) {
		String key = req2keyMapper.apply(req);
		return map.get(key);
	}

	@Override
	public void put(HttpRequest req, HttpResponse res) {
		
		if(!tester.isCacheable(req))
			return;
		
		int size = HTTP_RESPONSE_HEADSIZE + res.getHttpHeaders().getContentLength();
		
		if(currentSize + size > maxSize && map.size() > 0)
		{
			//delete one object
			HttpResponse rmr = map.remove();
			currentSize -= HTTP_RESPONSE_HEADSIZE + rmr.getHttpHeaders().getContentLength();
		}
		
		if(currentSize + size <= maxSize)
		{

			currentSize += size;
			String key = req2keyMapper.apply(req);
			map.put(key,res);
			logger.info("add "+key+" to cache "+humanReadableByteCount(currentSize,true));
		}
	}

	private static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
}
