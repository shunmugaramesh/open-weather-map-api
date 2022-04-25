package au.com.owm.weatherapi.config;

import au.com.owm.weatherapi.client.OpenWeatherMapClient;
import au.com.owm.weatherapi.repository.WeatherApiRepository;
import au.com.owm.weatherapi.service.WeatherAPIService;
import au.com.owm.weatherapi.validator.WeatherAPIServiceValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class ServiceConfiguration {

    @Bean
    public WeatherAPIService weatherAPIService(OpenWeatherMapClient openWeatherMapClient,
                                               WeatherApiRepository weatherApiRepository) {
        return new WeatherAPIService(openWeatherMapClient, weatherApiRepository);
    }

    @Bean
    public WeatherAPIServiceValidator weatherAPIServiceValidator() {
        return new WeatherAPIServiceValidator();
    }

    @Bean
    public WeatherApiRepository weatherApiRepository(EntityManager entityManager) {
        return new WeatherApiRepository(entityManager);
    }
}
