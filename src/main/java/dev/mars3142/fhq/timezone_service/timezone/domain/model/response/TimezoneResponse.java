package dev.mars3142.fhq.timezone_service.timezone.domain.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimezoneResponse {

  private String timezone;
  private String posix_tz;
}
