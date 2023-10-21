package fiit.stulib.sipvsbe.service.impl;

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
import java.io.*;

@Slf4j
@Service
public class ApplicationService implements IApplicationService {

    @Override
    public String save(LibraryLoan libraryLoan) {
        try {
            StringWriter xml = new StringWriter();

            // Create a JAXB context for the LibraryLoan class
            JAXBContext context = JAXBContext.newInstance(LibraryLoan.class);

            // Create a Marshaller
            Marshaller marshaller = context.createMarshaller();

            // Pretty-print the XML (optional)
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Marshal the LibraryLoan object to an XML file
            marshaller.marshal(libraryLoan, xml);

            try (PrintWriter out = new PrintWriter("src/main/resources/out/result.xml")) {
                out.println(xml);
                System.out.println("LibraryLoan object saved to result.xml");
            }

            return xml.toString();
        } catch (JAXBException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void validate() {
        try {
            // Create a SchemaFactory for XML Schema
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Load the XSD schema from a file (replace with your XSD file)
            Schema schema = factory.newSchema(new File("src/main/resources/templates/libraryLoan.xsd"));

            // Create a Validator from the schema
            Validator validator = schema.newValidator();

            // Validate the XML file (replace with your XML file)
            validator.validate(new StreamSource(new File("src/main/resources/out/result.xml")));

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

            Source xslt = new StreamSource(new File("src/main/resources/templates/libraryLoan.xsl"));

            Transformer transformer = transformerFactory.newTransformer(xslt);

            Source xmlInput = new StreamSource(new File("src/main/resources/out/result.xml"));

            StreamResult file = new StreamResult(new File("src/main/resources/out/result.html"));
            transformer.transform(xmlInput, file);

            System.out.println("Transformation successful. HTML file created.");
        } catch (TransformerException e) {
            System.err.println("Transformation error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


}
