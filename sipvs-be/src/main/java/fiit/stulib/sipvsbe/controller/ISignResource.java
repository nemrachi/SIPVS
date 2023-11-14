package fiit.stulib.sipvsbe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/zadanie2")
@RestController
public interface ISignResource {

    @GetMapping("/generatePdfFromXml")
    String generatePdfFromXml();
    
    @GetMapping(path = "/getxml", produces = "application/xml")
    String getXml();

    @GetMapping(path = "/getxsd", produces = "application/xml")
    String getXsd();

    @GetMapping(path = "/getxsl", produces = "application/xml")
    String getXsl();

}
