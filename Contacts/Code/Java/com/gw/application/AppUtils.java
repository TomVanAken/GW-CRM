package com.gw.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.openntf.domino.*;
import org.openntf.domino.utils.*;
import org.openntf.domino.utils.Factory.SessionType;

import com.gw.common.log.OpenLog;


/**
 * @author Tom Van Aken
 * Application specific Utilities
 */
public class AppUtils {

	private static OpenLog oLog = new OpenLog();
	
	static {
		oLog.setDebugMode(false);
	}
	/**
	 * Checks if current user is an author for the given document
	 * @param doc the document to check for
	 * @return true if one of the following conditions apply:
	 * 	- user is editor or above on the document database
	 *  - user is at least author and
	 *  --- has a role contained in one of the Authors fields
	 *  --- is member in one of the Authors fields
	 *  --- is member in one of the Authors fields
	 */
	
	public static boolean isAuthor(Document doc) {
		try {
			oLog.logDebug(doc.toString());
			Session s = Factory.getSession(SessionType.CURRENT);
			String userName = s.getEffectiveUserName();
			Database db = doc.getParentDatabase();
			oLog.logDebug(db.getTitle());
			int userAccess = db.queryAccess(userName);
			//User has editor access or above
			if(userAccess > ACL.LEVEL_AUTHOR) return true;
			
			//User has no author access
			if(userAccess < ACL.LEVEL_AUTHOR) return false;
			
			//Get values of all Authors fields on the document
			List<String> authors = new ArrayList<String>();
			for(Item item:doc.getItems()) {
				if(item.isAuthors()) {
					@SuppressWarnings("unchecked")
					List<String> fieldValues = item.getValues(ArrayList.class);
					authors.addAll(fieldValues);
				}
			}
			oLog.logDebug(authors);	
			
			//User is member of authors field
			if(authors.contains(userName)) return true;
			
			//Getting all info from user (groups and roles) to compare with field values in detail
			Collection<String> userGroupsAndRoles = s.getUserGroupNameCollection();
			userGroupsAndRoles.addAll(db.queryAccessRoles(userName));
			userGroupsAndRoles.add(s.getEffectiveUserName());
			oLog.logDebug(userGroupsAndRoles);
			//For deeper analysis of the values, we loop them one by one
			Vector<String> authorsUCase = new Vector<String>(); //Create this Collection already for faster exhaustive check later on
			for(String author:authors) {
				if(userGroupsAndRoles.contains(author)) return true;
				if(!author.startsWith("[")) {
					//This is not a role, conversion to canonical name for further testing
					Name name = s.createName(author);
					String canonical = name.getCanonical();
					String abbreviate = name.getAbbreviated();
					if(userGroupsAndRoles.contains(canonical) || userGroupsAndRoles.contains(abbreviate)) return true;
					authorsUCase.add(canonical.toUpperCase());
					authorsUCase.add(abbreviate.toUpperCase());
				}
				authorsUCase.add(author.toUpperCase());
								
			}
			
			//Try again to make sure it is not a Case issue - we have to iterate userGroupsAndRoles to test against UCase Author collection
			for(String userGroup:userGroupsAndRoles) {
				String ugUCase = userGroup.toUpperCase();
				if(authorsUCase.contains(ugUCase)) return true;
			}
			
		} catch(Exception e) {
			oLog.logException(e, "Unable to check author access", null, doc);
		}
		return false;
	}
	
}
