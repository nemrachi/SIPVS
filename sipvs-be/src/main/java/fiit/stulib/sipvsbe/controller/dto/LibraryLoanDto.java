package fiit.stulib.sipvsbe.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class LibraryLoanDto {

    @NotNull
    @Size(min = 7, max = 7)
    private String loanId;

    @NotNull
    private Integer librarianId;

    @NotNull
    private BorrowerDto borrower;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate dateOfLoan;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull
    private List<BookDto> loanedBooks;
}
