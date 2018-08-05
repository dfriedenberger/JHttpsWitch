package de.frittenburger.parser.bo;

import java.util.regex.Pattern;

public class UserAgent {
	
	private Pattern[] pattern    = null;
	private boolean robot    = false;
	private String description = null;
	
	public Pattern[] getPattern() {
		return pattern;
	}
	public void setPattern(Pattern[] pattern) {
		this.pattern = pattern;
	}
	public boolean isRobot() {
		return robot;
	}
	public void setRobot(boolean robot) {
		this.robot = robot;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static UserAgent create(String[] patternStr, boolean robot, String description) {

		Pattern pattern[] = new Pattern[patternStr.length];
		for(int i = 0;i < pattern.length;i++)
			pattern[i] = Pattern.compile(patternStr[i]);
		
		UserAgent userAgent = new UserAgent();
		userAgent.setPattern(pattern);
		userAgent.setRobot(robot);
		userAgent.setDescription(description);

		return userAgent;
	}

}
