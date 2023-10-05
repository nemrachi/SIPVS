package fiit.stulib.controller;

import fiit.stulib.controller.dto.LibraryLoanDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/zadanie1")
public interface IApplicationController {

  @PostMapping(path = "/save", produces = "application/json", consumes = "application/json")
  void save(@RequestBody @Valid @NotNull LibraryLoanDto libraryLoanDto);

  @GetMapping(path = "/validate", produces = "application/json", consumes = "application/json")
  void validate();

  @GetMapping(path = "/transform", produces = "application/json", consumes = "application/json")
  void transform();
}
