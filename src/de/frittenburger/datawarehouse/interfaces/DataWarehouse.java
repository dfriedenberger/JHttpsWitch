package de.frittenburger.datawarehouse.interfaces;
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

import de.frittenburger.datawarehouse.bo.BusinessVault;
import de.frittenburger.datawarehouse.bo.Hub;
import de.frittenburger.datawarehouse.bo.Link;
import de.frittenburger.datawarehouse.bo.Satellite;
import de.frittenburger.datawarehouse.bo.SqlResponse;

public interface DataWarehouse {

	//http://www.vertabelo.com/blog/technical-articles/data-vault-series-data-vault-2-0-modeling-basics

	void createHubIfNotExists(Hub hub) throws SQLException;

	void createSatelliteTable(Satellite sIndex) throws SQLException;

	void updateSatellite(Satellite satellite) throws SQLException;

	void addLink(Link link) throws SQLException;
	
	void calculate(BusinessVault bv) throws SQLException;


	SqlResponse executeQuery(String sql) throws SQLException, IOException;


}
