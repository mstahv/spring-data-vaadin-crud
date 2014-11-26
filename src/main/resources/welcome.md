# Spring Boot JPA CRUD example with Vaadin UI

This is a [simple CRUD app](https://github.com/mstahv/spring-data-vaadin-crud) 
built with Spring Boot + Vaadin. The artifact of the boot projects is by default
a jar file, which may sound bit weird for Java web developers. But,
this makes some things so easy you wont even guess. I already had an account, so
deploying this to [IBM Bluemix](http://www.bluemix.net) was just one command was 
required (after the usual 'mvn install'): *cf push bootexample -p target/\*.jar*

What really happens is that the [Cloudfoundry](http://www.cloudfoundry.org/), 
upon which Bluemix is based, recognizes this as a java application and executes 
the jar file. The front proxy in Bluemix connects automatically 
the 8080 port opened by the embedded Tomcat to your end users. All done.

