package com.seclore.sample.dms.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.seclore.fs.helper.core.ProtectedFile;
import com.seclore.fs.helper.enums.ProtectionType;
import com.seclore.fs.helper.exception.FSHelperException;
import com.seclore.fs.helper.library.FSHelperLibrary;
import com.seclore.fs.ws.client.core.RequestType;
import com.seclore.fs.ws.client.pscp.PSConnection;
import com.seclore.fs.ws.client.pscp.exception.PSCPException;
import com.seclore.sample.dms.core.Classification;
import com.seclore.sample.dms.util.xml.XMLDBService;

public class SecloreProtectUtil {
    private static Set<String> sWebConnectExtList = new HashSet<String>(Arrays.asList(
            "PDF,DOC,DOCX,XLS,XLSX,PPT,PPTX,TXT,RTF,CSV,ODT,ODS,ODP,PNG,BMP,JPEG,JPG,GIF,TIFF,TIF".split(",")));

    /**
     * @param pTempFilePath        - Absolute File path of file to be protected
     * @param displayFileName      - Display name of the file which will be goes in activity log.
     * @param protectionDetailsXml - Protection details
     * @return InputStream of Protected file
     * @throws IOException
     * @throws PSCPException
     */
    public static InputStream protectFile(String pTempFilePath, String displayFileName, String protectionDetailsXml,
            boolean htmlWrapEnabled) throws Exception {
        // Read protected file data in byte stream and return it
        ByteArrayInputStream lByteInputStream = null;
        FileInputStream lInputStream = null;
        File lFile = null;
        ProtectedFile lWrapperProtectedFile = null;
        File lWrapperFile = null;
        File lFinalFile = null;
        try {
            lFile = new File(pTempFilePath);

            // If file is not already protected, then protect the file
            if (FSHelperLibrary.isProtectedFile(pTempFilePath) == false) {
                // Calling the protect method to protect a file.
                PSConnection psConnection = null;
                String protectorDetails = null;
                String comments = null;
                if (displayFileName == null || displayFileName.trim().isEmpty()) {
                    displayFileName = pTempFilePath;
                }
                // if PSConnection is passed as null then Seclore Protection Library create and destroy PSConnection itself
                String lstrFileId = FSHelperLibrary
                        .protectX(psConnection, pTempFilePath, displayFileName, ProtectionType.PROTECT_WITH_HF_EXT_REF,
                                protectionDetailsXml, protectorDetails, comments);
                FSHelperLibrary.logInfo("File protected with File Id: " + lstrFileId);
                lFinalFile = lFile;
                if (htmlWrapEnabled == true && FSHelperLibrary.isHTMLWrapped(lFile.getPath()) == false) {
                    lWrapperProtectedFile = FSHelperLibrary.wrap(null, lFile.getPath(), displayFileName);
                    lWrapperFile = new File(lWrapperProtectedFile.getFilePath());
                    lFinalFile = lWrapperFile;
                }
            }
            lInputStream = new FileInputStream(lFinalFile);

            //read file contents and storing in byte array
            byte[] lArrFileData = new byte[(int) lFinalFile.length()];
            lInputStream.read(lArrFileData);

            //create ByteStream from byte array
            lByteInputStream = new ByteArrayInputStream(lArrFileData);
        } catch (PSCPException connectionExp) {
            FSHelperLibrary.logError(connectionExp.getMessage(), connectionExp);
            throw connectionExp;
        } catch (Exception lEx) {
            FSHelperLibrary.logError(lEx.getMessage(), lEx);
            throw lEx;
        } finally {
            //close fileinputstream if not null
            if (lInputStream != null) {
                try {
                    lInputStream.close();
                } catch (IOException lEx) {
                }
            }

            //delete file if not null
            if (lFile != null) {
                lFile.delete();
            }

            //delete Wrapper file if not null
            if (lWrapperFile != null) {
                lWrapperFile.delete();
            }
        }

        //return protected file contents as byte array input stream
        return lByteInputStream;
    }

    /**
     * Write a temporary file to be protected
     *
     * @param pInputStream  - content of the file
     * @param pTempFilePath - Temporary file path
     * @throws IOException
     */
    public static void writeTempFile(InputStream pInputStream, String pTempFilePath) throws IOException {
        FileOutputStream lOutputStream = null;
        try {
            lOutputStream = new FileOutputStream(pTempFilePath);

            byte[] bufferData = new byte[1024];
            int read = 0;

            //read employee data from byte stream
            while ((read = pInputStream.read(bufferData)) != -1) {
                //write file data in temp File output stream
                lOutputStream.write(bufferData, 0, read);
            }

            try {
                lOutputStream.flush();
            } catch (IOException e) {
            }
        } finally {
            //close output stream
            if (lOutputStream != null) {
                try {
                    lOutputStream.close();
                } catch (Exception lEx) {
                }
            }
        }
    }

    public static boolean isWebConnectSupported(String pFileName) {
        if (pFileName == null || pFileName.trim().isEmpty()) {
            return false;
        }

        int extIndex = pFileName.lastIndexOf(".");
        if (extIndex < 0) {
            return false;
        }

        String ext = pFileName.substring(extIndex + 1);
        //PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX, TXT, RTF, CSV, ODT, ODS, ODP, PNG, BMP, JPEG, JPG, GIF, TIFF, TIF
        if (sWebConnectExtList.contains(ext.toUpperCase())) {
            return true;
        }

        return false;
    }

    /**
     * Get classifications from Policy server.
     * This method also save classifications locally.
     *
     * @return
     * @throws Exception
     */
    public static Set<Classification> getClassifications() throws Exception {
        String xmlRequest = "<request><request-header><protocol-version>2</protocol-version></request-header></request>";
        try {
            String responseXML = FSHelperLibrary.sendRequest(null, RequestType.RT_GET_CLASSIFICATIONS, xmlRequest);
            LoggerUtil.logDebug("Get Classificatios Response XML: " + responseXML);

            Node rootNode = XMLUtil.getRootNode(responseXML);
            Node requestStatusNode = XMLUtil.parseNode("request-status", rootNode);
            String returnValue = XMLUtil.parseString("return-value", requestStatusNode);

            if (!"1".equals(returnValue)) {
                LoggerUtil.logError("Get Classificatios Response XML: " + responseXML);
                String errorMsg = XMLUtil.parseString("error-message", requestStatusNode);
                String dispMsg = XMLUtil.parseString("display-message", requestStatusNode);
                if (dispMsg == null || dispMsg.trim().isEmpty()) {
                    dispMsg = errorMsg;
                }
                throw new Exception(dispMsg);
            }

            NodeList lNodeList = XMLUtil.parseNodeList("classifications/classification", rootNode);
            if (lNodeList != null && lNodeList.getLength() > 0) {
                Set<Classification> lClassifications = new LinkedHashSet<Classification>();
                for (int i = 0; i < lNodeList.getLength(); i++) {
                    Node lNode = lNodeList.item(i);
                    String lstrClassId = XMLUtil.parseString("id", lNode);
                    String lstrClassName = XMLUtil.parseString("name", lNode);
                    String lstrClassDesc = XMLUtil.parseString("description", lNode);
                    String lstrStatus = XMLUtil.parseString("status", lNode);
                    // Take only active classification
                    if ("1".equals(lstrStatus)) {
                        Classification lClassification = new Classification();
                        lClassification.setId(lstrClassId);
                        lClassification.setName(lstrClassName);
                        lClassification.setDescription(lstrClassDesc);
                        lClassifications.add(lClassification);
                    }
                }
                XMLDBService.updateClassifications(lClassifications);
                return lClassifications;
            }
        } catch (FSHelperException e) {
            throw e;
        }
        return null;
    }
}
