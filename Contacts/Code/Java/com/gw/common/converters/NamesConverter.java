package com.gw.common.converters;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.openntf.domino.*;
import org.openntf.domino.utils.*;
import org.openntf.domino.utils.Factory.SessionType;

import com.gw.common.log.OpenLog;

public class NamesConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	    
		String[] names = value.split(",");
	    ArrayList<String> canonicals = new ArrayList<String>();
	    Session s = Factory.getSession(SessionType.CURRENT);
	    
	    
	    for(String name:names) {
	    	Name userName = s.createName(name.trim());
	    	if(userName.getAbbreviated().equals(userName.getCommon())) {
	    		//No hierarchical name
	    		canonicals.add(name);
	    	} else {
	    		//Hierarchical name
	    		canonicals.add(userName.getCanonical());
	    	}
	    }
	    return canonicals;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		OpenLog oLog = new OpenLog();
		oLog.logDebug(value.getClass().toString());
		
		return value.toString();    
	}

}
