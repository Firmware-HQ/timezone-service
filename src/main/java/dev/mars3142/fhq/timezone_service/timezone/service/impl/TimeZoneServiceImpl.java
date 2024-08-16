package dev.mars3142.fhq.timezone_service.timezone.service.impl;

import dev.mars3142.fhq.timezone_service.timezone.service.TimeZoneService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class TimeZoneServiceImpl implements TimeZoneService {

  @Override
  public String getPosixTimeZone(String timezone) {
    val filename = Path.of("/usr/share/zoneinfo/" + timezone);
    try {
      val bytes = Files.readAllBytes(filename);
      val content = new String(bytes).split("\n");
      return content[content.length - 1];
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
