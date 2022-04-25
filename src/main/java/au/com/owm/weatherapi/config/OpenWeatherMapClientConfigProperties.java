package au.com.owm.weatherapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="open-weather-map-api")
@Getter
@Setter
public class OpenWeatherMapClientConfigProperties{

    private String basePath;

}
