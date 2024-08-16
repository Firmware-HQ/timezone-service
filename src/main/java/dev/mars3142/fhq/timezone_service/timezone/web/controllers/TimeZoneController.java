package dev.mars3142.fhq.timezone_service.timezone.web.controllers;

import dev.mars3142.fhq.timezone_service.timezone.domain.model.request.TimeZoneRequest;
import dev.mars3142.fhq.timezone_service.timezone.domain.model.response.TimeZoneResponse;
import dev.mars3142.fhq.timezone_service.timezone.service.TimeZoneService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/timezone")
@RequiredArgsConstructor
public class TimeZoneController {

  private final TimeZoneService timeZoneService;

  @PostMapping()
  public TimeZoneResponse getTimeZone(@RequestBody TimeZoneRequest request) {
    val timezone = timeZoneService.getPosixTimeZone(request.timezone());
    return new TimeZoneResponse(timezone);
  }
}
