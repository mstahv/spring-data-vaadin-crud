### Spring Boot JPA CRUD example with Vaadin UI

This is a [simple CRUD app](https://github.com/mstahv/spring-data-vaadin-crud) 
built with Spring Boot + Vaadin. The artifact of Spring Bootprojects is by default
a jar file, which may sound bit weird for Java web developers. How to deploy the 
jar file when your server expects war files? I already had an account and 
was logged into [IBM Bluemix](http://www.bluemix.net) with CLI tools, so deploying needed
actually only this command (after building with the usual 'mvn install'):

*cf push bootexample -p target/\*.jar*

