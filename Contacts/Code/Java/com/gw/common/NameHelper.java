package com.gw.common;

import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * @author Tom Van Aken
 * Helper class for Domino Names
 */
public class NameHelper {
	private String fullName;
	private String commonName;
	
	public NameHelper(String name) {
		
		if(name.contains("/")) {
			//This is a hierarchical name, name conversion required
			Session s = Factory.getSession(SessionType.CURRENT);
			Name n = s.createName(name.trim());
			fullName = n.getCanonical();
			commonName = n.getAbbreviated();
		} else {
			//Non hierarchical name
			fullName = name;
			commonName = name;
		}
		
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NameHelper) {
			return ((NameHelper)obj).fullName.equalsIgnoreCase(fullName);
		}
		//No NameHelper object
		return false;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return fullName.toLowerCase().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return commonName;
	}
	
	public String getCommonName() {
		return commonName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
}