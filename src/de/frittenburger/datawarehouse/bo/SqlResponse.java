package de.frittenburger.datawarehouse.bo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SqlResponse {

	private final List<Map<String, String>> rows;

	public SqlResponse(List<Map<String, String>> rows) {
		this.rows = rows;
	}

	public Date getSingleDate(String key) throws ParseException {
		String val = getSingle(key);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val);
	}

	public long getSingleLong(String key) {
		String val = getSingle(key);
		return Long.parseLong(val);	
	}

	public int RowCount() {
		return rows.size();
	}

	public String getString(int i, String key) {
		String val = get(i,key);
		return val;
	}

	public long getLong(int i, String key) {
		String val = get(i,key);
		return Long.parseLong(val);
	}
	
	public double getDouble(int i, String key) {
		String val = get(i,key);
		return Double.parseDouble(val);
		
	}
	
	public Date getDate(int i, String key) throws ParseException {
		String val = get(i,key);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val);
	}

	
	private String getSingle(String key) {
		if(rows.size() == 0)
			throw new RuntimeException("empty response");
		if(rows.size() != 1)
			throw new RuntimeException("response not a single row "+rows);
		
		Map<String,String> ds = rows.get(0);
		
		if(!ds.containsKey(key))
			throw new RuntimeException(key +" not found in "+ds);
		
		return ds.get(key);
	}

	private String get(int i, String key) {
		if(rows.size() == 0)
			throw new RuntimeException("empty response");

		Map<String,String> ds = rows.get(i);
		
		if(!ds.containsKey(key))
			throw new RuntimeException(key +" not found in "+ds);
		
		return ds.get(key);
	}

	
	

	

	
	
}
