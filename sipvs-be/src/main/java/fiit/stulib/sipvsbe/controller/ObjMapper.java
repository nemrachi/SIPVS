package fiit.stulib.sipvsbe.controller;

import fiit.stulib.sipvsbe.controller.dto.BookDto;
import fiit.stulib.sipvsbe.controller.dto.BorrowerDto;
import fiit.stulib.sipvsbe.controller.dto.LibraryLoanDto;
import fiit.stulib.sipvsbe.service.model.Book;
import fiit.stulib.sipvsbe.service.model.Borrower;
import fiit.stulib.sipvsbe.service.model.LibraryLoan;

import java.util.ArrayList;
import java.util.List;

public class ObjMapper {

    public static LibraryLoan fromDto(LibraryLoanDto dto) {
        if (dto == null) {
            return null;
        }
        LibraryLoan obj = new LibraryLoan();
        obj.setLoanId(dto.getLoanId());
        obj.setLibrarianId(dto.getLibrarianId());
        obj.setBorrower(ObjMapper.fromDto(dto.getBorrower()));
        obj.setDueDate(dto.getDueDate().toString());
        obj.setDateOfLoan(dto.getDateOfLoan().toString());
        obj.setLoanedBooks(ObjMapper.fromListDto(dto.getLoanedBooks()));
        return obj;
    }

    public static Borrower fromDto(BorrowerDto dto) {
        if (dto == null) {
            return null;
        }
        Borrower obj = new Borrower();
        obj.setCardNumber(dto.getCardNumber());
        return obj;
    }

    public static List<Book> fromListDto(List<BookDto> dtos) {
        if (dtos == null) {
            return null;
        }
        List<Book> objs = new ArrayList<>();
        for (BookDto dto : dtos) {
            objs.add(ObjMapper.fromDto(dto));
        }
        return objs;
    }

    public static Book fromDto(BookDto dto) {
        if (dto == null) {
            return null;
        }
        Book obj = new Book();
        obj.setIsbn(dto.getIsbn());
        return obj;
    }
}
