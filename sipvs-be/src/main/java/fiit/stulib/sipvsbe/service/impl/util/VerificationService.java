package fiit.stulib.sipvsbe.service.impl.util;

import fiit.stulib.sipvsbe.controller.dto.VerifyResultDto;
import fiit.stulib.sipvsbe.service.IVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VerificationService implements IVerificationService {

    @Override
    public VerifyResultDto verify() {
        VerifyResultDto result = new VerifyResultDto();


        return result;
    }
}
