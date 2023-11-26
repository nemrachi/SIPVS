package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.IVerificationResource;
import fiit.stulib.sipvsbe.controller.dto.VerifyResultDto;
import fiit.stulib.sipvsbe.service.IVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class VerificationResource implements IVerificationResource {

    @Autowired
    private IVerificationService verificationService;

    @Override
    public VerifyResultDto verify() {
        return verificationService.verify();
    }
}
