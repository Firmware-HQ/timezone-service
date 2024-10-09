package dev.mars3142.fhq.timezone_service.root.web.controller;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

  @GetMapping
  public ResponseEntity<Void> getRoot() {
    return ResponseEntity.status(HttpStatus.SEE_OTHER)
        .location(URI.create("https://firmware-hq.dev"))
        .build();
  }

}
