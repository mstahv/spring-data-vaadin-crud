package crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.vaadin.spring.events.annotation.EnableEventBus;

@SpringBootApplication
@EnableEventBus
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean 
    public EventBusAutoconfigurator eventBusAutoconfigurator() {
        return new EventBusAutoconfigurator();
    }

}
