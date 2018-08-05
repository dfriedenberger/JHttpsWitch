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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class BusinessVault extends DataVaultItem {

	private String select = null;

	public BusinessVault(String name) {
		super("BV",name);
	}

	public void defineText(String key) {
		super.defineField(key, String.class, 0);
	}

	public void defineInteger(String key) {
		super.defineField(key, Integer.class, 0);
	}

	public void defineDouble(String key) {
		super.defineField(key, Double.class, 0);
	}

	public void defineDatetime(String key) {
		super.defineField(key, Date.class, 0);
	}

	public void defineBoolean(String key) {
		super.defineField(key, Boolean.class, 0);		
	}
	
	public void load(String file) throws IOException {
		select = readFile(file);
	}
	
	public static String readFile(String path) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}

	public String getSelect() {
		return select;
	}

	
	
}
