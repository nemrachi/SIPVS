package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.ITimestampResource;
import fiit.stulib.sipvsbe.service.ITimestampService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
public class TimestampResource implements ITimestampResource {

    @Autowired
    private ITimestampService timestampService;

    @Override
    public String addTimestamp(MultipartFile file) {

        if (file.isEmpty()) {
            return "File is empty";
        }

        String signedXml = null;
        try {
            InputStream inputStream = file.getInputStream();
            signedXml = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (signedXml == null) {
            throw new RuntimeException("Can not read file");
        }

        return timestampService.createTimestamp(signedXml);
    }
}
