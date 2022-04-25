package au.com.owm.weatherapi.client;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface OpenWeatherMapClient {

    @GET("/data/2.5/weather")
    Call<JsonNode> getWeatherData(@Query("q") String location,
                                  @Query("appid") String apiKey);

}
