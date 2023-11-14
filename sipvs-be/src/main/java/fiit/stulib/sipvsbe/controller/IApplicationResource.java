package fiit.stulib.sipvsbe.controller;

import fiit.stulib.sipvsbe.controller.dto.LibraryLoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RequestMapping("/api/zadanie1")
@RestController
public interface IApplicationResource {

    @PostMapping(path = "/save", produces = "application/json", consumes = "application/json")
    ResponseEntity<String> save(@RequestBody @Valid @NotNull LibraryLoanDto libraryLoanDto);

    @GetMapping(path = "/validate", produces = "application/json")
    ResponseEntity<String> validate();

    @GetMapping(path = "/transform", produces = "application/json")
    ResponseEntity<String> transform();

}
