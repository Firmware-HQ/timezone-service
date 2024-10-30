package dev.mars3142.fhq.timezone.timezone.controllers;

import dev.mars3142.fhq.timezone.timezone.domain.model.response.TimezoneResponse;
import dev.mars3142.fhq.timezone.timezone.service.TimezoneService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/timezone")
@RequiredArgsConstructor
public class TimezoneController {

  private final TimezoneService timeZoneService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public TimezoneResponse getTimeZone(
      @RequestHeader(value = "X-Forwarded-For", defaultValue = "127.0.0.1") String header_ip) {
    val clientIp = header_ip.split(",")[0];
    val ip = timeZoneService.getExternalIp(clientIp);
    val timezoneInfo = timeZoneService.getTimeZoneInfoByIp(ip);
    val posix = timeZoneService.getPosixTimeZone(timezoneInfo.timezone());
    val response = new TimezoneResponse();
    response.setTimezone(timezoneInfo.timezone());
    response.setPosix_tz(posix);
    return response;
  }

  @GetMapping("{area}")
  @ResponseStatus(HttpStatus.OK)
  public Page<String> getLocations(@PathVariable String area,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "direction", required = false) String direction) {
    var pageRequest = PageRequest.of(page, pageSize);
    if (direction != null && !direction.isEmpty()) {
      pageRequest = PageRequest.of(page, pageSize, Direction.fromString(direction), "location");
    }
    return timeZoneService.getPagedLocations(area, pageRequest);
  }

  @GetMapping("{area}/{location}")
  @ResponseStatus(HttpStatus.OK)
  public TimezoneResponse getTimeZoneForLocation(@PathVariable String area, @PathVariable String location) {
    val timezone = area + "/" + location;
    val posix = timeZoneService.getPosixTimeZone(timezone);
    val response = new TimezoneResponse();
    response.setTimezone(timezone);
    response.setPosix_tz(posix);
    return response;
  }
}
