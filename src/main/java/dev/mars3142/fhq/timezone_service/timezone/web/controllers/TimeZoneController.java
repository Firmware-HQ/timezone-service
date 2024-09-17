package dev.mars3142.fhq.timezone_service.timezone.web.controllers;

import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.TimeApiTimezoneZoneResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.model.response.LocationResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.model.response.TimeZoneResponse;
import dev.mars3142.fhq.timezone_service.timezone.service.TimeZoneService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/timezone")
@RequiredArgsConstructor
public class TimeZoneController {

  private final TimeZoneService timeZoneService;

  @GetMapping
  public TimeZoneResponse getTimeZone(@RequestHeader(value = "X-Forwarded-For", defaultValue = "127.0.0.1") String header) {
    val clientIp = header.split(",")[0];
    val ip = timeZoneService.getExternalIp(clientIp);
    val timezoneInfo = timeZoneService.getTimeZoneInfoByIp(ip);
    val posix = timeZoneService.getPosixTimeZone(timezoneInfo.timezone());
    return new TimeZoneResponse(timezoneInfo.timezone(), timezoneInfo.abbreviation(), posix);
  }

  @GetMapping("{area}")
  public LocationResponse getLocations(@PathVariable String area) {
    val locations = timeZoneService.getLocations(area);
    return new LocationResponse(locations.size(), locations);
  }

  @GetMapping("{area}/{location}")
  public TimeZoneResponse getTimeZoneForLocation(@PathVariable String area, @PathVariable String location) {
    val timezone = area + "/" + location;
    val timezoneInfo = timeZoneService.getTimeZoneInfo(timezone);
    val abbreviation = Objects.requireNonNullElse(timezoneInfo.dstInterval(), new TimeApiTimezoneZoneResponse.Interval(null)).dstName();
    val posix = timeZoneService.getPosixTimeZone(timezone);
    return new TimeZoneResponse(timezone, abbreviation, posix);
  }
}
