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

public class Hub extends DataVaultItem {


	public static final String ID = "ID";
	
	private final List<String> businessKeys = new ArrayList<String>();

	public Hub(String name) {
		
		super("HUB",name);
		super.defineField(ID, String.class , Field.PrimaryKey);
		super.defineField("LOAD_DTS", Date.class , 0);
		super.defineField("SRC_REC", String.class , 0);	
		
		super.setValue("LOAD_DTS", new Date());
		super.setValue("SRC_REC", "unknown");
		
	}
	
	public String getId() {
		return (String)getValue(ID);
	}
	
	public void setId(String id) {
		setValue(ID,id);
		
	}
	public void setBusinessKey(String key, String value) {
		
		if(businessKeys.contains(key))
			throw new RuntimeException("key "+key+" allways defined");
		businessKeys.add(key);
		
		super.defineField(key, String.class , Field.BusinessKey);	
		super.setValue(key,value);
		
	}

	public void setBusinessKey(String key, long value) {
		if(businessKeys.contains(key))
			throw new RuntimeException("key "+key+" allways defined");
		businessKeys.add(key);
		
		super.defineField(key, Long.class , Field.BusinessKey);	
		super.setValue(key,value);
	}

	public String[] getBusinessKeys() {
		if(businessKeys.isEmpty())
			throw new RuntimeException("no business keys defined");
		return businessKeys.toArray(new String[0]);
	}

	public void setSRC_REC(String source) {
		super.setValue("SRC_REC", source);
	}

	



	




	

	/*
	 * HUB_
	 * A Hub PK
	 * The Business Key column(s)
	 * The Load Date (LOAD_DTS)
	 * The Source for the record (REC_SRC)
	 */
}
