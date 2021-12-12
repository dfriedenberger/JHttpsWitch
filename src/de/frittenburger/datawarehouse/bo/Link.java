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
import java.util.Date;

public class Link extends DataVaultItem {

	public static final String ID = "ID";

	private String foreignKeySource = null;
	private String foreignKeyTarget = null;

	public Link(Hub source, Hub target) {
		super("LNK",source.getName()+"_To_"+target.getName());
		
		super.defineField(ID, String.class , Field.PrimaryKey);
		foreignKeySource = "ID_HUB_"+source.getName();
		foreignKeyTarget = "ID_HUB_"+target.getName();
		super.defineField(foreignKeySource, String.class , Field.ForeignKey);
		super.defineField(foreignKeyTarget, String.class , Field.ForeignKey);
		super.defineField(LOAD_DTS, Date.class , 0);
		super.defineField(SRC_REC, String.class , 0);
	

		super.setValue(foreignKeySource, source.getId());
		super.setValue(foreignKeyTarget, target.getId());
		super.setValue(LOAD_DTS, new Date());
		super.setValue(SRC_REC, "unknown");
		

	}

	public String getId() {
		return (String)getValue(ID);
	}
	
	public void setId(String id) {
		setValue(ID, id);
	}

	public String[] getForeignKeys() {
		return new String[]{ foreignKeySource , foreignKeyTarget };
	}


	
	/*
	 * A Link PK (Hash Key)
	 * The PKs from the parent Hubs – used for lookups
	 * The Business Key column(s) – new feature in DV 2.0
	 * The Load Date (LOAD_DTS)
	 * The Source for the record (REC_SRC)
	 */
}
