package fiit.stulib.sipvsbe.service;

import fiit.stulib.sipvsbe.controller.dto.VerifyResultDto;

import java.util.List;

public interface IVerificationService {

    List<VerifyResultDto> verify();
}
