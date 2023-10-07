package fiit.stulib.sipvsbe.service.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class Borrower {

    private String cardNumber;
}

