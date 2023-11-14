package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.ITimestampResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TimestampResource implements ITimestampResource {
    @Override
    public String addTimestamp() {
        return null;
    }
}
