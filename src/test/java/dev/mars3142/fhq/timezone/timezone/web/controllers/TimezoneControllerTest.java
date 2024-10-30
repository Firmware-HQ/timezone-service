package dev.mars3142.fhq.timezone.timezone.web.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Testing Timezone RestController")
public class TimezoneControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Disabled
  @Test
  @DisplayName("local timezone is Europe/Berlin")
  void getTimeZone() throws Exception {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/v1/timezone", String.class))
            .contains("Europe/Berlin");
  }

  @Test
  @DisplayName("Europe/Moscow is in European timezone list")
  void checkLocationMoscow() throws Exception {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/v1/timezone/Europe", String.class))
            .contains("Europe/Moscow").contains("64");
  }

  @Test
  @DisplayName("Europe/Hamburg is not in European timezone list")
  void checkLocationHamburg() throws Exception {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/v1/timezone/Europe", String.class))
            .doesNotContain("Europe/Hamburg");
  }

  @Test
  @DisplayName("Hawaii is not a valide timezone list")
  void checkLocationHawaii() throws Exception {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/v1/timezone/Hawaii", String.class))
            .contains("Not Found");
  }

  @Test
  @DisplayName("Europe/Paris has a valid timezone")
  void getTimeZoneForLocation() throws Exception {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/v1/timezone/Europe/Paris", String.class))
            .contains("Europe/Paris");
  }
}
