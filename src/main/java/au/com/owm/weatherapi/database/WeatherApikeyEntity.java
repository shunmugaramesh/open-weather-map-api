package au.com.owm.weatherapi.database;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "api_key_log")
public class WeatherApikeyEntity implements Serializable {

    @Id
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "no_of_times_requested")
    private int noOfTimesRequested;

    @Column(name= "first_usage_time")
    private LocalDateTime localDateTime;
}
