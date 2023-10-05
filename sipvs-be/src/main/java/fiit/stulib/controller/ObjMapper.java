package fiit.stulib.controller;

import fiit.stulib.controller.dto.BookDto;
import fiit.stulib.controller.dto.BorrowerDto;
import fiit.stulib.controller.dto.LibraryLoanDto;
import fiit.stulib.service.model.Book;
import fiit.stulib.service.model.Borrower;
import fiit.stulib.service.model.LibraryLoan;

import java.util.ArrayList;
import java.util.List;

public class ObjMapper {

  public static LibraryLoan fromDto(LibraryLoanDto dto) {
    if (dto == null) {
      return null;
    }
    LibraryLoan obj = new LibraryLoan();
    obj.setLoanId(dto.getLoanId());
    obj.setBorrower(ObjMapper.fromDto(dto.getBorrower()));
    obj.setLoanedBooks(ObjMapper.fromListDto(dto.getLoanedBooks()));
    obj.setLibrarianId(dto.getLibrarianId());
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
