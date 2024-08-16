package dev.mars3142.fhq.timezone_service.timezone.domain.entities.response;

public record TimeApiTimezoneZoneResponse(Interval dstInterval) {

  public record Interval(String dstName) {

  }
}
