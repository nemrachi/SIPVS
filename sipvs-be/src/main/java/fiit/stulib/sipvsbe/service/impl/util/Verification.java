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

    protected boolean assertElementAttributeValue(Element element, String attribute, List<String> expectedValues) {
        for (String expectedValue : expectedValues) {
            if (assertElementAttributeValue(element, attribute, expectedValue)) {
                return true;
            }
        }
        return false;
    }

    protected boolean assertElementAttributeValue(Element element, String attribute) {
        String actualValue = element.getAttribute(attribute);
        return !actualValue.isEmpty();
    }

}
