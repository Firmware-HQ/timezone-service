package dev.mars3142.fhq.timezone.timezone.domain.entities.response;

public record TimeApiTimezoneZoneResponse(Interval dstInterval) {

  public record Interval(String dstName) {

  }
}
