package fiit.stulib.sipvsbe.service;

import fiit.stulib.sipvsbe.controller.dto.LibraryLoanDto;

public interface IValidationService {
    void validateLibraryLoan(LibraryLoanDto libraryLoanDto);
}
