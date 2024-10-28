package dev.mars3142.fhq.timezone_service.timezone.service;

import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.IPApiResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.TimeApiTimezoneZoneResponse;
import java.util.List;

public interface TimezoneService {

  String getExternalIp(String ip);

  IPApiResponse getTimeZoneInfoByIp(String ip);

  TimeApiTimezoneZoneResponse getTimeZoneInfo(String timezone);

  String getPosixTimeZone(String timezone);

  List<String> getLocations(String area);
}
