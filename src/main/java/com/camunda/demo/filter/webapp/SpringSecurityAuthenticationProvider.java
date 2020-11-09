package com.camunda.demo.filter.webapp;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.camunda.demo.config.ApplicationConfig;
import com.camunda.demo.filter.common.FilterCommon;

import javax.servlet.http.HttpServletRequest;

public class SpringSecurityAuthenticationProvider extends ContainerBasedAuthenticationProvider {
	
	private ApplicationConfig appConfig = new ApplicationConfig();
	
	private FilterCommon filterCommon = new FilterCommon();

    @Override
    public AuthenticationResult extractAuthenticatedUser(HttpServletRequest request, ProcessEngine engine) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	if (authentication == null) {
            return AuthenticationResult.unsuccessful();
        }
    	
    	// get azure active directory logged in user information
		DefaultOidcUser oidcUser = 
				(DefaultOidcUser) authentication.getPrincipal();
		String userEmail = oidcUser.getAttributes().get("unique_name").toString();
		String username = filterCommon.getUsernameByUserEmail(userEmail, engine);

        if (username == null || username.isEmpty()) {
            return AuthenticationResult.unsuccessful();
        }

        AuthenticationResult authenticationResult = new AuthenticationResult(username, true);
        
        boolean useAzureRolesAsGroups = appConfig.useAzureRolesAsGroups;
        if (useAzureRolesAsGroups) {        	
        	authenticationResult.setGroups(filterCommon.getUserGroups(authentication));
        }

        return authenticationResult;
    }
}
