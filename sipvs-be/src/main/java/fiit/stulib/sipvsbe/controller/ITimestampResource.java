package fiit.stulib.sipvsbe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/zadanie3")
@RestController
public interface ITimestampResource {

    @GetMapping(path = "/addTimestamp", produces = "application/xml")
    String addTimestamp();
}
