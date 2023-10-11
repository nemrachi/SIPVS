package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.controller.dto.LibraryLoanDto;
import fiit.stulib.sipvsbe.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidationService implements IValidationService {

    @Override
    public void validateLibraryLoan(LibraryLoanDto libraryLoanDto) {
        if (!libraryLoanDto.getLoanId().matches("[A-Z][0-9]{6}")) {
            throw new IllegalArgumentException("'Loan ID' must have format: 1 uppercase letter and 6 numbers.");
        }
        if (!(libraryLoanDto.getLibrarianId() >= 0)) {
            throw new IllegalArgumentException("'Librarian ID' cannot be negative.");
        }
        if (!libraryLoanDto.getBorrower().getCardNumber().matches("[A-Z0-9]{7}")) {
            throw new IllegalArgumentException("'Borrower Card' cannot must have 7 upper case letters or numbers combined.");
        }
        if (!libraryLoanDto.getDateOfLoan().isBefore(libraryLoanDto.getDueDate())) {
            throw new IllegalArgumentException("'Date Of Loan' cannot be after 'Due Date'.");
        }
        libraryLoanDto.getLoanedBooks().forEach(bookDto -> {
            if (!bookDto.getIsbn().matches("[0-9]{10}|[0-9]{13}")) {
                throw new IllegalArgumentException("'ISBN' must be length of 10 or 13 numbers.");
            }
        });
    }
}
