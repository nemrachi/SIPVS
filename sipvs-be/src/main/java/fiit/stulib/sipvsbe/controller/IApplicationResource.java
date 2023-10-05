package fiit.stulib.sipvsbe.controller;

import fiit.stulib.sipvsbe.controller.dto.LibraryLoanDto;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface IApplicationResource {

    void save(@RequestBody @Valid @NotNull LibraryLoanDto libraryLoanDto);

    void validate();

    void transform();
}
