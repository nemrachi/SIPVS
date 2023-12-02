package fiit.stulib.sipvsbe.service.impl.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.security.Security;
import java.util.List;

/**
 * Includes common verification functions
 */
public abstract class Verification {

    protected Document document;

    public Verification(Document document) {
        this.document = document;
        org.apache.xml.security.Init.init();
        Security.addProvider(new BouncyCastleProvider());
    }

    protected boolean assertElementAttributeValue(Element element, String attribute, String expectedValue) {
        String actualValue = element.getAttribute(attribute);
        return actualValue != null && actualValue.equals(expectedValue);
    }

    protected boolean assertElementAttributeValue(Element element, List<String> expectedValues) {
        for (String expectedValue : expectedValues) {
            if (assertElementAttributeValue(element, "Algorithm", expectedValue)) {
                return false;
            }
        }
        return true;
    }

    protected boolean assertElementAttributeValue(Element element) {
        String actualValue = element.getAttribute("Id");
        return actualValue.isEmpty();
    }

}
