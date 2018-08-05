package de.frittenburger.datawarehouse;
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
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.junit.Test;

import de.frittenburger.datawarehouse.bo.DataVaultItem;
import de.frittenburger.datawarehouse.bo.Hub;
import de.frittenburger.datawarehouse.impl.DataWarehouseImpl;
import de.frittenburger.datawarehouse.interfaces.DataWarehouse;
import de.frittenburger.datawarehouse.interfaces.SqlService;

public class TestDataWarehouseImpl {

	@Test
	public void test() throws SQLException {
		
		SqlService sqlService = mock(SqlService.class);
		DataWarehouse dataWarehouse = new DataWarehouseImpl(sqlService);

		Hub hub = new Hub("xxx");
		hub.setBusinessKey("key", "value");
		dataWarehouse.createHubIfNotExists(hub);
		
		assertNotNull(hub.getId());
		
		
		verify(sqlService).createTableIfNotExists(any(DataVaultItem.class));
	}

}
