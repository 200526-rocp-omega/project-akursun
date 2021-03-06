package com.project.authorization;

import javax.servlet.http.HttpSession;

import com.project.exceptions.NotLoggedInException;
import com.project.exceptions.RoleNotAllowedException;
import com.project.models.User;

public class AuthService {

	public static void guard(HttpSession session, String...roles) {
		User currentUser = session == null ? null : (User) session.getAttribute("currentUser");
		if(session == null || currentUser == null) {
			throw new NotLoggedInException();
		}
		
		boolean found = false;
		String role = currentUser.getRole().getRole();
		for(String allowedRole : roles) {
			if(allowedRole.equals(role)) {
				found = true;
				break;
			}
		}
		
		if(!found) {
			throw new RoleNotAllowedException();
		}
	}
	
	public static void guard(HttpSession session, int id, String...roles) {
		try {
			guard(session, roles);
		} catch(RoleNotAllowedException e) {
			User current = (User) session.getAttribute("currentUser");
			if(id != current.getId()) {
				throw e;
			}
		}
	}
}