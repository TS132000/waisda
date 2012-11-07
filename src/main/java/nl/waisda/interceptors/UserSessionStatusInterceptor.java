/*  This file is part of Waisda 

    Copyright (c) 2012 Netherlands Institute for Sound and Vision
    https://github.com/beeldengeluid/waisda
	
    Waisda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Waisda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Waisda.  If not, see <http://www.gnu.org/licenses/>.
*/

package nl.waisda.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.waisda.domain.User;
import nl.waisda.services.UserSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


@Component
public class UserSessionStatusInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserSessionService userSessionService;
	
	public enum UserSessionStatus {
		NOT_AUTHENTICATED,
		AUTHENTICATED;
	}
	
	private UserSessionStatus desiredStatus;
	
	private String action = "/";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		User user = userSessionService.getCurrentUser(request.getSession());
		
		// Detect current status
		UserSessionStatus currentStatus = null;
		if (user == null) {
			currentStatus = UserSessionStatus.NOT_AUTHENTICATED;
		}
		else {
			currentStatus = UserSessionStatus.AUTHENTICATED;
		}
		
		// Status equality check
		if (currentStatus != getDesiredStatus()) {
			throw new ModelAndViewDefiningException(new ModelAndView("redirect:" + getAction()));
		}
		
		return true;
	}

	/*
	 * Getters and setters
	 */
	
	public UserSessionStatus getDesiredStatus() {
		return desiredStatus;
	}

	public void setDesiredStatus(UserSessionStatus desiredStatus) {
		this.desiredStatus = desiredStatus;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
