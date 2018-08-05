package de.frittenburger.routing;
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
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.frittenburger.routing.impl.MatcherImpl;
import de.frittenburger.routing.interfaces.Matcher;

public class TestMatcher {

	@Test
	public void test() {

		Matcher matcher = new MatcherImpl();
		
		assertTrue(matcher.match(Arrays.asList(new String[]{"*"}), "irgendwas"));
		assertTrue(matcher.match(Arrays.asList(new String[]{"*.example.*"}), "www.example.com"));
		assertFalse(matcher.match(Arrays.asList(new String[]{"*.e*e.*"}), "www.hello.de"));

	}
	
	 
}
