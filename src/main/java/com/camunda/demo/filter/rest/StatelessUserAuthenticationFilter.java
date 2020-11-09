package com.camunda.demo.filter.rest;


import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.util.EngineUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.camunda.demo.config.ApplicationConfig;
import com.camunda.demo.filter.common.FilterCommon;

import javax.servlet.*;
import java.io.IOException;

public class StatelessUserAuthenticationFilter implements Filter {
	
private ApplicationConfig appConfig = new ApplicationConfig();
	
	private FilterCommon filterCommon = new FilterCommon();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Current limitation: Only works for the default engine
        ProcessEngine engine = EngineUtil.lookupProcessEngine("default");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // get azure active directory logged in user information
 		DefaultOidcUser oidcUser = 
 				(DefaultOidcUser) authentication.getPrincipal();
 		String userEmail = oidcUser.getAttributes().get("unique_name").toString();
 		String username = filterCommon.getUsernameByUserEmail(userEmail, engine);
 		
 		if (username == null || username.isEmpty()) {
            return;
        }

        try {
        	
        	boolean useAzureRolesAsGroups = appConfig.useAzureRolesAsGroups;
            if (useAzureRolesAsGroups) {        	
            	engine.getIdentityService().setAuthentication(username, filterCommon.getUserGroups(authentication));
            } else {            	
            	engine.getIdentityService().setAuthenticatedUserId(username);
            }
        	
            chain.doFilter(request, response);
        } finally {
            clearAuthentication(engine);
        }

    }

    @Override
    public void destroy() {

    }

    private void clearAuthentication(ProcessEngine engine) {
        engine.getIdentityService().clearAuthentication();
    }

}
