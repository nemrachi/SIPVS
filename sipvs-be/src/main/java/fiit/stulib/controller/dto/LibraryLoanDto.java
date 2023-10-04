package fiit.stulib.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class LibraryLoanDto {

  @NotNull
  @Size(max = 20)
  private String loanId;

  @NotNull
  private BorrowerDto borrower;

  @Size(max = 20)
  private String dateOfLoan;

  @Size(max = 20)
  private String dueDate;

  @NotNull
  private List<BookDto> loanedBooks;
}
