package fiit.stulib.sipvsbe.service.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "Book")
public class Book {

    private String isbn;
}

