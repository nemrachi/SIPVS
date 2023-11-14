package fiit.stulib.sipvsbe.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping("/api/zadanie3")
@RestController
public interface ITimestampResource {

    @PostMapping(path = "/timestamp", produces = "application/xml", consumes = "application/xml")
    String addTimestamp(@RequestBody @NotNull String signedXml);
}
