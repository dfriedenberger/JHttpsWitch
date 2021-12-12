package de.frittenburger.datawarehouse.impl;
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.datawarehouse.bo.BusinessVault;
import de.frittenburger.datawarehouse.bo.DataVaultItem;
import de.frittenburger.datawarehouse.bo.Hub;
import de.frittenburger.datawarehouse.bo.Link;
import de.frittenburger.datawarehouse.bo.Satellite;
import de.frittenburger.datawarehouse.bo.SqlResponse;
import de.frittenburger.datawarehouse.interfaces.DataWarehouse;
import de.frittenburger.datawarehouse.interfaces.SqlService;

public class DataWarehouseImpl implements DataWarehouse {

	private final Logger logger = LogManager.getLogger(this.getClass());
	private final SqlService service;

	public DataWarehouseImpl(SqlService service) {
		this.service = service;
	}


	@Override
	public void createHubIfNotExists(Hub hub) throws SQLException {
		
		service.createTableIfNotExists(hub);
		String id = service.selectSingleOrDefault(hub,Hub.ID,hub.getBusinessKeys());
		if(id == null)
		{
			hub.setId(UUID.randomUUID().toString());
			service.insert(new DataVaultItem[]{ hub });
		}
		else
		{
			hub.setId(id);
		}
		
	}

	

	@Override
	public void createSatelliteTable(Satellite satellite) throws SQLException {
		service.createTableIfNotExists(satellite);		
	}

	@Override
	public void updateSatellite(Satellite satellite) throws SQLException {
		
		service.createTableIfNotExists(satellite);
		Map<String,String> m = service.selectLast(satellite,satellite.getForeignKey(),Satellite.LOAD_DTS);
		
		if(m != null)
		{
			boolean differente = false;
			for(String key : satellite.getAttributeKeys())
			{
				String val1 = satellite.getEscapedValue(key);
				String val2 = m.get(key);
				if(val1.startsWith("'"))
					val2 = "'"+val2+"'";
				logger.debug("Compare "+val1 + " == "+val2);
				
				if(!val1.equals(val2))
					differente = true;
			}
			if(!differente) return;
			
			
		}
	
		service.insert(new DataVaultItem[]{ satellite });
	}


	@Override
	public void addLink(Link link) throws SQLException {
		service.createTableIfNotExists(link);
		String id = service.selectSingleOrDefault(link,Link.ID,link.getForeignKeys());
		if(id == null)
		{
			link.setId(UUID.randomUUID().toString());
			service.insert(new DataVaultItem[]{ link });
		}
		else
		{
			link.setId(id);
		}
	}


	@Override
	public void calculate(BusinessVault bv) throws SQLException {
		service.createTableIfNotExists(bv);
		service.truncateTable(bv);
		service.insertInto(bv,bv.getSelect());
	}



	@Override
	public SqlResponse executeQuery(String path) throws SQLException, IOException {
		
		String sql = BusinessVault.readFile(path);
		return new SqlResponse(service.select(sql));
	}


}
