package au.com.owm.weatherapi.web;

import au.com.owm.weatherapi.exception.WeatherApiException;
import au.com.owm.weatherapi.model.WeatherApiResponse;
import au.com.owm.weatherapi.service.WeatherAPIService;
import au.com.owm.weatherapi.validator.WeatherAPIServiceValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class WeatherAPIResource {

    private WeatherAPIService weatherAPIService;

    private WeatherAPIServiceValidator weatherAPIServiceValidator;

    public WeatherAPIResource(WeatherAPIService weatherAPIService,
                              WeatherAPIServiceValidator weatherAPIServiceValidator) {
        this.weatherAPIService =weatherAPIService;
        this.weatherAPIServiceValidator = weatherAPIServiceValidator;
    }


    @GetMapping("/weatherapi/v1/description")
    public ResponseEntity<WeatherApiResponse> getWeatherDescription(@RequestParam Map<String, String> queryParams) {
        weatherAPIServiceValidator.validateInputQueryParams(queryParams);
       return ResponseEntity.ok(weatherAPIService.getWeatherDescription(queryParams));
    }

    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<WeatherApiException> handleWeatherApiException(WeatherApiException weatherApiException) {
        return ResponseEntity.status(weatherApiException.getErrorCode().value()).body(weatherApiException);
    }
}
