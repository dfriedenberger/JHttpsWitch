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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataVaultItem {

	public static final String LOAD_DTS = "LOAD_DTS";
	public static final String SRC_REC = "SRC_REC";

	private final String name;
	private final String tablename;
	private final List<Field> fields = new ArrayList<Field>();
	private final Map<String, Object> values = new HashMap<String,Object>();

	public DataVaultItem(String type,String name)
	{
		this.name = name;
		this.tablename = type + "_" + name;
	}
	
	public String getName() {
		return name;
	}
	public String getTableName() {
		return tablename;
	}

	public Field[] getFields() {
		return fields.toArray(new Field[0]);
	}


	private boolean hasKey(String key) {
		
		if(key == null || key.trim().isEmpty())
			throw new RuntimeException("Invalid key "+key);
		
		
		for(Field f : fields)
			if(f.getName().equals(key)) return true;
		return false;
	}
	
	protected void defineField(String name, Class<?> clazz, int type) {		
		
		if(hasKey(name))
			throw new RuntimeException("Key "+name+" allways defined");
		
		String dt = clazz.getSimpleName();
		switch(dt)
		{
			case "Integer":
			case "Long":
			case "Boolean":
				fields.add(new Field(name,"Integer",type));
				break;
			case "String":
				fields.add(new Field(name,"Text",type));
				break;
			case "Date":
				fields.add(new Field(name,"Datetime",type));
				break;
			case "Double":
				fields.add(new Field(name,"Real",type));
				break;
			default:
				throw new RuntimeException("not implementd "+dt);
		}
	}
		

	protected void setValue(String name, Object value)
	{
		if(value == null) 
			throw new RuntimeException("value is null for key "+name);
		values.put(name,value);
	}
	
	public Object getValue(String key) {
		
		if(!hasKey(key))
			throw new RuntimeException("no such key "+key);

		if(!values.containsKey(key))
			throw new RuntimeException("no value for key "+key);
		
		return values.get(key);

	}

	


	public String getEscapedValue(String key) {
		Object value = getValue(key);
	
		String dt = value.getClass().getSimpleName();
		
		if(dt.equals("Long"))
			return ""+(Long)value;
		if(dt.equals("Integer"))
			return ""+(Integer)value;
		if(dt.equals("String"))
			return "'"+((String)value).replaceAll("[']", "''")+"'";
		if(dt.equals("Date"))
			return "'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)value)+"'";
		if(dt.equals("Boolean")) 
			return ((Boolean)value)?"1":"0";
		if(dt.equals("Double")) 
			return ""+(Double)value;
		throw new RuntimeException("not implementd "+dt);
	}
	
	
}
