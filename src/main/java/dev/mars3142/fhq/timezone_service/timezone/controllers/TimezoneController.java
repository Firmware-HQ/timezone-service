package dev.mars3142.fhq.timezone_service.timezone.controllers;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import dev.mars3142.fhq.timezone_service.timezone.domain.model.response.LocationResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.model.response.TimezoneResponse;
import dev.mars3142.fhq.timezone_service.timezone.service.TimezoneService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
    response.add(linkTo(TimezoneController.class).slash(ip).withSelfRel());
    return response;
  }

  @GetMapping("{area}")
  @ResponseStatus(HttpStatus.OK)
  public LocationResponse getLocations(@PathVariable String area) {
    val locations = timeZoneService.getLocations(area);
    val result = new LocationResponse();
    result.setLocations(locations);
    result.add(linkTo(TimezoneController.class).slash(area).withSelfRel());
    return result;
  }

  @GetMapping("{area}/{location}")
  @ResponseStatus(HttpStatus.OK)
  public TimezoneResponse getTimeZoneForLocation(@PathVariable String area, @PathVariable String location) {
    val timezone = area + "/" + location;
    val posix = timeZoneService.getPosixTimeZone(timezone);
    val response = new TimezoneResponse();
    response.setTimezone(timezone);
    response.setPosix_tz(posix);
    response.add(linkTo(TimezoneController.class).slash(area).slash(location).withSelfRel());
    return response;
  }
}
