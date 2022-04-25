package au.com.owm.weatherapi.validator;

import au.com.owm.weatherapi.exception.WeatherApiException;
import au.com.owm.weatherapi.validator.WeatherAPIServiceValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class WeatherAPIServiceValidatorTest {
    WeatherAPIServiceValidator weatherAPIServiceValidator = new WeatherAPIServiceValidator();

    @Test
    public void validInputQueryParams() {
        Map<String, String> queryParams = Map.of("apiKey", "60f4084d4a116eb07ef41ff89f64e1de",
                "city", "Sydney",
                "country", "Australia");
        weatherAPIServiceValidator.validateInputQueryParams(queryParams);
    }

    @Test
    public void missingMandatoryInputQueryParams() {
        Map<String, String> queryParams = Map.of("city", "Sydney",
                "country", "Australia");
        WeatherApiException weatherApiException = Assertions.assertThrows(WeatherApiException.class,
                () -> weatherAPIServiceValidator.validateInputQueryParams(queryParams));
        Assertions.assertEquals(BAD_REQUEST, weatherApiException.getErrorCode());
        Assertions.assertEquals("Mandatory param apiKey is missing", weatherApiException.getErrorMessage());
    }

    @Test
    public void missingInvalidInputQueryParams() {
        Map<String, String> queryParams = Map.of("apiKey", "60f4084d4a116eb07ef41ff89f64e1de",
                "city", "324244",
                "country", "Australia");
        WeatherApiException weatherApiException = Assertions.assertThrows(WeatherApiException.class,
                () -> weatherAPIServiceValidator.validateInputQueryParams(queryParams));
        Assertions.assertEquals(BAD_REQUEST, weatherApiException.getErrorCode());
        Assertions.assertEquals("Invalid query param. Enter valid apiKey and city", weatherApiException.getErrorMessage());
    }
}
