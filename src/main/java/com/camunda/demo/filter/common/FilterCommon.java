package com.camunda.demo.filter.common;

import java.util.List;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.User;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.camunda.demo.config.ApplicationConfig;

public class FilterCommon {
	
	private ApplicationConfig appConfig = new ApplicationConfig();
	
	public String getUsernameByUserEmail(String userEmail, ProcessEngine engine) {
		List<User> users = engine.getIdentityService().createUserQuery().list();

		String username = "";
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getEmail().equalsIgnoreCase(userEmail)) {
				username = users.get(i).getId();
				break;
			}
		}
		
		String domainNameForCreatingNewUsers = appConfig.domainNameForCreatingNewUsers;
		
		if(domainNameForCreatingNewUsers != null && !domainNameForCreatingNewUsers.isEmpty()) {			
			// try to create a new user
			if( (username == null || username.isEmpty()) && userEmail.toLowerCase().endsWith(domainNameForCreatingNewUsers)) {
				String newUsername = userEmail.replace(domainNameForCreatingNewUsers, "");
				User newUser = engine.getIdentityService().newUser(newUsername);
				newUser.setEmail(userEmail);
				engine.getIdentityService().saveUser(newUser);
				username = userEmail;
			}
		}
		
		return username;
	}
	
	public List<String> getUserGroups(Authentication authentication){

        List<String> groupIds;

        groupIds = authentication.getAuthorities().stream()
                .map(res -> res.getAuthority())
                .map(res -> res.substring(5)) // Strip "ROLE_"
                .collect(Collectors.toList());

        return groupIds;
    }

}
