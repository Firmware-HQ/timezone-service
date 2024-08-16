package dev.mars3142.fhq.timezone_service.timezone.service.impl;

import dev.mars3142.fhq.timezone_service.exceptions.NotFoundException;
import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.IpifyResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.TimeApiTimezoneZoneResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.WorldTimeApiIpResponse;
import dev.mars3142.fhq.timezone_service.timezone.service.TimeZoneService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TimeZoneServiceImpl implements TimeZoneService {

  private final RestClient restClient;

  @Override
  public String getExternalIp(String ip) {
    if (ip.equals("127.0.0.1")) {
      val response = restClient
          .get()
          .uri("https://api.ipify.org?format=json")
          .retrieve()
          .body(IpifyResponse.class);
      return Objects.requireNonNull(response).ip();
    }
    return ip;
  }

  @Override
  @Cacheable(value = "TZInfoByIp", key = "{#ip}")
  public WorldTimeApiIpResponse getTimeZoneInfoByIp(String ip) {
    return restClient
        .get()
        .uri("https://worldtimeapi.org/api/ip/" + ip)
        .retrieve()
        .body(WorldTimeApiIpResponse.class);
  }

  @Override
  @Cacheable(value = "TZInfo", key = "{#timezone}")
  public TimeApiTimezoneZoneResponse getTimeZoneInfo(String timezone) {
    return restClient
        .get()
        .uri(builder -> builder
            .scheme("https")
            .host("timeapi.io")
            .path("api/timezone/zone")
            .queryParam("timeZone", timezone)
            .build())
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
          throw new NotFoundException();
        })
        .body(TimeApiTimezoneZoneResponse.class);
  }

  @Override
  @Cacheable(value = "posixTZ", key = "{#timezone}")
  public String getPosixTimeZone(String timezone) {
    val filename = Path.of("/usr/share/zoneinfo/" + timezone);
    if (!filename.toFile().exists()) {
      throw new NotFoundException();
    }
    try {
      val bytes = Files.readAllBytes(filename);
      val content = new String(bytes).split("\n");
      return content[content.length - 1];
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @Cacheable(value = "locations", key = "{#area}")
  public List<String> getLocations(String area) {
    val directory = new File("/usr/share/zoneinfo/" + area);
    if (!directory.exists()) {
      throw new NotFoundException();
    }
    return Stream.of(Objects.requireNonNull(directory.listFiles()))
        .filter(file -> !file.isDirectory())
        .map(file -> {
          val path = file.getPath().split("/");
          return path[path.length - 2] + "/" + path[path.length - 1];
        })
        .sorted()
        .toList();
  }
}
