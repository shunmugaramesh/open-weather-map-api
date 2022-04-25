package au.com.owm.weatherapi.service;

import au.com.owm.weatherapi.exception.WeatherApiException;
import au.com.owm.weatherapi.client.OpenWeatherMapClient;
import au.com.owm.weatherapi.database.WeatherApikeyEntity;
import au.com.owm.weatherapi.model.WeatherApiResponse;
import au.com.owm.weatherapi.repository.WeatherApiRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static au.com.owm.weatherapi.constants.Constants.*;

public class WeatherAPIService {

    private static final Logger log = LoggerFactory.getLogger(WeatherAPIService.class);

    private OpenWeatherMapClient openWeatherMapClient;

    private WeatherApiRepository weatherApiRepository;

    private int apiKeyUsage;

    public WeatherAPIService(OpenWeatherMapClient openWeatherMapClient, WeatherApiRepository weatherApiRepository) {
        this.openWeatherMapClient = openWeatherMapClient;
        this.weatherApiRepository = weatherApiRepository;
    }

    public WeatherApiResponse getWeatherDescription(Map<String, String> queryParams) {
        checkApiKeyUsageLimit(queryParams.get(API_KEY));
        WeatherApiResponse weatherApiResponse = new WeatherApiResponse();
        try {
            String weatherDescription = null;
            Call<JsonNode> jsonNodeCall = openWeatherMapClient.getWeatherData(formLocation(queryParams), queryParams.get(API_KEY));
            Response<JsonNode> response  = jsonNodeCall.execute();
            if(response.isSuccessful()) {
                JsonNode weatherResponseNode = response.body() != null ? response.body().get("weather") : null;
                log.info("Response from Open Weather Map API : {} ", weatherResponseNode);
                if(weatherResponseNode.isArray()) {
                    weatherDescription = weatherResponseNode.get(0).get("description").asText();
                }
                weatherApiResponse.setWeatherDescription(weatherDescription);
                saveApiKeyUsage(queryParams.get(API_KEY), apiKeyUsage);
                return weatherApiResponse;
            } else {
                String errorResponseMessage = response.raw().message();
                if(StringUtils.isNotEmpty(errorResponseMessage)) {
                    log.error("Error response from Open Weather API call : {} ", errorResponseMessage);
                 throw WeatherApiException.generic(response.raw().code(), errorResponseMessage);
                }
            }
            return null;
        } catch (IOException e) {
            log.error("Error in calling Open Weather API : {} ", e);

        }
        return null;
    }

    private void saveApiKeyUsage(String apiKey, int apiKeyUsage) {
        WeatherApikeyEntity weatherApikeyEntity = new WeatherApikeyEntity();
        weatherApikeyEntity.setApiKey(apiKey);
        weatherApikeyEntity.setLocalDateTime(LocalDateTime.now());
        weatherApikeyEntity.setNoOfTimesRequested(++apiKeyUsage);
        log.debug("Calling H2 DB with input: {}", weatherApikeyEntity);
        weatherApiRepository.saveWeatherApikeyEntity(weatherApikeyEntity);
    }

    private void checkApiKeyUsageLimit(String apiKey) {
        log.debug("Calling H2 DB with input : {}", apiKey);
        WeatherApikeyEntity weatherApikeyEntity = weatherApiRepository.findWeatherApikeyEntityBy(apiKey);
        if (weatherApikeyEntity != null) {
            apiKeyUsage = weatherApikeyEntity.getNoOfTimesRequested();
            LocalDateTime localDateTime = LocalDateTime.now();
            ZonedDateTime now = localDateTime.atZone(ZoneId.of("Australia/Sydney"));
            Duration duration = Duration.between(weatherApikeyEntity.getLocalDateTime(), now);
            if (duration.toHours() < 1 && weatherApikeyEntity.getNoOfTimesRequested() >= 5) {
                log.error("API Key Usage exceeded hourly limit");
                apiKeyUsage = 0;
                throw WeatherApiException.generic("API Key Usage exceeded hourly limit. Use different API Key");

            }
        }
    }

    private String formLocation(Map<String, String> queryParams) {
        return StringUtils.isNotBlank(queryParams.get(COUNTRY)) ? queryParams.get(CITY)
                : StringUtils.join(queryParams.get(CITY), ",", queryParams.get(COUNTRY));
    }
}
