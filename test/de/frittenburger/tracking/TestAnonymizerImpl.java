package de.frittenburger.tracking;
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

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import de.frittenburger.tracking.impl.AnonymizerImpl;
import de.frittenburger.tracking.interfaces.Anonymizer;

public class TestAnonymizerImpl {

	@Test
	public void test() throws UnknownHostException {
		Anonymizer anonymizer = new AnonymizerImpl();
		
		InetAddress addr = InetAddress.getByName("192.23.57.23");
		
		String anonymIp = anonymizer.anonymizeIpAddress(addr.getHostAddress());
		assertEquals("192.23.57.0", anonymIp);
	}

}
