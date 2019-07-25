package de.frittenburger.cache;

import static org.junit.Assert.*;

import org.junit.Test;

import de.frittenburger.cache.impl.CacheMapImpl;
import de.frittenburger.cache.interfaces.CacheMap;
import de.frittenburger.io.bo.HttpResponse;

public class TestCacheMapImpl {

	@Test
	public void test() {
		
		CacheMap map = new CacheMapImpl();
		
		HttpResponse r1 = new HttpResponse();
		HttpResponse r2 = new HttpResponse();

		map.put("k1", r1);
		assertEquals(r1,map.get("k1"));
		assertTrue(map.containsKey("k1"));

		
		map.put("k2", r2);
		assertEquals(r2,map.get("k2"));
		assertTrue(map.containsKey("k2"));
		
		assertEquals(r1,map.get("k1"));
		map.remove();
		assertTrue(map.containsKey("k1"));
		assertFalse(map.containsKey("k2"));

		
		map.remove();
		
		assertFalse(map.containsKey("k1"));
		assertFalse(map.containsKey("k2"));
		assertEquals(0,map.size());



		
	}

}
