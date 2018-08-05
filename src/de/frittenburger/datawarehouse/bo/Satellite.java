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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Satellite extends DataVaultItem {

	private String foreignKey = null;
	private List<String> attributeKeys = new ArrayList<String>();
	

	public Satellite(String name, Hub hub) {
		
		super("SAT",name);
		foreignKey = "ID_HUB_"+hub.getName();
		super.defineField(foreignKey, String.class , Field.ForeignKey);
		super.defineField("LOAD_DTS", Date.class , 0);
		super.defineField("SRC_REC", String.class , 0);
		
		super.setValue(foreignKey, hub.getId());
		super.setValue("LOAD_DTS", new Date());
		super.setValue("SRC_REC", "unknown");
	}
	
	public Satellite(String name, Link lnk) {
		super("SAT",name);
		foreignKey = "ID_LNK_"+lnk.getName();

		super.defineField(foreignKey, String.class , Field.ForeignKey);
		super.defineField("LOAD_DTS", Date.class , 0);
		super.defineField("SRC_REC", String.class , 0);
		
		super.setValue(foreignKey, lnk.getId());
		super.setValue("LOAD_DTS", new Date());
		super.setValue("SRC_REC", "unknown");
		
	}

	public void setAttribute(String key, long value) {
		
		if(attributeKeys.contains(key))
			throw new RuntimeException("key "+key+" allways defined");
		attributeKeys.add(key);
		super.defineField(key, Long.class , 0);	
		super.setValue(key,value);		
	}

	public void setAttribute(String key, String value) {
		setAttribute(key, String.class , value);	
	}

	public void setAttribute(String key, double value) {
		setAttribute(key, Double.class , value);	
	
	}

	public void setAttribute(String key, Date value) {
		setAttribute(key, Date.class , value);	
	}
	
	public void setAttribute(String key, boolean value) {
		setAttribute(key, Boolean.class , value);	
	}
	
	public void setAttribute(String key, Class<?> clazz, Object value) {
		if(attributeKeys.contains(key))
			throw new RuntimeException("key "+key+" allways defined");
		attributeKeys.add(key);
		super.defineField(key, clazz , 0);	
		super.setValue(key,value);		
	}

	public void setSRC_REC(String source) {
		super.setValue("SRC_REC", source);
	}
	
	public void setLOAD_DTS(Date date) {
		super.setValue("LOAD_DTS", date);
	}
	
	public String getForeignKey() {
		return foreignKey;
	}	
	
	public String[] getAttributeKeys() {
		return attributeKeys.toArray(new String[0]);
	}

	

	


	
	/*
	 * SAT_
	 * PK: FK from the Hub
	 * PK: The Load Date (LOAD_DTS)
	 * descriptive Columns
	 * The Source for the record (REC_SRC)
	 * 
	 */
}
