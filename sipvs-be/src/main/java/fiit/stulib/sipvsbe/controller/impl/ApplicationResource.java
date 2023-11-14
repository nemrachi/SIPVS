package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.IApplicationResource;
import fiit.stulib.sipvsbe.controller.ObjMapper;
import fiit.stulib.sipvsbe.controller.dto.LibraryLoanDto;
import fiit.stulib.sipvsbe.service.IApplicationService;
import fiit.stulib.sipvsbe.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApplicationResource implements IApplicationResource {

    @Autowired
    private IApplicationService applicationService;

    @Autowired
    private IValidationService validationService;

    @Override
    public ResponseEntity<String> save(LibraryLoanDto libraryLoanDto) {
        try {
            validationService.validateLibraryLoan(libraryLoanDto);
            applicationService.save(ObjMapper.fromDto(libraryLoanDto));
            return ResponseEntity.ok("XML file was saved.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Save error: " + e.getMessage());
        }
    }

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

    @Override
    public ResponseEntity<String> transform() {
        try {
            applicationService.transform();
            return ResponseEntity.ok("HTML file was saved.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Transform error: " + e.getMessage());
        }
    }

}
