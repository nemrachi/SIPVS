package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.IVerificationResource;
import fiit.stulib.sipvsbe.controller.dto.VerifyResultDto;
import fiit.stulib.sipvsbe.service.IVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class VerificationResource implements IVerificationResource {

    @Autowired
    private IVerificationService verificationService;

    @Override
    public List<VerifyResultDto> verify() {
        return verificationService.verify();
    }
}
