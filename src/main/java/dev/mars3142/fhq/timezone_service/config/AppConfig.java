package dev.mars3142.fhq.timezone_service.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableCaching
public class AppConfig {

  @Bean
  public ClientHttpRequestFactory getClientHttpRequestFactory() {
    val requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(Duration.of(6, ChronoUnit.SECONDS));
    requestFactory.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));
    return requestFactory;
  }

  @Bean
  public RestClient.Builder restClientBuilder(ClientHttpRequestFactory requestFactory) {
    return RestClient
        .builder()
        .requestFactory(requestFactory);
  }

  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager();
  }
}
