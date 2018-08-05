package de.frittenburger.parser.impl;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.frittenburger.parser.bo.UserAgent;
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
import de.frittenburger.parser.interfaces.UserAgentParser;

public class UserAgentParserImpl implements UserAgentParser {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private List<UserAgent> agents = null;

	
	public void load(List<UserAgent> agents)
	{
		this.agents = agents;
	}
	
	@Override
	public String parse(String browserAgentString) {
		
		if(browserAgentString == null)
			return "Empty";
		if(browserAgentString.isEmpty())
			return "Empty";
		
		for(UserAgent agent : agents)
		{
			for(Pattern pa : agent.getPattern())
			{
				if(pa.matcher(browserAgentString).find())
					return agent.getDescription();
			}
		}
		
		logger.warn("Unknown Agent "+browserAgentString);
		
		return "Unknown";
	}

}
