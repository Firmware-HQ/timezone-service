package dev.mars3142.fhq.timezone_service.timezone.domain.model.response;

import java.util.List;

public record LocationResponse(int count, List<String> locations) {

}
