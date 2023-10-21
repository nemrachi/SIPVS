package fiit.stulib.sipvsbe.service;

import fiit.stulib.sipvsbe.service.model.LibraryLoan;

public interface IApplicationService {

    void save(LibraryLoan libraryLoan);

    void validate();

    void transform();

    String sign();
}
