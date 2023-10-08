package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.IApplicationResource;
import fiit.stulib.sipvsbe.controller.ObjMapper;
import fiit.stulib.sipvsbe.controller.dto.LibraryLoanDto;
import fiit.stulib.sipvsbe.service.IApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/zadanie1")
public class ApplicationResource implements IApplicationResource {

    @Autowired
    private IApplicationService applicationService;

    @PostMapping(path = "/save", produces = "application/json", consumes = "application/json")
    @Override
    public void save(LibraryLoanDto libraryLoanDto) {
        applicationService.save(ObjMapper.fromDto(libraryLoanDto));
    }


    @GetMapping(path = "/validate", produces = "application/json")
    @Override
    public ResponseEntity<String> validate() {
        try {
            applicationService.validate();
            return ResponseEntity.ok("Validation successful. XML is valid against the XSD.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Validation error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/transform", produces = "application/json")
    @Override
    public void transform() {
        applicationService.transform();
    }

}
