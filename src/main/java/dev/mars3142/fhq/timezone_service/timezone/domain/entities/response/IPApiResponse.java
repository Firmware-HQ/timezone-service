package dev.mars3142.fhq.timezone_service.timezone.domain.entities.response;

public record IPApiResponse(String status, String country, String countryCode, String region, String regionName,
                            String city, String zip, String lat, String lon, String timezone, String isp, String or,
                            String as, String query
) {

}
