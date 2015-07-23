/**
 * Base functions for all DominoDAO classes
 */
package com.gw.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.openntf.domino.ACL;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.gw.common.NameHelper;
import com.gw.common.log.OpenLog;
import com.ibm.commons.util.StringUtil;

/**
 * @author jda, 2013.11.13
 * 
 */
public class BaseDominoDAO {

	private Database db = null;
	private boolean viewDirty = false;
	protected boolean runAsSigner = false;
	private static OpenLog oLog = null;

	static {
		oLog = new OpenLog();
		//oLog.setDebugMode(false);
	}

	protected Session getSession() {
		if (runAsSigner) {
			return Factory.getSession(SessionType.SIGNER);
		}
		//		debug("getSession...");
		return Factory.getSession(SessionType.CURRENT);
	}

	protected boolean isUnid(String key) {
		return (null != key && key.matches("[0-9A-Fa-f]{32}"));
	}

	protected Database getDb() {
		if (null == db) {
			db = getSession().getCurrentDatabase();
		}
		return db;
	}

	protected View getView(String name) {
		return getDb().getView(name);
	}

	protected void setViewDirty(boolean b) {
		viewDirty = b;
	}

	protected boolean isViewDirty() {
		return viewDirty;
	}

	public void setRunAsSigner(boolean b) {
		runAsSigner = b;
	}

	protected boolean updateField(Document doc, String field, String value) {
		if (doc.hasItem(field)) {
			if (StringUtil.isEmpty(value)) { // "" or null
				doc.removeItem(field);
				return true;
			} else {
				// if (value.equals(doc.getItemValueString(field))) {
				if (value.equals(doc.get(field))) {
					return false;
				}
			}
		} else {
			if (StringUtil.isEmpty(value)) { // "" or null
				return false;
			}
		}
		// doc.replaceItemValue(field, value);
		doc.put(field, value);
		return true;
	}

	protected boolean updateField(Document doc, final String field, final List<?> value) {
		if (doc.hasItem(field)) {
			if (isEmptyList(value)) {
				doc.removeItem(field);
				return true;
			} else {
				value.removeAll(Arrays.asList(null, ""));
				if (value.equals(doc.getItemValue(field))) {
					return false;
				}
			}
		} else {
			if (isEmptyList(value)) {
				return false;
			}
			value.removeAll(Arrays.asList(null, ""));
		}
		// doc.replaceItemValue(field, value);
		//		debug("updateField " + field + ": " + value);
		doc.put(field, value);
		return true;
	}

	private boolean isEmptyList(List<?> value) {
		if (null == value) return true;
		if (value.size() == 0) return true;
		for (Object object : value) {
			if (null != object) {
				if (object instanceof String && StringUtil.isNotEmpty((String) object)) {
					return false;
				} else {
					// Assume other object is not "empty"
					return false;
				}
			}
		}
		return true;
	}

	protected boolean updateField(Document doc, String field, boolean value) {
		return updateField(doc, field, fromBoolean(value));
	}

	protected boolean updateField(Document doc, String field, Double value) {
		if (doc.hasItem(field)) {
			if (null == value) {
				doc.removeItem(field);
				return true;
			} else {
				if (value.equals(doc.getItemValueDouble(field))) {
					return false;
				}
			}
		} else {
			if (null == value) {
				return false;
			}
		}
		// doc.replaceItemValue(field, value);
		doc.put(field, value);
		return true;
	}

	protected boolean updateField(Document doc, String field, Integer value) {
		if (doc.hasItem(field)) {
			if (null == value) {
				doc.removeItem(field);
				return true;
			} else {
				if (value.equals(doc.getItemValueInteger(field))) {
					return false;
				}
			}
		} else {
			if (null == value) {
				return false;
			}
		}
		// doc.replaceItemValue(field, value);
		doc.put(field, value);
		return true;
	}

	protected boolean updateField(Document doc, String field, Long value) {
		if (doc.hasItem(field)) {
			if (null == value) {
				doc.removeItem(field);
				return true;
			} else {
				if (value.equals(doc.get(field))) {
					return false;
				}
			}
		} else {
			if (null == value) {
				return false;
			}
		}
		// doc.replaceItemValue(field, value);
		doc.put(field, value);
		return true;
	}

	protected boolean updateField(Document doc, String field, Date value) {
		if (doc.hasItem(field)) {
			if (null == value) {
				doc.removeItem(field);
				return true;
			} else {
				Date dt = toDate(doc.get(field));
				if (value.equals(dt)) {
					return false;
				}
			}
		} else {
			if (null == value) {
				return false;
			}
		}
		// doc.replaceItemValue(field, value);
		doc.put(field, value);
		return true;
	}

	protected String fromBoolean(boolean b) {
		return b ? "1" : "";
	}

	protected boolean toBoolean(String s) {
		return "1".equals(s);
	}

	protected Integer toInteger(Object obj) {
		Double d = toDouble(obj);
		if (null == d) {
			return null;
		} else {
			return d.intValue();
		}
	}

	protected Double toDouble(Object obj) {
		//		debug("toDouble(" + obj + ") - obj.class=" + obj.getClass().getSimpleName());
		if (null != obj) {
			if (obj instanceof Double) {
				return (Double) obj;
			}
			try {
				if (obj instanceof String) {
					String s = (String) obj;
					return Double.valueOf(s);
				}
			} catch (NumberFormatException nfe) {
				// Ignore.... Return zero
			}
		}
		return null;
	}

	protected Date toDate(Object obj) {
		//		debug("toDate(" + obj + ") - obj.class=" + obj.getClass().getSimpleName());
		if (null != obj) {
			if (obj instanceof Date) {
				return (Date) obj;
			}
			if (obj instanceof DateTime) {
				DateTime dt = (DateTime) obj;
				return dt.toJavaDate();
			}
			if (obj instanceof String) {
				String s = (String) obj;
				if (StringUtil.isEmpty(s)) {
					return null;
				}
				// Date d = new Date(s);
				Date d = null;
				try {
					d = new SimpleDateFormat("dd/mm/yyyy").parse(s);
				} catch (ParseException e) {
					// Try another format
					try {
						d = new SimpleDateFormat("dd-mm-yyyy").parse(s);
					} catch (ParseException e1) {
						// Ignore - just return null
					}
				}

				return d;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<String> toList(Object obj) {
		//		debug("toList(" + obj + ") - obj.class=" + obj.getClass().getSimpleName());
		List<String> list = new ArrayList<String>();
		if (null != obj) {
			if (obj instanceof Vector) {
				Vector v = (Vector) obj;
				for (int i = 0; i < v.size(); i++) {
					String e = (String) v.elementAt(i);
					if (StringUtil.isNotEmpty(e)) {
						list.add(e);
					}
				}
				// return new ArrayList<String>((Vector) obj);
			} else if (obj instanceof String) {
				String s = (String) obj;
				if (StringUtil.isNotEmpty(s)) {
					list.add(s);
				}
			}
		}
		return list;
	}

	protected List<Date> toDateList(Object obj) {
		//		debug("toDateList(" + obj + ") - obj.class=" + obj.getClass().getSimpleName());
		List<Date> list = new ArrayList<Date>();
		if (null != obj) {
			if (obj instanceof Vector) {
				Vector v = (Vector) obj;
				for (int i = 0; i < v.size(); i++) {
					Date dt = toDate(v.elementAt(i));
					if (null != dt) {
						list.add(dt);
					}
				}
			} else {
				Date dt = toDate(obj);
				if (null != dt) {
					list.add(dt);
				}
			}
		}
		return list;
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
	
			int userAccess = db.queryAccess(userName);
			//User has editor access or above
			if(userAccess > ACL.LEVEL_AUTHOR) return true;
			
			//User has no author access
			if(userAccess < ACL.LEVEL_AUTHOR) return false;
			
			//Get values of all Authors fields on the document
			List<NameHelper> authors = new ArrayList<NameHelper>();
			for(Item item:doc.getItems()) {
				if(item.isAuthors()) {
					@SuppressWarnings("unchecked")
					List<String> fieldValues = item.getValues(ArrayList.class);
					for(String value:fieldValues) authors.add(new NameHelper(value));
				}
			}
			oLog.logDebug(authors);	
			
			//User is member of authors field
			NameHelper user = new NameHelper(userName);
			if(authors.contains(user)) return true;
			
			//Getting all info from user (groups and roles) to compare with author field values
			Collection<NameHelper> userGroupsAndRoles = new ArrayList<NameHelper>();
			for(String group:s.getUserGroupNameCollection()) userGroupsAndRoles.add(new NameHelper(group));
			for(String role:db.queryAccessRoles(userName)) userGroupsAndRoles.add(new NameHelper(role));
					
			oLog.logDebug(userGroupsAndRoles);
			
			//For deeper analysis of the values, we loop them one by one
			for(NameHelper author:authors) {
				if(userGroupsAndRoles.contains(author)) return true;
			}
			
		} catch(Exception e) {
			oLog.logException(e, "Unable to check author access", null, doc);
		}
		return false;
	}
}
