package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.service.AppConfig;
import fiit.stulib.sipvsbe.service.IApplicationService;
import fiit.stulib.sipvsbe.service.model.LibraryLoan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class ApplicationService implements IApplicationService {

    @Override
    public void save(LibraryLoan libraryLoan) {
        try {
            JAXBContext context = JAXBContext.newInstance(LibraryLoan.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(libraryLoan, new File(AppConfig.XML));

            System.out.println("LibraryLoan object saved to result.xml");
        } catch (JAXBException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void validate() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(AppConfig.XSD));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(AppConfig.XML)));

            System.out.println("Validation successful. XML is valid against the XSD.");
        } catch (SAXException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void transform() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File(AppConfig.XSL));

            Transformer transformer = transformerFactory.newTransformer(xslt);
            Source xmlInput = new StreamSource(new File(AppConfig.XML));

            transformer.transform(xmlInput, new StreamResult(new File(AppConfig.HTML)));

            System.out.println("Transformation successful. HTML file created.");
        } catch (TransformerException e) {
            System.err.println("Transformation error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
