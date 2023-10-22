import React from 'react';

/*
* simplier ditec sign calling
*/
function callback(succes, error = (e) => { console.log("error: " + e) }) {
    return {
        onSuccess: succes,
        onError: error
    }
}

function SignComponent() {
    let xml = e.target.result;
    let namespace = "http://library.com/loan";
    let xs_ref = "http://www.w3.org/2001/XMLSchema";
    let xslt_ref = "http://www.w3.org/1999/XSL/Transform";

    ditec.dSigXadesJs.deploy(null, this.callback(function(){			
        				
        ditec.dSigXadesJs.initialize(this.callback(function(){

            ditec.dSigXadesJs.addXmlObject2("xml1", "Formular", xml, xsd, namespace, xs_ref, xslt, xslt_ref, "HTML", this.callback(function(){
            ditec.dSigXadesJs.addPdfObject("objectId_2", "test pdf", decodeURIComponent("@encodedBase64Content"), "", "http://example.com/objectFormatIdentifier", 2, false,  this.callback(function(){
        
                ditec.dSigXadesJs.sign20("signatureId", "http://www.w3.org/2001/04/xmlenc#sha256", "urn:oid:1.3.158.36061701.1.2.3", "dataEnvelopeId", "http://dataEnvelopeURI", "dataEnvelopeDescr", this.callback(function(){
            
                    ditec.dSigXadesJs.getSignedXmlWithEnvelope(this.callback(function(ret) {
                        hiddenElement.href = 'data:text/xml,' + encodeURI(textToSave);
                        hiddenElement.target = '_blank';
                        hiddenElement.download = 'result.xml';
                        hiddenElement.click();
                        // returned signed document
                        alert(ret);
                    }));
                }));
            }));
            }));
        }));
    }));
}

export default SignComponent;