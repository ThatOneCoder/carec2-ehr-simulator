package carec2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class Carec2 extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(Carec2.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Carec2.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(Carec2.class, args);
    }
}

