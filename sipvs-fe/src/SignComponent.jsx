import React from 'react';
import {getPDF, getXML, getXSD, getXSL} from "./api.js";

/*
* simplier ditec sign calling
*/
function callback(succes, error = (e) => {
    console.log("error: " + e)
}) {
    return {
        onSuccess: succes,
        onError: error
    }
}
async function SignComponent() {
    let xml = await getXML();
    let xsd = await getXSD();
    let xslt = await getXSL();
    const pdfBase64 = await getPDF();
    let namespace = "http://library.com/loan";
    let xs_ref = "http://www.w3.org/2001/XMLSchema";
    let xslt_ref = "http://www.w3.org/1999/XSL/Transform";

    ditec.dSigXadesJs.deploy(null, callback(function () {
        ditec.dSigXadesJs.initialize(callback(function () {
            ditec.dSigXadesJs.addXmlObject2("one", "Formular", xml, xsd, namespace, xs_ref, xslt, xslt_ref, "HTML", callback(function () {
                ditec.dSigXadesJs.addPdfObject("two", "LoanPDF", pdfBase64, "", "http://objectFormatIdentifier", 3, false, callback(function () {
                    ditec.dSigXadesJs.sign20("signatureId", "http://www.w3.org/2001/04/xmlenc#sha256", "urn:oid:1.3.158.36061701.1.2.3", "dataEnvelopeId", "http://dataEnvelopeURI", "dataEnvelopeDescr", callback(function () {
                        ditec.dSigXadesJs.getSignedXmlWithEnvelope(callback(function (ret) {
                            const blob = new Blob([ret], { type: "text/xml" });
                            const url = URL.createObjectURL(blob);
                            const link = document.createElement("a");
                            link.href = url;
                            link.download = "signedLoan.xml";
                            link.click();
                        }));
                    }));
                }));
            }));
        }));
    }));
}

export default SignComponent;