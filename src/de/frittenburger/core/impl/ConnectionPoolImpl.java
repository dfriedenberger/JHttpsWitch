package de.frittenburger.core.impl;
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
import java.util.ArrayList;
import java.util.List;

import de.frittenburger.core.interfaces.ConnectionPool;
import de.frittenburger.io.interfaces.Connection;

public class ConnectionPoolImpl implements ConnectionPool {

	private final List<Connection> connectionList = new ArrayList<Connection>();
	
	@Override
	public Connection getConnection() {

		synchronized(connectionList)
		{
			// Durchlaufe die Connections und suche nach einem verfuegbarem
			for (Connection cl : connectionList) {
				if (cl.isAvailable()) {
					return cl.lock();
				}
			}
		}
		return null;
	}
	
	@Override
	public void addConnection(Connection client)
	{
		synchronized(connectionList)
		{
			connectionList.add(client);
		}
	}

	

	@Override
	public int getUsedConnectionCnt() {
		synchronized(connectionList)
		{
			int used = 0;
			for (Connection cl : connectionList) {
				if (cl.isAvailable()) continue;
				used++;
			}
			return used;
		}
	}

	@Override
	public int size() {
		synchronized(connectionList)
		{
			return connectionList.size();
		}
	}

	@Override
	public String toString() {
		return "ConnectionPoolImpl [connectionList=" + connectionList + "]";
	}
	
	

	
	

	

}
