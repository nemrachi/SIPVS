package fiit.stulib.sipvsbe.service;

import fiit.stulib.sipvsbe.service.model.LibraryLoan;

public interface IApplicationService {

    String save(LibraryLoan libraryLoan);

    void validate();

    void transform();
}
