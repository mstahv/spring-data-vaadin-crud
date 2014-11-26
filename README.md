# Spring Data JPA CRUD with Vaadin

A super simple single table CRUD example with [Spring Data JPA](http://projects.spring.io/spring-data-jpa/) and [Vaadin](https://vaadin.com). Uses [Spring Boot](http://projects.spring.io/spring-boot/) for easy project setup and development. Helps you to get started with basic JPA backed applications and [vaadin4spring](https://github.com/peholmst/vaadin4spring) integration library.

For larger applications, consider applying some commonly known design patterns for your UI code. Check e.g. [this MVP example](https://github.com/peholmst/vaadin4spring/tree/master/spring-vaadin-mvp).

## How to play with this example

### Suggested method

* Clone the project
* Import to your favorite IDE
* Execute the main method from Application class

### Just execute it

```
git clone https://github.com/mstahv/spring-data-vaadin-crud.git
cd spring-data-vaadin-crud
mvn spring-boot:run
```

### Just deploy it

The built jar file is really simple to deploy in modern PaaS services. E.g. if you have existing Bluemix account and are already logged in with your cf (CLI) tools just execute following:

```
git clone https://github.com/mstahv/spring-data-vaadin-crud.git
cd spring-data-vaadin-crud
mvn install
cf push choose-namefor-your-server-here -p target/*.jar -b https://github.com/cloudfoundry/java-buildpack.git

```

Note, that you can also use the cf push without the java-buildpack, but then you need to downgrade the example to Java 7. Check out the custom "java7" branch for that.
