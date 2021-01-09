package club.beingsoft.restaurants.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    //https://stackoverflow.com/questions/21708339/avoid-jackson-serialization-on-non-fetched-lazy-objects
    @Bean
    public Hibernate5Module datatypeHibernateModule() {
        return new Hibernate5Module();
    }
}
