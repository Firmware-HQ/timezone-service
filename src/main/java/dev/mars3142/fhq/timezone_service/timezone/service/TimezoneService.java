package dev.mars3142.fhq.timezone_service.timezone.service;

import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.IPApiResponse;
import dev.mars3142.fhq.timezone_service.timezone.domain.entities.response.TimeApiTimezoneZoneResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TimezoneService {

  String getExternalIp(String ip);

  IPApiResponse getTimeZoneInfoByIp(String ip);

  TimeApiTimezoneZoneResponse getTimeZoneInfo(String timezone);

  String getPosixTimeZone(String timezone);

  Page<String> getPagedLocations(String area, PageRequest pageRequest);
}
