
import org.w3c.dom.NodeList;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.IOException;


public class Bank2RequestSoap {

    LoanRequest loanRequest;
    String soapEndpointUrl = "http://localhost/loanProject/bankSoap.php";
    String soapAction = "http://localhost/loanProject/bankSoap.php";

    public Bank2RequestSoap(LoanRequest req){
        loanRequest = req;



    }


    private void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "myNamespace";
        String myNamespaceURI = "https://www.w3schools.com/xml/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getInterest", myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("ssn", myNamespace);
        soapBodyElem1.addTextNode(""+loanRequest.getSsn());
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("creditScore", myNamespace);
        soapBodyElem2.addTextNode(""+loanRequest.getCreditScore());
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("duration", myNamespace);
        soapBodyElem3.addTextNode(""+loanRequest.getLoanDuration());
        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("loan", myNamespace);
        soapBodyElem4.addTextNode(""+loanRequest.getLoanAmount());
    }

    public String callSoapWebService() {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction), soapEndpointUrl);

            soapConnection.close();
            return decode(soapResponse);
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
        return "";
    }

    private SOAPMessage createSOAPRequest(String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();
        return soapMessage;
    }

    private String decode(SOAPMessage message) throws IOException, SOAPException {

        SOAPBody body = message.getSOAPBody();

        NodeList list = body.getElementsByTagName("return");

        for (int i = 0; i < list.getLength(); i++) {
            NodeList innerList = list.item(i).getChildNodes();

            for (int j = 0; j < innerList.getLength(); j++) {
                return innerList.item(j).getTextContent();
            }
        }
        return "";
    }
}