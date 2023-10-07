package fiit.stulib.sipvsbe.service.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryLoan {

    @XmlAttribute(name = "loan_id")
    private String loanId;

    @XmlElement
    private Integer librarianId;

    private Borrower borrower;

    @XmlElement
    private String dateOfLoan;

    @XmlElement
    private String dueDate;

    @XmlElementWrapper(name = "borrowedBooks")
    @XmlElement(name = "book")
    private List<Book> loanedBooks;
}

