package com.camunda.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

	@Value("${domainNameForCreatingNewUsers}")
	public String domainNameForCreatingNewUsers;
	
	@Value("#{new Boolean('${useAzureRolesAsGroups}')}")
	public boolean useAzureRolesAsGroups;
	
	
}
