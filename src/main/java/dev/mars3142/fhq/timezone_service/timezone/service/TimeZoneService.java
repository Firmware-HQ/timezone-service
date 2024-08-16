package dev.mars3142.fhq.timezone_service.timezone.service;

import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.TimeApiTimezoneZoneResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.WorldTimeApiIpResponse;
import java.util.List;

public interface TimeZoneService {

  WorldTimeApiIpResponse getTimeZoneInfoByIp();

  TimeApiTimezoneZoneResponse getTimeZoneInfo(String timezone);

  String getPosixTimeZone(String timezone);

  List<String> getLocations(String area);
}
