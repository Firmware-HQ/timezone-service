package dev.mars3142.fhq.timezone_service.timezone.domain.model.response;

import java.util.List;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class LocationResponse extends RepresentationModel<LocationResponse> {

  private int count;
  private List<String> locations;

  public void setLocations(List<String> locations) {
    this.count = locations.size();
    this.locations = locations;
  }
}
