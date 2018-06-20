package com.seclore.sample.ara.webservice;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.oracle.casb.seclore.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.seclore.sample.dms.util.LoggerUtil;

/**
 * XML helper class used to parse information from request XML.
 *
 * @author harindra.chaudhary
 */
public class XMLHelper {
    public static final String PROTOCOL_VERSION = "1";
    public static final String SUCCESS = "1";
    public static final Short STATUS_ERROR = -2;
    public static final Short STATUS_FAILED = -1;
    private static XPath xpath = XPathFactory.newInstance().newXPath();

    public static Document parseDocument(String xmlString) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new InputSource(new StringReader(xmlString)));
            return xmlDocument;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Node getRootNode(Document document) {
        NodeList nodeList = document.getChildNodes();

        return nodeList.item(0);
    }

    /**
     * @param xmlTagName
     * @param parentNode
     * @return
     */
    public static Node parseNode(String xmlTagName, Node parentNode) {
        try {
            Node node = (Node) xpath.evaluate(xmlTagName, parentNode, XPathConstants.NODE);
            return node;
        } catch (XPathExpressionException e) {
            LoggerUtil.logError("Error while parsing - " + xmlTagName, e);
        }
        return null;
    }

    /**
     * @param xmlTagName
     * @param parentNode
     * @return
     */
    public static NodeList parseNodeList(String xmlTagName, Node parentNode) {
        try {
            NodeList nodeList = (NodeList) xpath.evaluate(xmlTagName, parentNode, XPathConstants.NODESET);
            return nodeList;
        } catch (XPathExpressionException e) {
            LoggerUtil.logError("Error while parsing - " + xmlTagName, e);
        }
        return null;
    }

    public static String parseRequestId(Node parentNode) {
        try {
            String requestId = (String) xpath
                    .evaluate("//ara-request-header/request-id/text()", parentNode, XPathConstants.STRING);
            return requestId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ARAUserDetails parseUserDetails(Node parentNode) {
        try {
            Node node = (Node) xpath.evaluate("//ara-request-details-get-access-right/ara-user-details", parentNode,
                    XPathConstants.NODE);
            if (node == null) {
                return null;
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(ARAUserDetails.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ARAUserDetails userDetails = (ARAUserDetails) jaxbUnmarshaller.unmarshal(node);
            return userDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ARAHotFolderDetails parseHFDetails(Node parentNode) {
        try {
            Node node = (Node) xpath.evaluate("ara-hot-folder-details", parentNode, XPathConstants.NODE);
            if (node == null) {
                return null;
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(ARAHotFolderDetails.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ARAHotFolderDetails hfDetails = (ARAHotFolderDetails) jaxbUnmarshaller.unmarshal(node);
            return hfDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ARAFileDetails parseFileDetails(Node parentNode) {
        try {
            Node node = (Node) xpath.evaluate("ara-file-details", parentNode, XPathConstants.NODE);
            if (node == null) {
                return null;
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(ARAFileDetails.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ARAFileDetails fileDetails = (ARAFileDetails) jaxbUnmarshaller.unmarshal(node);
            return fileDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ARAClassificationDetails parseClassification(Node parentNode) {
        try {
            Node node = (Node) xpath.evaluate("ara-classification-details", parentNode, XPathConstants.NODE);
            if (node == null) {
                return null;
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(ARAClassificationDetails.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ARAClassificationDetails classificationDetails = (ARAClassificationDetails) jaxbUnmarshaller
                    .unmarshal(node);
            return classificationDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ARAOwnerDetails parseOwnerDetails(Node parentNode) {
        try {
            Node node = (Node) xpath.evaluate("ara-owner-details", parentNode, XPathConstants.NODE);
            if (node == null) {
                return null;
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(ARAOwnerDetails.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ARAOwnerDetails ownerDetails = (ARAOwnerDetails) jaxbUnmarshaller.unmarshal(node);
            return ownerDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generatOwnerDetailsXML(ARAOwnerDetails pARAOwnerDetails) throws Exception {
        if (pARAOwnerDetails == null) {
            return "";
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ARAOwnerDetails.class);
            Marshaller lMarshaller = jaxbContext.createMarshaller();
            lMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            lMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter lsw = new StringWriter();
            lMarshaller.marshal(pARAOwnerDetails, new StreamResult(lsw));
            return lsw.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public static String generatClassificationDetailsXML(ARAClassificationDetails classificationDetails)
            throws Exception {
        if (classificationDetails == null) {
            return "";
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ARAClassificationDetails.class);
            Marshaller lMarshaller = jaxbContext.createMarshaller();
            lMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            lMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter lsw = new StringWriter();
            lMarshaller.marshal(classificationDetails, new StreamResult(lsw));
            return lsw.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public static String escapeForXML(String strXML) {
        if (strXML == null || strXML.trim().isEmpty()) {
            return strXML;
        }

        return strXML.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
