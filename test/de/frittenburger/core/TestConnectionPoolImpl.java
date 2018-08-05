package de.frittenburger.core;
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

import org.junit.Test;

import de.frittenburger.core.interfaces.ConnectionPool;
import de.frittenburger.io.interfaces.Connection;
import de.frittenburger.core.impl.ConnectionPoolImpl;

import static org.mockito.Mockito.*;

public class TestConnectionPoolImpl {

	@Test
	public void test() {
		
		Connection client1 = mock(Connection.class);
		when(client1.isAvailable()).thenReturn(false);

		Connection client2 = mock(Connection.class);
		when(client2.isAvailable()).thenReturn(true);
		when(client2.lock()).thenReturn(client2);

		ConnectionPool pool = new ConnectionPoolImpl();
		
		pool.addConnection(client1);
		pool.addConnection(client2);

		assertEquals(2, pool.size());
		assertEquals(1, pool.getUsedConnectionCnt());
		
		
		
		Connection cl = pool.getConnection();
		assertEquals(client2, cl);

	
		
		
	}

}
