FROM openjdk:11
ADD target ./
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "finexio-camunda.jar"]