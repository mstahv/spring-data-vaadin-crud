# Spring Data JPA CRUD with Vaadin

A super simple single table CRUD example with [Spring Data JPA](http://projects.spring.io/spring-data-jpa/) and [Vaadin](https://vaadin.com). Uses [Spring Boot](http://projects.spring.io/spring-boot/) for easy project setup and development. Helps you to get started with basic JPA backed applications and [Vaadin Spring Boot](https://vaadin.com/addon/vaadin-spring-boot) integration library.

For larger applications, consider applying some commonly known design patterns for your UI code. Check e.g. [this MVP example](https://github.com/peholmst/vaadin4spring/tree/master/spring-vaadin-mvp).

As an example for a really easy Vaadin add-on usage, there is Switch add-on added as a dependency and the application uses [cdn.virit.in](http://cdn.virit.in) service to compile and host the widgetset. See [this changeset](https://github.com/mstahv/spring-data-vaadin-crud/commit/2d67627d5757ec952d410e86ea9928747a7bef21?w=1) for setup instructions.

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
