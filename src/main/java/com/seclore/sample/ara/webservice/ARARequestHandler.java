package com.seclore.sample.ara.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.oracle.casb.seclore.model.*;
import com.oracle.casb.seclore.service.FilePermissionService;
import com.oracle.casb.seclore.service.impl.FilePermissionServiceImpl;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seclore.sample.dms.constant.Constants;
import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.Classification;
import com.seclore.sample.dms.core.Owner;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

import java.io.IOException;

/**
 * Access Right Adaptor (ARA) request handler which receive request from Policy Server and return response with DMS rights.
 *
 * @author harindra.chaudhary
 */
@Path("/request")
@Provider
public class ARARequestHandler {

    Logger logger = LoggerFactory.getLogger(ARARequestHandler.class);

    FilePermissionService filePermissionService;

    XmlMapper xmlMapper;

    public ARARequestHandler() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        filePermissionService = new FilePermissionServiceImpl(objectMapper);
        xmlMapper = new XmlMapper();
    }


    @GET
    public String hello() {
        return "SecloreWebApp Hello World!";
    }
    /**
     * Handle Access Right Adaptor ping request
     *
     * @param pingRequsetXML - Request XML received in the HTTP request body
     * @return Response XML
     */
    @GET
    @POST
    @Path("/ping")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String handleARAPingRequest(String pingRequsetXML) {
        String lstrMethodName = this.getClass().getName() + "::handleARAPingRequest:: ";
        logger.debug(lstrMethodName + "START");

        Document document = XMLHelper.parseDocument(pingRequsetXML);
        Node rootNode = XMLHelper.getRootNode(document);

        String requestId = XMLHelper.parseRequestId(rootNode);
        String errorMessageXML = "";
        String displayMessageXML = "";
        String pingResponseXML = "<ara-response-ping type=\"1\">" + "<ara-response-header>" + "<request-id>" + XMLHelper
                .escapeForXML(requestId) + "</request-id>" + "<protocol-version>1</protocol-version>" + "<status>"
                + XMLHelper.SUCCESS + "</status>" + errorMessageXML + displayMessageXML + "</ara-response-header>" +

                "<ara-response-details-ping>" + "<ara-supported-protocol-versions>" + "<protocol-version>"
                + XMLHelper.PROTOCOL_VERSION + "</protocol-version>" + "</ara-supported-protocol-versions>" +

                "<ara-supported-services>" + "<service-type>1</service-type>" + "<service-type>2</service-type>"
                + "<service-type>3</service-type>" + "<service-type>4</service-type>" + "</ara-supported-services>"
                + "</ara-response-details-ping>" +

                "</ara-response-ping>";

        String lstrReqResp = "<request-response>" + pingRequsetXML + pingResponseXML + "</request-response>";
        logger.debug(lstrMethodName + "[" + requestId + "]:" + "Request and Response XML: " + lstrReqResp);
        logger.debug(lstrMethodName + "[" + requestId + "]:" + "END");
        return pingResponseXML;
    }


    /**
     * Handle Access Right Adaptor get access right request
     * @return XML response
     * @param requsetXML - Request XML received in the HTTP request body
     * @return Response XML
     */
    @GET
    @POST
    @Path("/getaccessright")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String handleARAGetAccessRight(String requsetXML)
    {
        String lstrMethodName = this.getClass().getName() + "::handleARAGetAccessRight:: ";
        logger.debug(lstrMethodName + "START");
        logger.debug( lstrMethodName + ": Received ARA Request XML:- " + requsetXML);

        Document document = XMLHelper.parseDocument( requsetXML );
        Node rootNode = XMLHelper.getRootNode( document );
        String requestId = XMLHelper.parseRequestId( rootNode );

        String errorMessageXML = "";
        String displayMessageXML = "";
        String status = XMLHelper.SUCCESS;
        int fsPrimaryRight = 0;
        String fileName = "";
        String lstrXMLOwner = "";
        String lstrXMLClassification = "";
        String lstrXMLARADispMsg = "";
        try
        {
            Node requestDetailsNode = XMLHelper.parseNode("ara-request-details-get-access-right", rootNode);
            ARAHotFolderDetails  		hfDetails 		= XMLHelper.parseHFDetails( requestDetailsNode );
            ARAClassificationDetails 	classificationDetails 	= XMLHelper.parseClassification( requestDetailsNode );
            ARAUserDetails 				userDetails 	= XMLHelper.parseUserDetails( requestDetailsNode );
            ARAFileDetails 				fileDetails 	= XMLHelper.parseFileDetails( requestDetailsNode );
            ARAOwnerDetails 			ownereDetails 	= XMLHelper.parseOwnerDetails( requestDetailsNode );

            fileName = fileDetails.getExtName();
            String lstrExtnData = fileDetails.getExtData();

            if( lstrExtnData == null || lstrExtnData.trim().isEmpty() )
            {
                throw new Exception("File External Reference data not found!");
            }

            String userId = userDetails.getExtId();
            String emailId = userDetails.getEmailId();

            logger.debug( lstrMethodName + "["+requestId+"]:" +" UserId: "+userId);
            logger.debug( lstrMethodName + "["+requestId+"]:" + " EmailId: "+emailId);

            JsonParser lJsonParser = new JsonParser();
            JsonObject lJsonObj =(JsonObject) lJsonParser.parse( lstrExtnData.trim() );
            JsonElement lJsonElement = lJsonObj.get( Constants.KEY_HF_EXTN_REF_ID );
            if( lJsonElement == null )
            {
                throw new Exception("'" + Constants.KEY_HF_EXTN_REF_ID + "' not found in file external data");
            }
            String lstrExtnHfid =  lJsonElement.getAsString();

            // int rights = XMLDBService.getUserRights( userId, emailId, hfDetails.getExtId() ,fileDetails.getExtId() );
            int rights = XMLDBService.getUserRights( userId, emailId, lstrExtnHfid, fileDetails.getExtId() );

            if( rights == Constants.FILE_NOT_EXIST)
            {
                status = "-1";
                errorMessageXML = "<error-message>"+XMLHelper.escapeForXML("Requested File '"+fileDetails.getExtName()+"' does not exist")+"</error-message>";
                displayMessageXML = "<display-message>"+XMLHelper.escapeForXML("Requested File '"+fileDetails.getExtName()+"' does not exist")+"</display-message>";
                logger.debug(lstrMethodName + "["+requestId +"]:" +" Requested File '"+fileDetails.getExtName()+"' does not exist");
            }
            else if( rights == Constants.NO_RIGHTS)
            {
                lstrXMLARADispMsg = "<ara-display-message>"+XMLHelper.escapeForXML("User'"+userDetails.getName()+"' does not have rights on file '"+fileDetails.getExtName()+"'")+"</ara-display-message>";
                logger.debug(lstrMethodName + "["+requestId +"]:" +" User'"+userDetails.getName()+"' does not have rights on file '"+fileDetails.getExtName()+"'");
            }
            else
            {
                fsPrimaryRight = mapWithFileSecureRights( rights );
            }

            //get file owner
            if( status == XMLHelper.SUCCESS )
            {
                //Prepare Owner XML
                AppFile lFile = XMLDBService.getFile(lstrExtnHfid, fileDetails.getExtId());
                Owner lOwner = lFile.getOwner();
                lstrXMLOwner = prepareOwnerXML( requestId, lOwner, ownereDetails );

                //Prepare Classification XML
                Classification lClassification = lFile.getClassification();
                lstrXMLClassification = prepareClassificationXML( requestId, lClassification, classificationDetails );

            }
        }
        catch(Exception exp)
        {
            status = "-290013";
            errorMessageXML = "<error-message>"+XMLHelper.escapeForXML("Error occured while fetching the rights - "+exp.getMessage())+"</error-message>";
            displayMessageXML = "<display-message>"+XMLHelper.escapeForXML("Some error occured while fetching the rights")+"</display-message>";
            LoggerUtil.logError(lstrMethodName + "["+requestId +"]:" +" Error occured while fetching the rights - "+exp.getMessage(), exp);
        }

        // Construct response header XML
        String respHeaderXML = "<ara-response-header>" +
                "<request-id>" + requestId + "</request-id>" +
                "<protocol-version>1</protocol-version>" +
                "<status>" + status + "</status>" +
                errorMessageXML +
                displayMessageXML +
                "</ara-response-header>";


        String respDetailsXML = "";
        if( status == XMLHelper.SUCCESS)
        {
            respDetailsXML = generateResponseDetailsXML( fsPrimaryRight, lstrXMLARADispMsg, fileName, lstrXMLOwner, lstrXMLClassification );
        }

        // Construct response XML
        String responseXML = "<ara-response-get-access-right type=\"2\">" +
                respHeaderXML +
                respDetailsXML +
                "</ara-response-get-access-right>";

        String lstrReqResp = "<request-response>" + requsetXML + responseXML + "</request-response>";
        LoggerUtil.logInfo(lstrMethodName + "["+requestId +"]:" +" Request and Response XML: " + lstrReqResp);
        logger.debug(lstrMethodName + "["+requestId +"]:" +"END");
        return responseXML;
    }


    /**
     * Handle Access Right Adaptor get access right request
     *
     * @param request - Request XML received in the HTTP request body
     * @return Response XML
     */
    @GET
    @POST
    @Path("/getaccessrights")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public AccessRightResponse handleARAGetAccessRight(AccessRightRequest request) throws IOException {
        logger.debug("Request id " +request.getAraHeader().getRequestId());
        logger.info("User object "
                + ReflectionToStringBuilder
                .reflectionToString(request.getAraRequestDetailsGetAccessRight().getAraUserDetails()));
        logger.info("Owner object "
                + ReflectionToStringBuilder
                .reflectionToString(request.getAraRequestDetailsGetAccessRight().getAraOwnerDetails().getAraUserDetails()));
        logger.info("File object "
                + ReflectionToStringBuilder
                .reflectionToString(request.getAraRequestDetailsGetAccessRight().getAraFileDetails()));
        logger.info("Hot Folder object "
                + ReflectionToStringBuilder
                .reflectionToString(request.getAraRequestDetailsGetAccessRight().getAraHotFolderDetails()));
        //return filePermissionService.getFileAccessPermissions(request);
        return getResponse(request);
    }

    private AccessRightResponse getResponse(AccessRightRequest request) throws IOException {
        String lstrMethodName = this.getClass().getName() + "::handleARAGetAccessRight:: ";

        String requestId = request.getAraHeader().getRequestId();

        String errorMessageXML = "";
        String displayMessageXML = "";

        int fsPrimaryRight = 0;
        String fileName = "";
        String lstrXMLOwner = "";
        String lstrXMLClassification = "";
        String lstrXMLARADispMsg = "";
        String status = XMLHelper.SUCCESS;
        try {
            ARAHotFolderDetails hfDetails = request.getAraRequestDetailsGetAccessRight().getAraHotFolderDetails();
            ARAClassificationDetails classificationDetails = request.getAraRequestDetailsGetAccessRight().getAraClassificationDetails();
            ARAUserDetails userDetails = request.getAraRequestDetailsGetAccessRight().getAraUserDetails();
            ARAFileDetails fileDetails = request.getAraRequestDetailsGetAccessRight().getAraFileDetails();
            ARAOwnerDetails ownereDetails = request.getAraRequestDetailsGetAccessRight().getAraOwnerDetails();

            fileName = fileDetails.getExtName();
            String lstrExtnData = fileDetails.getExtData();

            if (lstrExtnData == null || lstrExtnData.trim().isEmpty()) {
                throw new Exception("File External Reference data not found!");
            }
            logger.debug("File Name " + fileDetails.getExtName());
            logger.debug("File External Reference" + fileDetails.getExtData());
            logger.debug("User Email ID : " + userDetails.getEmailId());
            logger.debug("User External Ref ID : " + userDetails.getExtId());
            String userId = userDetails.getExtId();
            String emailId = userDetails.getEmailId();


            JsonParser lJsonParser = new JsonParser();
            JsonObject lJsonObj = (JsonObject) lJsonParser.parse(lstrExtnData.trim());
            JsonElement lJsonElement = lJsonObj.get(Constants.KEY_HF_EXTN_REF_ID);
            if (lJsonElement == null) {
                throw new Exception("'" + Constants.KEY_HF_EXTN_REF_ID + "' not found in file external data");
            }
            String lstrExtnHfid = lJsonElement.getAsString();

            logger.debug("External file hf-id lstrExtnHfid " + lstrExtnHfid);
            // int rights = XMLDBService.getUserRights( userId, emailId, hfDetails.getExtId() ,fileDetails.getExtId() );
            int rights = XMLDBService.getUserRights(userId, emailId, lstrExtnHfid, fileDetails.getExtId());

            if (rights == Constants.FILE_NOT_EXIST) {
                status = "-1";
                errorMessageXML = "<error-message>" + XMLHelper
                        .escapeForXML("Requested File '" + fileDetails.getExtName() + "' does not exist")
                        + "</error-message>";
                displayMessageXML = "<display-message>" + XMLHelper
                        .escapeForXML("Requested File '" + fileDetails.getExtName() + "' does not exist")
                        + "</display-message>";
                logger.debug(
                        lstrMethodName + "[" + requestId + "]:" + " Requested File '" + fileDetails.getExtName()
                                + "' does not exist");
            } else if (rights == Constants.NO_RIGHTS) {
                lstrXMLARADispMsg = "<ara-display-message>" + XMLHelper.escapeForXML(
                        "User'" + userDetails.getName() + "' does not have rights on file '" + fileDetails.getExtName()
                                + "'") + "</ara-display-message>";
                logger.debug(lstrMethodName + "[" + requestId + "]:" + " User'" + userDetails.getName()
                        + "' does not have rights on file '" + fileDetails.getExtName() + "'");
            } else {
                fsPrimaryRight = mapWithFileSecureRights(rights);
            }
            //get file owner
            if (status == XMLHelper.SUCCESS) {
                //Prepare Owner XML
                AppFile lFile = XMLDBService.getFile(lstrExtnHfid, fileDetails.getExtId());
                Owner lOwner = lFile.getOwner();
                lstrXMLOwner = prepareOwnerXML(requestId, lOwner, ownereDetails);

                //Prepare Classification XML
                Classification lClassification = lFile.getClassification();
                lstrXMLClassification = prepareClassificationXML(requestId, lClassification, classificationDetails);

            }
        } catch (Exception exp) {
            status = "-290013";
            errorMessageXML = "<error-message>" + XMLHelper
                    .escapeForXML("Error occured while fetching the rights - " + exp.getMessage()) + "</error-message>";
            displayMessageXML =
                    "<display-message>" + XMLHelper.escapeForXML("Some error occured while fetching the rights")
                            + "</display-message>";
            logger.error(
                    lstrMethodName + "[" + requestId + "]:" + " Error occured while fetching the rights - " + exp
                            .getMessage(), exp);
        }

        // Construct response header XML
        String respHeaderXML = "<ara-response-header>" + "<request-id>" + requestId + "</request-id>"
                + "<protocol-version>1</protocol-version>" + "<status>" + status + "</status>" + errorMessageXML
                + displayMessageXML + "</ara-response-header>";

        String respDetailsXML = "";
        if (status == XMLHelper.SUCCESS) {
            respDetailsXML = generateResponseDetailsXML(fsPrimaryRight, lstrXMLARADispMsg, fileName, lstrXMLOwner,
                    lstrXMLClassification);
        }

        // Construct response XML
        String responseXML = "<ara-response-get-access-right type=\"2\">" + respHeaderXML + respDetailsXML
                + "</ara-response-get-access-right>";

        logger.debug(lstrMethodName + "[" + requestId + "]:" + " Request and Response XML: " + responseXML);
        logger.debug(lstrMethodName + "[" + requestId + "]:" + "END");
        return xmlMapper.readValue(responseXML, AccessRightResponse.class);
    }

    @GET
    @POST
    @Path("/getfileinformation")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String handleARAGetFileDetails(
            String requsetXML) {
        String lstrMethodName = this.getClass().getName() + "::handleARAGetFileDetails:: ";
        logger.debug(lstrMethodName + "START");
        logger.debug(lstrMethodName + "Received GetFileDetails Request XML:- " + requsetXML);

        Document document = XMLHelper.parseDocument(requsetXML);
        Node rootNode = XMLHelper.getRootNode(document);
        String requestId = XMLHelper.parseRequestId(rootNode);

        String errorMessageXML = "";
        String displayMessageXML = "";
        String status = XMLHelper.SUCCESS;
        String fileName = "";
        String lstrXMLOwner = "";
        String lstrXMLClassification = "";
        try {
            Node requestDetailsNode = XMLHelper.parseNode("ara-request-details-get-file-information", rootNode);
            ARAHotFolderDetails hfDetails = XMLHelper.parseHFDetails(requestDetailsNode);
            ARAClassificationDetails classificationDetails = XMLHelper.parseClassification(requestDetailsNode);
            ARAFileDetails fileDetails = XMLHelper.parseFileDetails(requestDetailsNode);
            ARAOwnerDetails ownereDetails = XMLHelper.parseOwnerDetails(requestDetailsNode);

            fileName = fileDetails.getExtName();
            String lstrExtnData = fileDetails.getExtData();

            if (lstrExtnData == null || lstrExtnData.trim().isEmpty()) {
                throw new Exception("File External Reference data not found!");
            }

            logger.debug(lstrMethodName + "[" + requestId + "]:" + "FS FileID: " + fileDetails.getFsId());
            logger.debug(lstrMethodName + "[" + requestId + "]:" + "Ext FileID: " + fileDetails.getExtId());
            logger.debug(lstrMethodName + "[" + requestId + "]:" + "Ext FileName: " + fileName);

            JsonParser lJsonParser = new JsonParser();
            JsonObject lJsonObj = (JsonObject) lJsonParser.parse(lstrExtnData.trim());
            JsonElement lJsonElement = lJsonObj.get(Constants.KEY_HF_EXTN_REF_ID);
            if (lJsonElement == null) {
                throw new Exception("'" + Constants.KEY_HF_EXTN_REF_ID + "' not found in file external data");
            }
            String lstrExtnHfid = lJsonElement.getAsString();

            AppFile lFile = XMLDBService.getFile(lstrExtnHfid, fileDetails.getExtId());
            if (lFile == null) {
                status = "-1";
                errorMessageXML = "<error-message>" + XMLHelper
                        .escapeForXML("Requested File '" + fileDetails.getExtName() + "' does not exist")
                        + "</error-message>";
                displayMessageXML = "<display-message>" + XMLHelper
                        .escapeForXML("Requested File '" + fileDetails.getExtName() + "' does not exist")
                        + "</display-message>";
                logger.debug(
                        lstrMethodName + "[" + requestId + "]:" + " Requested File '" + fileDetails.getExtName()
                                + "' does not exist");
            } else {
                // Prepare Owner XML
                Owner lOwner = lFile.getOwner();
                lstrXMLOwner = prepareOwnerXML(requestId, lOwner, ownereDetails);

                // Prepare Classification XML
                Classification lClassification = lFile.getClassification();
                lstrXMLClassification = prepareClassificationXML(requestId, lClassification, classificationDetails);
            }
        } catch (Exception exp) {
            status = "-2";
            errorMessageXML = "<error-message>" + XMLHelper
                    .escapeForXML("Error occured while fetching the file details- " + exp.getMessage())
                    + "</error-message>";
            displayMessageXML =
                    "<display-message>" + XMLHelper.escapeForXML("Some error occured while fetching the file details")
                            + "</display-message>";
            LoggerUtil.logError(
                    lstrMethodName + "[" + requestId + "]:" + " Error occured while fetching the file details - " + exp
                            .getMessage(), exp);
        }

        // Construct response header XML
        String respHeaderXML = "<ara-response-header>" + "<request-id>" + requestId + "</request-id>"
                + "<protocol-version>1</protocol-version>" + "<status>" + status + "</status>" + errorMessageXML
                + displayMessageXML + "</ara-response-header>";

        String respDetailsXML = "";
        if (status == XMLHelper.SUCCESS) {
            respDetailsXML = "<ara-response-details-get-file-information>" + lstrXMLClassification + lstrXMLOwner
                    + "</ara-response-details-get-file-information>";

        }

        // Construct response XML
        String responseXML = "<ara-response-get-file-information type=\"3\">" + respHeaderXML + respDetailsXML
                + "</ara-response-get-file-information>";

        String lstrReqResp = "<request-response>" + requsetXML + responseXML + "</request-response>";
        LoggerUtil.logInfo(lstrMethodName + "[" + requestId + "]:" + "Request and Response XML: " + lstrReqResp);
        logger.debug(lstrMethodName + "[" + requestId + "]:" + "END");
        return responseXML;
    }

    /**
     * Generate get access right response details
     *
     * @param pFSPrimaryRight
     * @param pFileName
     * @param pOwnerXML
     * @param pClassificationXML
     * @return
     */
    private String generateResponseDetailsXML(int pFSPrimaryRight, String pXMLARADispMsg, String pFileName,
            String pOwnerXML, String pClassificationXML) {

        String lstrXMLWaterMark = "";
        if (pFSPrimaryRight != 0) {
            lstrXMLWaterMark = "<ara-watermark-details>" + "<lines>" + "<line>This document is '$FILECLASS$'</line>"
                    + "<line>User- $USERNAME$</line>" + "<line>File- " + XMLHelper.escapeForXML(pFileName)
                    + "($FILEID$)</line>" + "<line>$VIEWTIME$</line>" + "</lines>" + "</ara-watermark-details>";
        }
        //Construct response details xml = "";
        String respDetailsXML =
                "<ara-response-details-get-access-right>" + "<ara-access-right-details>" + "<primary-access-right>"
                        + pFSPrimaryRight + "</primary-access-right>" +
                        /*"<offline-access-right>false</offline-access-right>"+*/
                        "</ara-access-right-details>" +

                        //ARA-Display-Message if there is no right
                        pXMLARADispMsg +

                        // Water mark XML
                        lstrXMLWaterMark +

                        // Add classification and owner XML
                        pClassificationXML + pOwnerXML + "</ara-response-details-get-access-right> ";
        return respDetailsXML;
    }

    /**
     * Map application rights to Seclore FileSecure right
     *
     * @param rights
     * @return
     */
    private int mapWithFileSecureRights(int rights) {
        if (rights == 0) {
            return 0;
        }

        // Read
        else if (rights == 1) {
            // View | Light View | Copy Data | Screen Capture
            return (2 | 6 | 258 | 514);
        }

        // Read + Change
        else if (rights == 2) {
            // View | Light View | Edit | Copy Data | Screen Capture
            return (2 | 6 | 34 | 258 | 514);
        }

        // Read + Print
        else if (rights == 3) {
            // View | Light View | Print | Copy Data | Screen Capture
            return (2 | 6 | 10 | 258 | 514);
        }

        // Read + Change + Print
        else if (rights == 4) {
            // View | Light View | Print | Edit | Copy Data | Screen Capture
            return (2 | 6 | 10 | 34 | 258 | 514);
        }

        // Full Control
        else if (rights == 5) {
            // View|Lite Viewer|Print|Edit|Full Control|Copy Data|Screen Capture| Macro
            return (2 | 6 | 10 | 34 | 170 | 258 | 514 | 1026);
        }
        return 0;

    }

    private String prepareOwnerXML(String pRequestId, Owner lOwner, ARAOwnerDetails ownereDetails) throws Exception {
        String lstrMethodName = this.getClass().getName() + "::prepareOwnerXML::";
        logger.debug(lstrMethodName + "[" + pRequestId + "]:" + "START");
        if (lOwner != null) {
            String emailXML = "";
            String lstrEmailId = lOwner.getEmailId();
            if (lstrEmailId != null && !lstrEmailId.trim().isEmpty()) {
                emailXML = "<email-id>" + XMLHelper.escapeForXML(lstrEmailId) + "</email-id>";
            }

            // if email-id is available
            if (!emailXML.isEmpty()) {
                String lstrXML =
                        "<ara-owner-details><ara-user-details>" + emailXML + "</ara-user-details></ara-owner-details>";
                LoggerUtil.logInfo(
                        lstrMethodName + "[" + pRequestId + "]:" + "File Owner details return by application: "
                                + lstrXML);
                logger.debug(lstrMethodName + "[" + pRequestId + "]:" + "END");
                return lstrXML;
            }
        }

        // Default Owner
        String lstrXML = XMLHelper.generatOwnerDetailsXML(ownereDetails);
        LoggerUtil.logInfo(lstrMethodName + "[" + pRequestId + "]:"
                + "Same file owner (received in request) is returned by application: " + lstrXML);
        logger.debug(lstrMethodName + "[" + pRequestId + "]:" + "END");
        return lstrXML;
    }

    private String prepareClassificationXML(String pRequestId, Classification lClassification,
            ARAClassificationDetails classificationDetails) throws Exception {
        String lstrMethodName = this.getClass().getName() + "::prepareClassificationXML::";
        logger.debug(lstrMethodName + "[" + pRequestId + "]:" + "START");
        if (lClassification != null) {
            String lstrClsfin = lClassification.getId();
            if (lstrClsfin != null && !lstrClsfin.trim().isEmpty()) {
                LoggerUtil.logInfo(lstrMethodName + "[" + pRequestId + "]:" + "Classification return by application: "
                        + lClassification.getName() + "(" + lstrClsfin + ")");
                String lstrXML = "<ara-classification-details><fs-id>" + lstrClsfin + "</fs-id><name>" + lClassification
                        .getName() + "</name></ara-classification-details>";
                logger.debug(lstrMethodName + "[" + pRequestId + "]:" + "END");
                return lstrXML;
            }
        }

        // Default Classification
        String lstrXML = XMLHelper.generatClassificationDetailsXML(classificationDetails);
        LoggerUtil.logInfo(lstrMethodName + "[" + pRequestId + "]:"
                + "Same classification (received in request) is return by application: " + lstrXML);
        logger.debug(lstrMethodName + "[" + pRequestId + "]:" + "END");
        return lstrXML;
    }
}
