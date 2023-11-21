package fiit.stulib.sipvsbe.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RequestMapping("/api/zadanie3")
@RestController
public interface ITimestampResource {

    @PostMapping(path = "/timestamp", produces = "application/xml", consumes = "application/xml")
    String addTimestamp(@RequestParam("file") MultipartFile file);
}
