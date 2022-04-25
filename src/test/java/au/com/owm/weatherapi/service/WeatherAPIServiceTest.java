package au.com.owm.weatherapi.service;

import au.com.owm.weatherapi.client.OpenWeatherMapClient;
import au.com.owm.weatherapi.database.WeatherApikeyEntity;
import au.com.owm.weatherapi.exception.WeatherApiException;
import au.com.owm.weatherapi.model.WeatherApiResponse;
import au.com.owm.weatherapi.repository.WeatherApiRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.mock.Calls;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherAPIServiceTest {

    private OpenWeatherMapClient openWeatherMapClient = mock(OpenWeatherMapClient.class);

    private WeatherApiRepository weatherApiRepository = mock(WeatherApiRepository.class);

    private WeatherAPIService weatherAPIService = new WeatherAPIService(openWeatherMapClient, weatherApiRepository);

    Map<String, String> queryParams;

    @BeforeEach
    public void init() {
        queryParams = Map.of("apiKey", "60f4084d4a116eb07ef41ff89f64e1de",
                "city", "Sydney",
                "country", "Australia");
    }

    @Test
    public void testSuccessfulWeatherDescription() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readValue(Files.readString(Paths.get("src/test/resources/owm-weather-response.json")), JsonNode.class);
        when(openWeatherMapClient.getWeatherData(any(), any())).thenReturn(Calls.response(jsonNode));
        WeatherApiResponse weatherApiResponse = weatherAPIService.getWeatherDescription(queryParams);
        Assertions.assertEquals("broken clouds", weatherApiResponse.getWeatherDescription());
    }

    @Test
    public void testApiKeyUsageExceeded() {
        when(weatherApiRepository.findWeatherApikeyEntityBy(any())).thenReturn(new WeatherApikeyEntity("60f4084d4a116eb07ef41ff89f64e1de", 5, LocalDateTime.now()));
        WeatherApiException weatherApiException = Assertions.assertThrows(WeatherApiException.class, () -> weatherAPIService.getWeatherDescription(queryParams));
        Assertions.assertEquals("API Key Usage Limit has been exceeded more than allocated limit in an hour. Use different API Key",
                weatherApiException.getErrorMessage());
    }
}
