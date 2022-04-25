package au.com.owm.weatherapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class WeatherapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherapiApplication.class, args);
	}

}
