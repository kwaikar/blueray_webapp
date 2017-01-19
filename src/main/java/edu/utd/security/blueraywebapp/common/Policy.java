package edu.utd.security.blueraywebapp.common;

import java.util.Collections;
/**
 * PoJo for holding Policy information.
 * @author kanchan
 *
 */
public class Policy {

	private final String filePath;
	private final String regex;
	private final String priviledge;
	
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}
	/**
	 * @return the priviledge
	 */
	public String getPriviledge() {
		return priviledge;
	}
	public Policy(String filePath, String regex, String priviledge) {
		super();
		this.filePath = filePath;
		this.regex = regex;
		this.priviledge = priviledge;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\""+"filePath"+"\""+":"+"\""+filePath+"\",");
		sb.append("\""+"regex"+"\""+":"+"\""+regex.replaceAll("\\\\", "\\\\\\\\")+"\",");
		sb.append("\""+"priviledge"+"\""+":"+"\""+priviledge+"\"");
		sb.append("}");
		return sb.toString();
	}
}
