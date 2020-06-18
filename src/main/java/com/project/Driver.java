package com.project;

import com.project.dao.IUserDAO;
import com.project.dao.UserDAO;
import com.project.models.Role;
import com.project.models.User;


public class Driver {

	public static void main(String[] args) {
		IUserDAO dao = new UserDAO();
		
		User user = new User(0, "username2", "password", "first", "last", "email2@yahoo.com", new Role(1,"Standard"));
		System.out.println(dao.insert(user));
		
		
		for (User u : dao.findAll()) {
			System.out.println(u);
		}
		
		
	}

}