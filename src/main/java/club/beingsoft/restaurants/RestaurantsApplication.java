package club.beingsoft.restaurants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//@ComponentScan(basePackages = "club.beingsoft.restaurants.*")
public class RestaurantsApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestaurantsApplication.class);
    }

}
