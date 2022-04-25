package au.com.owm.weatherapi.web;

import au.com.owm.weatherapi.client.OpenWeatherMapClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.h2.util.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import retrofit2.Response;
import retrofit2.mock.Calls;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class WeatherApiResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenWeatherMapClient openWeatherMapClient;

    @Test
    @DisplayName("/weatherapi/v1/description : Success")
    public void shouldReturn200SuccessGetWeatherDescription() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readValue(Files.readString(Paths.get("src/test/resources/owm-weather-response.json")), JsonNode.class);

        when(openWeatherMapClient.getWeatherData(any(), any())).thenReturn(Calls.response(jsonNode));

        mockMvc.perform(MockMvcRequestBuilders.get("/weatherapi/v1/description?city=Melbourne&country=Australia&apiKey=60f4084d4a116eb07ef41ff89f64e1de"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.weatherDescription").value("broken clouds"));
    }

    @Test
    @DisplayName("/weatherapi/v1/description : Bad Request Mandatory fields")
    public void shouldReturn400BadRequestMissingApiKey() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readValue(Files.readString(Paths.get("src/test/resources/owm-weather-response.json")), JsonNode.class);

        when(openWeatherMapClient.getWeatherData(any(), any())).thenReturn(Calls.response(jsonNode));

        mockMvc.perform(MockMvcRequestBuilders.get("/weatherapi/v1/description?city=345345&country=Australia&apiKey="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Mandatory param apiKey is missing"));
    }

    @Test
    @DisplayName("/weatherapi/v1/description : Bad Request Invalid City")
    public void shouldReturn400BadRequestInvalidCity() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readValue(Files.readString(Paths.get("src/test/resources/owm-weather-response.json")), JsonNode.class);

        when(openWeatherMapClient.getWeatherData(any(), any())).thenReturn(Calls.response(jsonNode));

        mockMvc.perform(MockMvcRequestBuilders.get("/weatherapi/v1/description?city=345345&country=Australia&apiKey=60f4084d4a116eb07ef41ff89f64e1de"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Invalid query param. Enter valid apiKey and city"));
    }

    @Test
    @DisplayName("/weatherapi/v1/description : Success")
    public void shouldReturn404NotFound() throws Exception {
      JsonNode jsonNode = new ObjectMapper().readValue(Files.readString(Paths.get("src/test/resources/owm-weather-response-not-found.json")), JsonNode.class);

        when(openWeatherMapClient.getWeatherData(any(), any())).thenReturn(Calls.response(Response.error(404, ResponseBody.create(MediaType.parse("application/json"), ""))));

        mockMvc.perform(MockMvcRequestBuilders.get("/weatherapi/v1/description?city=df&country=Australia&apiKey=60f4084d4a116eb07ef41ff89f64e1de"))
                .andExpect(status().is4xxClientError());
    }
}
