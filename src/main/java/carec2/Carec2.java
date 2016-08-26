package carec2;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Carec2 extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(Carec2.class);

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Carec2.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(Carec2.class, args);
    }

    @Bean
    public CamelContext camelContext() throws Exception {
        return new SpringCamelContext(applicationContext);
    }



}

