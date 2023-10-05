package fiit.stulib.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Data
public class LibraryLoanDto {

  @NotNull
  @Size(max = 7)
  private String loanId;

  @NotNull
  private Integer librarianId;

  @NotNull
  private BorrowerDto borrower;

  @NotNull
  private Instant dateOfLoan;

  @NotNull
  private Instant dueDate;

  @NotNull
  private List<BookDto> loanedBooks;
}
