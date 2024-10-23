package dev.mars3142.fhq.timezone_service.timezone.domain.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
@RequiredArgsConstructor
public class TimeZoneResponse extends RepresentationModel<TimeZoneResponse> {

  private final String timezone;
  private final String abbreviation;
  private final String posix_tz;
}
