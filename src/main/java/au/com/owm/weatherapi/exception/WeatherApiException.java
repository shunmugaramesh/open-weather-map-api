package au.com.owm.weatherapi.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties({"cause", "stackTrace", "message", "suppressed", "localizedMessage"})
public class WeatherApiException extends RuntimeException {

    private HttpStatus errorCode;
    private String errorMessage;

    public static WeatherApiException generic(String errorMessage) {
        return new WeatherApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    public static WeatherApiException badRequest(String errorMessage) {
        return new WeatherApiException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    public static WeatherApiException generic(int httpStatusCode, String errorMessage) {
        return new WeatherApiException(HttpStatus.valueOf(httpStatusCode), errorMessage);
    }
}
