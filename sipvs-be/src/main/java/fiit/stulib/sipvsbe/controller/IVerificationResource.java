package fiit.stulib.sipvsbe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/zadanie4")
@RestController
public interface IVerificationResource {

    @GetMapping(path = "/verify")
    void verify();
}
