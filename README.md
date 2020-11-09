# CAMUNDA
Camunda project

## DOCKER

### Build latest jar file
Dockerfile uses jar file built using maven. So run command `mvn clean install` in the root folder. It will build `finexio-camunda.jar` at path `/target/finexio-camunda.jar`

### Build docker image
Run this command in root folder: `docker build -f Dockerfile -t finexio-camunda .`

### Check if image was generated successfully
Run command `docker images` to see if image was generated or not

### Run docker image
Following is the command to run camunda in docker. Environment variables are provided using `-e`. On localhost, database url can be `jdbc:postgresql://host.docker.internal:5432/<database_name>`. Please provide environment variables in following command before running it.
```
docker run -p 8000:8080 -e camunda.bmp.database.type=postgres -e spring.datasource.password='<password>' -e spring.datasource.url=jdbc:postgresql://host.docker.internal:5432/camunda -e spring.datasource.username=postgres -e azure.activedirectory.tenant-id=<tenant-id> -e azure.activedirectory.user-group.allowed-groups=User -e security.oauth2.client.registration.azure.client-id=<client-id> -e security.oauth2.client.registration.azure.client-secret=client-secret -t finexio-camunda
```