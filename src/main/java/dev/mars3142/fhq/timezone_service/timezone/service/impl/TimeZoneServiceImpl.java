package dev.mars3142.fhq.timezone_service.timezone.service.impl;

import dev.mars3142.fhq.timezone_service.exceptions.NotFoundException;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TimeZoneServiceImpl implements TimeZoneService {

  private final RestClient restClient;

  @Override
  public WorldTimeApiIpResponse getTimeZoneInfoByIp() {
    return restClient
        .get()
        .uri("https://worldtimeapi.org/api/ip")
        .retrieve()
        .body(WorldTimeApiIpResponse.class);
  }

  @Override
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
