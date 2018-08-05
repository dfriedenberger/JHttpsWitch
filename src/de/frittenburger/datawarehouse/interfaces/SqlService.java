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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import de.frittenburger.datawarehouse.bo.BusinessVault;
import de.frittenburger.datawarehouse.bo.DataVaultItem;

public interface SqlService {

	
	void open(String filename) throws SQLException, ReflectiveOperationException;
	
	void close() throws SQLException;

	
	void update(String sql) throws SQLException;
	
	List<Map<String, String>> select(String sql) throws SQLException;
	
	
	void insert(DataVaultItem[] items) throws SQLException;
	
	List<Map<String, String>> select(DataVaultItem item, String[] keys, String[] where,String values[]) throws SQLException;

	public String selectSingleOrDefault(DataVaultItem item, String key, String[] where) throws SQLException;
	
	public Map<String, String> selectLast(DataVaultItem item, String key,String timeKey) throws SQLException; 
	
	
	public void createTableIfNotExists(DataVaultItem item) throws SQLException;
	
	public void truncateTable(BusinessVault item) throws SQLException;
	
	public void insertInto(BusinessVault item, String select) throws SQLException ;
	

}
