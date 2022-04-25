package au.com.owm.weatherapi.validator;

import au.com.owm.weatherapi.exception.WeatherApiException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static au.com.owm.weatherapi.constants.Constants.*;

public class WeatherAPIServiceValidator {

    public void validateInputQueryParams(Map<String, String> queryParams) {
        if (StringUtils.isBlank(queryParams.get(API_KEY))) {
                throw WeatherApiException.badRequest("Mandatory param apiKey is missing");
        }
        if (StringUtils.isBlank(queryParams.get(CITY))) {
            throw WeatherApiException.badRequest("Mandatory param city is mandatory");
        }

        validateValidInputQueryParams(queryParams);
    }

    private void validateValidInputQueryParams(Map<String, String> queryParams) {
        if (queryParams.get(API_KEY).chars().count() != 32
                || !(queryParams.get(API_KEY).matches(API_KEY_REGEX))
                || !StringUtils.isAlpha(queryParams.get(CITY))) {
            throw WeatherApiException.badRequest("Invalid query param. Enter valid apiKey and city");
        }
    }
}
