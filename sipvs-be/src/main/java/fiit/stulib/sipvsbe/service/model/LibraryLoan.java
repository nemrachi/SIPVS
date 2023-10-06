package fiit.stulib.sipvsbe.service.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "LibraryLoan")
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryLoan {

    @XmlElement(name = "loanId")
    private String loanId;

    private Borrower borrower;

    @XmlElement(name = "dateOfLoan")
    private String dateOfLoan;

    @XmlElement(name = "dueDate")
    private String dueDate;

    @XmlElementWrapper(name = "loanedBooks")
    @XmlElement(name = "book")
    private List<Book> loanedBooks;

    @XmlElement(name = "librarianId")
    private Integer librarianId;
}

