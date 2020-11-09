# CAMUNDA AZURE SSO BOILERPLATE
This is the boilerplate code for camunda with Azure AD SSO. This code is based on this article: https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-active-directory

## Environment Variables
Please provide following environment variables in `application.yaml` file:
```
- azure.activedirectory.tenant-id
- spring.security.oauth2.client.registration.azure.client-id
- spring.security.oauth2.client.registration.azure.client-secret
- domainNameForCreatingNewUsers	// If you want to create new users who are logged into their Microsoft Account through specific domain (for example @finexio.com), then you can provide this value equal to the domain i.e. "@finexio.com". Otherwise leave it empty if you do not want to create new users.
- useAzureRolesAsGroups	// Set it to true if you want to use Azure roles for Camunda Authentication Groups.
```

## Licence
MIT