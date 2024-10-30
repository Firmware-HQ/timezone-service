package dev.mars3142.fhq.timezone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI timezoneOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Timezone API").version("1.0"))
        .servers(
            List.of(
                new Server().url("https://api.firmware-hq.dev"),
                new Server().url("http://localhost:8090")
            )
        );
  }
}
