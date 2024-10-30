package dev.mars3142.fhq.timezone_service.timezone.service.impl;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("Testing Timezone Service Default Implementation")
@RestClientTest(TimezoneServiceImpl.class)
class TimezoneServiceImplTest {

  @Autowired
  private TimezoneServiceImpl timeZoneService;

  @Autowired
  private MockRestServiceServer server;

  @BeforeEach
  public void setUp() {
    server.reset();
  }

  @Test
  @DisplayName("Return same ip")
  void getSameIp() {
    val ip = timeZoneService.getExternalIp("8.8.8.8");
    assertThat(ip).isEqualTo("8.8.8.8");
  }

  @Test
  @DisplayName("Return custom ip")
  void getCustomIp() {
    server.expect(requestTo("https://api.ipify.org?format=json"))
          .andRespond(withSuccess("""
                                          {"ip":"8.8.8.8"}
                                          """, MediaType.APPLICATION_JSON));
    val ip = timeZoneService.getExternalIp("127.0.0.1");
    assertThat(ip).isEqualTo("8.8.8.8");
  }

  @Test
  void getTimeZoneInfoByIp() {
  }

  @Test
  void getTimeZoneInfo() {
  }

  @Test
  void getPosixTimeZone() {
  }

  @Test
  void getPagedLocations() {
  }
}
