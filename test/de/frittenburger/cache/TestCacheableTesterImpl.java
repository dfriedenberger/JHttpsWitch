package de.frittenburger.cache;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.frittenburger.cache.impl.CacheableTesterImpl;
import de.frittenburger.cache.interfaces.CacheableTester;
import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.io.bo.HttpRequest;

public class TestCacheableTesterImpl {

	@Test
	public void testGetImageCacheable() {
		
		HttpRequest req = new HttpRequest();
		
		HttpHeaders headers = req.getHttpHeaders();
		
		headers.setHost("host");
        req.setUrl("image.jpg");
        req.setMethod("GET");
        
        Set<String> extensions = new HashSet<>();
        extensions.add("jpg");
		CacheableTester tester = new CacheableTesterImpl(extensions);
		
		assertTrue(tester.isCacheable(req));

		
	
	}
	
	@Test
	public void testGetImageNoCacheable() {
		
		HttpRequest req = new HttpRequest();
		
		HttpHeaders headers = req.getHttpHeaders();
		
		headers.setHost("host");
        req.setUrl("image.jpg");
        req.setMethod("GET");
		
        Set<String> extensions = new HashSet<>();
        extensions.add("css");
		CacheableTester tester = new CacheableTesterImpl(extensions);		
		assertFalse(tester.isCacheable(req));

		
	
	}
	
	@Test
	public void testPostNotCacheable() {
		
		HttpRequest req = new HttpRequest();
		
		HttpHeaders headers = req.getHttpHeaders();
		
		headers.setHost("host");
        req.setUrl("image.jpg");
        req.setMethod("POST");
		
        Set<String> extensions = new HashSet<>();
        extensions.add("jpg");
		CacheableTester tester = new CacheableTesterImpl(extensions);
		
		
		assertFalse(tester.isCacheable(req));

		
	
	}
	
	@Test
	public void testParamsNotCacheable() {
		
		HttpRequest req = new HttpRequest();
		
		HttpHeaders headers = req.getHttpHeaders();
		
		headers.setHost("host");
        req.setUrl("image.jpg?param=value");
        req.setMethod("GET");
		
        Set<String> extensions = new HashSet<>();
        extensions.add("jpg");
		CacheableTester tester = new CacheableTesterImpl(extensions);
		
		
		assertFalse(tester.isCacheable(req));

		
	
	}

}
