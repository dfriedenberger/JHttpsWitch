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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.frittenburger.datawarehouse.bo.BusinessVault;
import de.frittenburger.datawarehouse.bo.DataVaultItem;
import de.frittenburger.datawarehouse.bo.Field;
import de.frittenburger.datawarehouse.interfaces.SqlService;

public class SqlServiceImpl implements SqlService {

	private final Logger logger = Logger.getLogger(this.getClass());

	private static final int MAXINSERT = 1000;
	private Connection conn = null;

	public SqlServiceImpl() {}

	
	@Override
	public void open(String fileName) throws SQLException, ReflectiveOperationException {
	    
		Class.forName("org.sqlite.JDBC");

		String url = "jdbc:sqlite:" + fileName;
		this.conn = DriverManager.getConnection(url);

		if (conn != null) {
			DatabaseMetaData meta = conn.getMetaData();
			logger.debug("The driver name is " + meta.getDriverName());
		}
		
	}

	@Override
	public void close() throws SQLException {
		
		if (conn != null) 
			conn.close();
		conn = null;
		
	}
	
	@Override
	public void update(String sql) throws SQLException {
		dump(sql);
	 	Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
	}
	
	@Override
	public List<Map<String, String>> select(String sql) throws SQLException {
		dump(sql);
	 	Statement stmt = conn.createStatement();
	 	ResultSet rs = stmt.executeQuery(sql);
        
	 	 ResultSetMetaData md = rs.getMetaData();
	 	  int columns = md.getColumnCount();
	 	  List<Map<String,String>> list = new ArrayList<Map<String,String>>(50);
	 	  while (rs.next()){
	 	     Map<String,String> row = new HashMap<String,String>(columns);
	 	     for(int i=1; i<=columns; ++i){           
	 	      row.put(md.getColumnName(i),rs.getString(i));
	 	     }
	 	     list.add(row);
	 	  }
		 dump(list);

	 	 return list;
	}
	
	private void dump(List<Map<String, String>> list) {
		
		int l = list.size();
		if(l > 5) 
		{
			logger.debug("Cut results to 5 (length="+l+")");		
			l = 5;
		}
		
		for(int i = 0;i < l;i++)
			logger.debug(list.get(i));		
	}

	private void dump(String sql) {
		String info = sql;
		info = info.replaceAll("\\R"," ");
		if(info.length() > 160) info = info.substring(0, 160) +" ...";
		logger.debug(info);		
	}

	
	
	
	@Override
	public void insert(DataVaultItem[] items) throws SQLException {
		
		int offset = 0;
		
		
		String fieldlist = "";
		for(Field field : items[0].getFields())
		{
			if(!fieldlist.isEmpty()) fieldlist += ",";
			fieldlist += field.getName();
		}
		
		while (offset < items.length) {
			
			

			int to = items.length;
			if (to - offset > MAXINSERT)
				to = offset + MAXINSERT;

			String values = "";
			for (int x = offset; x < to; x++) {
				values += "\r\n" + (x == offset ? " " : ",") + "(";
				String escaped = "";
				for(Field field : items[x].getFields())
				{
					if(!escaped.isEmpty()) escaped += ",";
					escaped += items[x].getEscapedValue(field.getName());
				}
				values += escaped + ")";
			}
		  	
			String sql = String.format("INSERT INTO  %s (%s) VALUES %s;",items[offset].getTableName(),fieldlist,values);
			update(sql);

			offset = to;
		}
	}
	
	
	@Override
	public List<Map<String, String>> select(DataVaultItem item, String[] keys, String[] where,String values[]) throws SQLException {
		
		if(keys.length < 1)
			throw new RuntimeException("no keys for select");
		
		if(where.length != values.length)
			throw new RuntimeException(where + " different length from "+values);
		
		String fieldlist = "";
		for(String k : keys)
		{
			if(!fieldlist.isEmpty()) fieldlist += ",";
			fieldlist += k;
		}
		
		String wherelist = "";
		
		for(int i = 0;i < where.length;i++)
		{
			if(wherelist.isEmpty()) 
				wherelist = " WHERE ";
				else
				wherelist += " AND ";
			wherelist += where[i] + "=" + values[i];
		}
		
		String sql = String.format("SELECT %s FROM %s%s;",fieldlist,item.getTableName(),wherelist);
		return select(sql);
				
		
	}

	@Override
	public String selectSingleOrDefault(DataVaultItem item, String key, String[] where) throws SQLException {
		
		
		
		List<String> values = new ArrayList<String>();
		for(int i = 0;i < where.length;i++)
			values.add(item.getEscapedValue(where[i]));
		
		List<Map<String, String>> list = select(item,new String[]{key},where,values.toArray(new String[0]));
		
		switch(list.size())
		{
		case 0: 
			return null;
		case 1:
			return list.get(0).get(key);
			default:
				throw new RuntimeException("multiple items found "+list);
		}
	}
	
	@Override
	public Map<String, String> selectLast(DataVaultItem item, String key,String timeKey) throws SQLException {
		
		
		String sql = String.format("SELECT %s , MAX(%s) as MAXTIME FROM %s WHERE %s = %s GROUP BY %s",
				key,timeKey,item.getTableName(),key,item.getEscapedValue(key),key);
		
		List<Map<String, String>> list = select(sql);

		switch(list.size())
		{
		case 0: 
			return null;
		case 1:
			break;
		default:
			throw new RuntimeException("multiple items found "+list);
		}
		
		List<Map<String, String>> list2 = select(item, new String[]{"*"} , new String[]{key, timeKey },
				new String[]{"'" + list.get(0).get(key)+"'","'"+list.get(0).get("MAXTIME")+"'"});
		switch(list.size())
		{
		case 0: 
			throw new RuntimeException("could not select  "+list);		
		case 1:
			return list2.get(0);
		default:
			throw new RuntimeException("multiple items found "+list);
		}
		
	}
	
	
	
	@Override
	public void createTableIfNotExists(DataVaultItem item) throws SQLException {
	  	
		String define = "";

		for(Field field : item.getFields())
		{
			if(!define.isEmpty()) define += ",";
			define += field.getName();
			define += " "+field.getDataType();
			//Todo Primary keys, unique , ...
		}
		
	  	String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s);",item.getTableName(),define);
	  	update(sql);		

    }

	@Override
	public void truncateTable(BusinessVault item) throws SQLException {
		
		String sql = String.format("DELETE FROM %s;",item.getTableName());
	  	update(sql);	
	  	
	}

	@Override
	public void insertInto(BusinessVault item, String select) throws SQLException {
		
		String define = "";
		for(Field field : item.getFields())
		{
			if(!define.isEmpty()) define += ",";
			define += field.getName();
		}
		
		String sql = String.format("INSERT INTO %s (%s) %s;",item.getTableName(),define,select);
	  	update(sql);	

	}

	

}
