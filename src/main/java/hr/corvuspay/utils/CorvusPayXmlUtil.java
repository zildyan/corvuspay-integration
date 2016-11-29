package hr.corvuspay.utils;

import hr.corvuspay.types.CorvusPayResponseCodeType;
import hr.corvuspay.types.CorvusPayResponseType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.CorvusPayInvalidResponse;
import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.CorvusPayResponseParseException;
import static hr.corvuspay.types.CorvusPayResponseType.*;

public class CorvusPayXmlUtil {

    public static Map<CorvusPayResponseType, String> parseResponse(String response) {
        if(response == null || response.isEmpty())
            throw new CorvusPayInvalidResponse("Response is empty.");
        
        Document doc = getParsedDocument(response);
        doc.normalizeDocument();

        if(containsElementNode(doc, "errors"))
            handleErrors(doc);
        else if (!containsElementNode(doc, "order"))
            throw new CorvusPayInvalidResponse("Tag order missing from response.");

        final Element orderElement = getElement(doc, "order");
        final String responseCode = getTextContent(orderElement, "response-code");
        final String responseMessage = getTextContent(orderElement, "response-message");
        final String responseCodeDescription = getResponseDescription(responseCode);

        return new HashMap<CorvusPayResponseType, String>(){{
            put(RESPONSE_CODE, responseCode);
            put(RESPONSE_MESSAGE, responseMessage);
            put(RESPONSE_DESCRIPTION, responseCodeDescription);
        }};
    }

    private static Document getParsedDocument(String response) {
        try {
            return DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(response.getBytes("utf-8"))));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new CorvusPayResponseParseException(e);
        }
    }

    private static boolean containsElementNode(Document doc, String tagName) {
        NodeList elementNodesByTagName = doc.getElementsByTagName(tagName);
        return elementNodesByTagName != null &&
                elementNodesByTagName.item(0).getNodeType() == Node.ELEMENT_NODE;
    }

    private static void handleErrors(Document doc) {
        Element errElement = getElement(doc, "errors");
        if(errElement.getElementsByTagName("description") != null){
            throw new CorvusPayInvalidResponse("CorvusPay error: " + getTextContent(errElement, "description"));
        } else {
            throw new CorvusPayInvalidResponse("CorvusPay error undefined");
        }
    }

    private static Element getElement(Document doc, String tagName) {
        return (Element) doc.getElementsByTagName(tagName).item(0);
    }

    private static String getTextContent(Element element, String tagName) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

    private static String getResponseDescription(String responseCode) {
        return CorvusPayResponseCodeType.valueOf("CODE_" + responseCode).getDescription();
    }
}
