package dev.mars3142.fhq.timezone_service.timezone.web.controllers;

import dev.mars3142.fhq.timezone_service.timezone.domain.model.request.TimeZoneRequest;
import dev.mars3142.fhq.timezone_service.timezone.domain.model.response.TimeZoneResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/timezone")
public class TimeZoneController {

  @PostMapping()
  public TimeZoneResponse getTimeZone(@RequestBody TimeZoneRequest request) {
    return new TimeZoneResponse(request.timezone());
  }
}
