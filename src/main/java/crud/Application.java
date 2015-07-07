package crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*
     * If this don't compile, issue "mvn install" once. If it still don't 
     * compile you are most likely using an odd IDE called Eclipse, which 
     * don't really get maven projects, so you need to configure the 
     * generated sources from target/generated-sources/vwscdn to be 
     * included on your classpath: right click on project -> Properties -> 
     * Java build path -> Source tab -> Add folder.
     */
    @Bean
    public in.virit.WidgetSet viritinCdnInitializer() {
        return new in.virit.WidgetSet();
    }

    @Bean
    public org.peimari.dawn.CdnFonts cdnFonts() {
        return new org.peimari.dawn.CdnFonts();
    }

}
