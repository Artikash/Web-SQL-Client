import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "components" })
public class Application {
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
