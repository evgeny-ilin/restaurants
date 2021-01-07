package club.beingsoft.restaurants.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Restaurants REST API documentation")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .email("evgeny.ilin@gmail.com")
                                                .url("https://beingsoft.com")
                                                .name("Evgeny Ilin")
                                )
                );
    }

}