package com.gw.application.rest;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gw.common.log.OpenLog;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.xsp.extlib.component.rest.CustomService;
import com.ibm.xsp.extlib.component.rest.CustomServiceBean;

public class RestAccount extends CustomServiceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.ibm.xsp.extlib.component.rest.CustomServiceBean#renderService(com.ibm.xsp.extlib.component.rest.CustomService, com.ibm.domino.services.rest.RestServiceEngine)
	 */
	@Override
	public void renderService(CustomService service, RestServiceEngine engine)
			throws ServiceException {
		
		
		HttpServletRequest request = engine.getHttpRequest();
        HttpServletResponse response = engine.getHttpResponse();
 
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
 
        OpenLog oLog = new OpenLog();
        oLog.logDebug(request.getMethod());
        oLog.logDebug(request.getParameterMap());
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
	}
	

}
