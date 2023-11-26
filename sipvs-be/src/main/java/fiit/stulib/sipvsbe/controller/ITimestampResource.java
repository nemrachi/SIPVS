package fiit.stulib.sipvsbe.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/zadanie3")
@RestController
public interface ITimestampResource {

    @PostMapping(path = "/timestamp", produces = "application/xml")
    String addTimestamp(@RequestParam("file") MultipartFile file);
}
