package au.com.owm.weatherapi.repository;

import au.com.owm.weatherapi.database.WeatherApikeyEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

import static au.com.owm.weatherapi.constants.Constants.API_KEY;
import static au.com.owm.weatherapi.constants.Constants.API_KEY_USAGE;

@Transactional
public class WeatherApiRepository {

    private EntityManager entityManager;

    public WeatherApiRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public WeatherApikeyEntity findWeatherApikeyEntityBy(String apiKey) {
        Query query = entityManager.createNativeQuery("SELECT * FROM api_key_log " +
                        "WHERE api_key = :apiKey",
                        WeatherApikeyEntity.class)
                .setParameter(API_KEY, apiKey);
        Optional<Object> resultSet = query.getResultList().stream().findFirst();
        return  resultSet.isPresent() ? (WeatherApikeyEntity) resultSet.get() : null;
    }

    public void saveWeatherApikeyEntity(WeatherApikeyEntity weatherApikeyEntity) {
        int noOfTimesRequested = weatherApikeyEntity.getNoOfTimesRequested();
        int executeUpdate = entityManager
                .createNativeQuery("UPDATE api_key_log " +
                        "SET NO_OF_TIMES_REQUESTED = :apiKeyUsage" +
                        " WHERE API_KEY = :apiKey")
                .setParameter(API_KEY, weatherApikeyEntity.getApiKey())
                .setParameter(API_KEY_USAGE, noOfTimesRequested)
                .executeUpdate();
        if (executeUpdate != 1) {
            entityManager.merge(weatherApikeyEntity);
        }
      }
}
