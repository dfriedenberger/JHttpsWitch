package de.frittenburger.cache;

import static org.junit.Assert.*;

import org.junit.Test;

import de.frittenburger.cache.impl.CacheImpl;
import de.frittenburger.cache.impl.CacheMapImpl;
import de.frittenburger.cache.interfaces.Cache;
import de.frittenburger.cache.interfaces.CacheableTester;
import de.frittenburger.io.bo.HttpHeaders;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;
import static org.mockito.Mockito.*;

public class TestCacheImpl {

	@Test
	public void testCacheRequest() {
		
		
		
		HttpRequest req = new HttpRequest();
		
		HttpHeaders headers = req.getHttpHeaders();
		
		headers.setHost("host");
        req.setUrl("image.jpg");
        req.setMethod("GET");
        
		HttpResponse res = new HttpResponse();
		res.getHttpHeaders().setContentType("contentType");
		res.setStatus(200);		
		
		
		CacheableTester tester = mock(CacheableTester.class);
		
		when(tester.isCacheable(req)).thenReturn(true);
		
		Cache cache = new CacheImpl(500,new CacheMapImpl(),tester);		

		
		assertFalse(cache.contains(req));
		
		cache.put(req, res);
		
		assertTrue(cache.contains(req));
		HttpResponse res2 = cache.get(req);
		
		assertEquals(res, res2);
		
	}
	
	
	@Test
	public void testCacheParallelRequest() {
		
		
		
		HttpRequest req1 = new HttpRequest();
		
		req1.getHttpHeaders().setHost("host1");
        req1.setUrl("image.jpg");
        req1.setMethod("GET");
        
		HttpResponse res1 = new HttpResponse();
		res1.getHttpHeaders().setContentType("contentType");
		res1.setStatus(200);		
		
		
		HttpRequest req2 = new HttpRequest();
		
		req2.getHttpHeaders().setHost("host2");
		req2.setUrl("image.jpg");
		req2.setMethod("GET");
        
		HttpResponse res2 = new HttpResponse();
		res2.getHttpHeaders().setContentType("contentType");
		res2.setStatus(200);		
		
		
		CacheableTester tester = mock(CacheableTester.class);
		
		when(tester.isCacheable(req1)).thenReturn(true);
		when(tester.isCacheable(req2)).thenReturn(true);

		Cache cache = new CacheImpl(500,new CacheMapImpl(),tester);		

		
		assertFalse(cache.contains(req1));
		assertFalse(cache.contains(req2));

		cache.put(req1, res1);
		cache.put(req2, res2);

		assertTrue(cache.contains(req1));
		HttpResponse res1r = cache.get(req1);
		
		assertEquals(res1r, res1);
	
		
		assertTrue(cache.contains(req2));
		HttpResponse res2r = cache.get(req2);
		
		assertEquals(res2r, res2);
	}
	
	@Test
	public void testCleanupCacheRequestIfSizeIsToSmall() {
		
		
		HttpRequest req1 = new HttpRequest();
		
		req1.getHttpHeaders().setHost("host1");
        req1.setUrl("image.jpg");
        req1.setMethod("GET");
        
		HttpResponse res1 = new HttpResponse();
		res1.getHttpHeaders().setContentType("contentType");
		res1.setStatus(200);		
        res1.setContent("1234567890".getBytes()); //10 Bytes

		
		HttpRequest req2 = new HttpRequest();
		
		req2.getHttpHeaders().setHost("host2");
		req2.setUrl("image.jpg");
		req2.setMethod("GET");

		HttpResponse res2 = new HttpResponse();
		res2.getHttpHeaders().setContentType("contentType");
		res2.setStatus(200);		
		res2.setContent("1234567890".getBytes()); //10 Bytes

		
		CacheableTester tester = mock(CacheableTester.class);
		
		when(tester.isCacheable(req1)).thenReturn(true);
		when(tester.isCacheable(req2)).thenReturn(true);

		Cache cache = new CacheImpl(64 /* HTTP_RESPONSE_HEADSIZE */ + 10,new CacheMapImpl(),tester);		//Maximal 1 Entry and 10 Bytes Payload

		
		assertFalse(cache.contains(req1));
		assertFalse(cache.contains(req2));

		cache.put(req1, res1);
		
		
		cache.put(req2, res2);

		assertFalse(cache.contains(req1));
			
		
		assertTrue(cache.contains(req2));
		HttpResponse res2r = cache.get(req2);
		
		assertEquals(res2r, res2);
	}
	
	
	
	@Test
	public void testNoCacheRequest() {
		
		
		
		HttpRequest req = new HttpRequest();
		
		HttpHeaders headers = req.getHttpHeaders();
		
		headers.setHost("host");
        req.setUrl("image.jpg");
        req.setMethod("POST");
        
		HttpResponse res = new HttpResponse();
		res.getHttpHeaders().setContentType("contentType");
		res.setStatus(200);		
		
		CacheableTester tester = mock(CacheableTester.class);
		
		when(tester.isCacheable(req)).thenReturn(false);
		
		Cache cache = new CacheImpl(500,new CacheMapImpl(),tester);		
		
		assertFalse(cache.contains(req));
		
		cache.put(req, res);
		
		assertFalse(cache.contains(req));
		
		
	}
	
	

}
