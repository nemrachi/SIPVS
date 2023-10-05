package fiit.stulib.service.model;

import lombok.Data;

import java.util.List;

@Data
public class LibraryLoan {
  private String loanId;
  private Borrower borrower;
  private String dateOfLoan;
  private String dueDate;
  private List<Book> loanedBooks;
  private Integer librarianId;
}
