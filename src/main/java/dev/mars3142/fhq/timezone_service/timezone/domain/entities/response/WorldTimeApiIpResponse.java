package dev.mars3142.fhq.timezone_service.timezone.domain.entities.response;

public record WorldTimeApiIpResponse(String utc_offset, String timezone, int day_of_week, int day_of_year, String datetime,
                                     String utc_datetime, int unixtime, int raw_offset, int week_number, boolean dst,
                                     String abbreviation, int dst_offset, String dst_from, String dst_until, String client_ip) {

}
