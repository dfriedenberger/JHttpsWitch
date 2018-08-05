package de.frittenburger.io.impl;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.frittenburger.io.interfaces.PersistenceService;

public class PersistenceServiceImpl implements PersistenceService {

	
	@Override
	public String[] listFiles(String path, String regex) {
		return new File(path).list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.matches(regex);
			}
		});
	}

	
	@Override
	public List<String> readLines(String filename) throws IOException {
		
		List<String> lines = new ArrayList<String>();

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					filename)));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.trim().equals(""))
					continue;
				lines.add(line);
			}

		} finally {
			if (in != null)
				in.close();
		}

		return lines;

	}

	@Override
	public void appendLine(String filename, String line) throws IOException {
		
		OutputStream out = null;
		try
		{
			out = new FileOutputStream(filename, true);
			out.write(line.getBytes());
			out.write('\n');
			out.flush();
		} finally {
			if(out != null)
				out.close();
			
		}

	}

}
