package fiit.stulib.service;

import fiit.stulib.service.model.LibraryLoan;

public interface IApplicationService {

  void save(LibraryLoan libraryLoan);

  void validate();

  String transform();
}
