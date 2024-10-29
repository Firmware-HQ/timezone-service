package dev.mars3142.fhq.timezone_service.timezone.domain.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class TimezoneResponse extends RepresentationModel<TimezoneResponse> {

  private String timezone;
  private String posix_tz;
}
