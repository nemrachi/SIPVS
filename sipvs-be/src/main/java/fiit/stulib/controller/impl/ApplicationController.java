package fiit.stulib.controller.impl;

import fiit.stulib.controller.IApplicationController;
import fiit.stulib.controller.ObjMapper;
import fiit.stulib.controller.dto.LibraryLoanDto;
import fiit.stulib.service.IApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@Slf4j
public class ApplicationController implements IApplicationController {

  @Autowired
  private IApplicationService applicationService;

  public void save(LibraryLoanDto libraryLoanDto) {
    applicationService.save(ObjMapper.fromDto(libraryLoanDto));
  }

  public void validate() {
    applicationService.validate();
  }

  public void transform() {
    applicationService.transform();
  }
}
