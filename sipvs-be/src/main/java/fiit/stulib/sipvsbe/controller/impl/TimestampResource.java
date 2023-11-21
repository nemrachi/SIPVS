package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.ITimestampResource;
import fiit.stulib.sipvsbe.service.ITimestampService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TimestampResource implements ITimestampResource {

    @Autowired
    private ITimestampService timestampService;

    @Override
    public String addTimestamp(String signedXml) {
        String timestamped = timestampService.createTimestamp(signedXml);
        return timestamped;
    }
}
