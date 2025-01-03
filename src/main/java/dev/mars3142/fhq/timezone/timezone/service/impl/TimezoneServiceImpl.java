package dev.mars3142.fhq.timezone.timezone.service.impl;

import dev.mars3142.fhq.timezone.exceptions.NotFoundException;
import dev.mars3142.fhq.timezone.timezone.domain.entities.response.IPApiResponse;
import dev.mars3142.fhq.timezone.timezone.domain.entities.response.IpifyResponse;
import dev.mars3142.fhq.timezone.timezone.domain.entities.response.TimeApiTimezoneZoneResponse;
import dev.mars3142.fhq.timezone.timezone.service.TimezoneService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class TimezoneServiceImpl implements TimezoneService {

  private final RestClient restClient;

  public TimezoneServiceImpl(RestClient.Builder restClientBuilder) {
    restClient = restClientBuilder.build();
  }

  @Override
  @Cacheable(value = "externalIp", key = "{#ip}")
  public String getExternalIp(String ip) {
    var result = ip;
    if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
      val response = restClient
          .get()
          .uri("https://api.ipify.org?format=json")
          .retrieve()
          .body(IpifyResponse.class);
      result = Objects.requireNonNull(response).ip();
    }
    log.debug("IP Address: {}", result);
    return result;
  }

  @Override
  @Cacheable(value = "TZInfoByIp", key = "{#ip}")
  public IPApiResponse getTimeZoneInfoByIp(String ip) {
    return restClient
        .get()
        .uri(builder -> builder
            .scheme("http")
            .host("ip-api.com")
            .path("json/" + ip)
            .build())
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
          throw new NotFoundException();
        })
        .body(IPApiResponse.class);
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
  @Cacheable(value = "locations", key = "{#area, #pageRequest}")
  public Page<String> getPagedLocations(String area, PageRequest pageRequest) {
    val directory = new File("/usr/share/zoneinfo/" + area);
    if (!directory.exists()) {
      throw new NotFoundException();
    }
    return toPage(Stream.of(Objects.requireNonNull(directory.listFiles()))
        .filter(file -> !file.isDirectory())
        .map(file -> {
          val path = file.getPath().split("/");
          return path[path.length - 2] + "/" + path[path.length - 1];
        })
        .toList(), pageRequest);
  }

  Page<String> toPage(List<String> list, PageRequest pageRequest) {
    val pageSize = pageRequest.getPageSize();
    val pageNumber = pageRequest.getPageNumber();
    val totalPages = list.size() / pageSize;

    int max = pageNumber >= totalPages ? list.size() : pageSize * (pageNumber + 1);
    int min = pageNumber > totalPages ? max : pageSize * pageNumber;

    var content = list.stream().sorted((left, right) -> {
          if (!pageRequest.getSort().isSorted()) {
            return 0;
          }

          if (Objects.requireNonNull(pageRequest.getSort().getOrderFor("location")).isAscending()) {
            return left.compareTo(right);
          } else {
            return right.compareTo(left);
          }
        }
    );
    return new PageImpl<>(content.toList().subList(min, max), pageRequest, list.size());
  }
}
