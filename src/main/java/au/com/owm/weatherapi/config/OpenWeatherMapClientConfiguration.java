package au.com.owm.weatherapi.config;

import au.com.owm.weatherapi.client.OpenWeatherMapClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@EnableConfigurationProperties(OpenWeatherMapClientConfigProperties.class)
public class OpenWeatherMapClientConfiguration {

    @Bean
    public OpenWeatherMapClient openWeatherMapClient(OpenWeatherMapClientConfigProperties
                                                                 openWeatherMapClientConfigProperties) {

        OpenWeatherMapClient openWeatherMapClient = new Retrofit.Builder()
                .baseUrl(openWeatherMapClientConfigProperties.getBasePath())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(OpenWeatherMapClient.class);

        return openWeatherMapClient;
    }
}
