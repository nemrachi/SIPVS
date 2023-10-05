package fiit.stulib.sipvsbe.service.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "LibraryLoan")
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryLoan {

    @XmlElement
    private String loanId;

    @XmlElement
    private Borrower borrower;

    @XmlElement
    private String dateOfLoan;

    @XmlElement
    private String dueDate;

    @XmlElementWrapper(name = "loanedBooks")
    @XmlElement(name = "Book")
    private List<Book> loanedBooks;

    @XmlElement
    private Integer librarianId;
}

