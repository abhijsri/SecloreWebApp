package com.seclore.sample.webconnect.servlet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seclore.fs.helper.exception.FSHelperException;
import com.seclore.fs.helper.library.FSHelperLibrary;
import com.seclore.fs.ws.client.pscp.PSConnection;
import com.seclore.sample.dms.config.HotFolderConfig;
import com.seclore.sample.dms.config.SecloreWSClientConfig;
import com.seclore.sample.dms.constant.Constants;
import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.util.Global;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.SecloreProtectUtil;
import com.seclore.sample.dms.util.XMLUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * This servlet handle request to view the file using Seclore WebConnect Integration.
 *
 * @author harindra.chaudhary
 */
@WebServlet(name = "viewFileOnlineServlet", urlPatterns = { "/viewFileOnline.do" }) public class ViewFileOnlineServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String TEMP_FOLDER_PATH = Global.getAppRealPath() + File.separator + "SECLORE_TEMP_FOLDER";

    /**
     * Default constructor
     */
    public ViewFileOnlineServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoggerUtil.logDebug("ViewFileOnlineServlet::doPost::START");
        HttpSession session = request.getSession();

        String fileId = request.getParameter("fileId");
        String folderId = request.getParameter("folderId");

        // ======== File and Folder Validations - START ======== //
        if (folderId == null || folderId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        AppFolder lAppFolder = XMLDBService.getFolder(folderId);
        if (lAppFolder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        if (fileId == null || fileId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "The file you were looking for could not be found!");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        }

        AppFile lAppFile = XMLDBService.getFile(folderId, fileId);
        if (lAppFile == null) {
            session.setAttribute("ERROR_MESSAGE", "The file you were looking for could not be found!");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        }
        // ======== File and Folder Validations - END ======== //

        try {
            // ==== Seclore WebConnect Integration - START ==== //

            // Steps:-
            // 	- If file is supported or not
            //	- First check if file is already uploaded or not
            //	- If not already uploaded then first upload the file using Enterprise Application user (Cabinet User) login context.
            //	- If already uploaded then no need to upload again
            //  - Redirect to Policy Server view file page

            boolean isSupportedFile = FSHelperLibrary.isSupportedFile(lAppFile.getName());
            boolean isWebConnectSupported = SecloreProtectUtil.isWebConnectSupported(lAppFile.getName());
            // If folder is Seclore IRM Enabled and file is supported by Seclore WSClient
            if (!lAppFolder.getIrmEnabled() || !isWebConnectSupported || !isSupportedFile) {
                LoggerUtil.logInfo(
                        "ViewFileOnlineServlet:: File not suported- (Is IRM Enabled:" + lAppFolder.getIrmEnabled()
                                + ", Is WebConnect Supported:" + isWebConnectSupported + ", Is WSClient Supported:"
                                + isSupportedFile + ")");
                session.setAttribute("ERROR_MESSAGE", "The file is not supported to view using Seclore WebConnect");
                response.sendRedirect("fileList.do?folderId=" + folderId);
                return;
            }

            String fileDetailsXML = "<request>" + "<request-header>" + "<request-type>83</request-type>"
                    + "<protocol-version>2</protocol-version>" + "</request-header>" + "<request-details>"
                    + "<file-ref-id>" + XMLUtil.escapeForXML(lAppFile.getId()) + "</file-ref-id>" + "</request-details>"
                    + "</request>";

            // Get uploaded file details if already uploaded
            String lstrXMLResponse = FSHelperLibrary.sendRequest(null, 83, fileDetailsXML);
            LoggerUtil.logDebug("ViewFileOnlineServlet:: Uploaded File Information XML: " + lstrXMLResponse);

            Node rootNode = XMLUtil.getRootNode(lstrXMLResponse);
            Node requestStatusNode = XMLUtil.parseNode("request-status", rootNode);
            String returnValue = XMLUtil.parseString("return-value", requestStatusNode);

            // Get configured Policy Server Url
            PSConnection psConnection = FSHelperLibrary.getPSConnection();
            String lstrPSUrl = psConnection.getURL();
            FSHelperLibrary.releasePSConnection(psConnection);

            // if return value is  1 means file is already uploaded
            if ("1".equals(returnValue)) {
                LoggerUtil.logInfo("ViewFileOnlineServlet:: File '" + lAppFile.getName() + "(" + lAppFile.getId()
                        + ")' already uploaded, Opening the file for user '" + session.getAttribute("name") + "'");
                session.setAttribute("PS_VIEW_FILE_URL", lstrPSUrl + "/viewfile?file_ref_id=" + lAppFile.getId());
                response.sendRedirect("fileList.do?folderId=" + folderId);
                return;
            }

            String fileName = lAppFile.getName();
            String originalFilePath = Global.getAppDataDir() + File.separator + folderId + File.separator + fileName;
            File lfile = new File(originalFilePath);
            if (!lfile.exists()) {
                session.setAttribute("ERROR_MESSAGE", "The file you were looking for could not be found!");
                response.sendRedirect("fileList.do?folderId=" + folderId);
                return;
            }

            // Upload the file on WebConnect
            LoggerUtil.logInfo(
                    "ViewFileOnlineServlet:: Uploading the file '" + lAppFile.getName() + "(" + lAppFile.getId()
                            + ")' for user '" + session.getAttribute("name") + "'");
            String responseXML = uploadFileOnPSWebConnect(lAppFile, lAppFolder);

            if (responseXML == null || responseXML.trim().isEmpty()) {
                LoggerUtil.logError("ViewFileOnlineServlet:: Error occured while sending the file to Web Viewer");
                session.setAttribute("ERROR_MESSAGE", "Error occured while sending the file to Web Viewer");
                response.sendRedirect("fileList.do?folderId=" + folderId);
                return;
            }

            rootNode = XMLUtil.getRootNode(responseXML);
            requestStatusNode = XMLUtil.parseNode("request-status", rootNode);
            returnValue = XMLUtil.parseString("return-value", requestStatusNode);

            if ("1".equals(returnValue)) {
                LoggerUtil.logInfo(
                        "ViewFileOnlineServlet::Opening the file for user '" + session.getAttribute("name") + "'");
                session.setAttribute("PS_VIEW_FILE_URL", lstrPSUrl + "/viewfile?file_ref_id=" + lAppFile.getId());
                response.sendRedirect("fileList.do?folderId=" + folderId);
                return;
            }

            String errorMessage = XMLUtil.parseString("error-message", requestStatusNode);
            String displayMessage = XMLUtil.parseString("display-message", requestStatusNode);

            LoggerUtil.logError("ViewFileOnlineServlet:: WebConnect File Upload: " + errorMessage);
            session.setAttribute("ERROR_MESSAGE", displayMessage);

            // ==== Seclore WebConnect Integration - END ==== //

        } catch (Exception e) {
            LoggerUtil.logError(e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", e.getMessage());
        }

        LoggerUtil.logDebug("ViewFileOnlineServlet::doPost::END");
        response.sendRedirect("fileList.do?folderId=" + folderId);
        return;

    }

    /**
     * Protect the file and upload to Policy Server WebConnect
     *
     * @param pPSUrl
     * @param pAppFile
     * @param pAppFolder
     * @throws Exception
     */
    private String uploadFileOnPSWebConnect(AppFile pAppFile, AppFolder pAppFolder) throws Exception {

        String fileName = pAppFile.getName();

        // ****** WRITE TEMP FILE ****** //
        // Create Temporary folder if not exist
        createTempFolder(TEMP_FOLDER_PATH);
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        String tempFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + new Date().getTime() + fileExt;
        String tempFilePath = TEMP_FOLDER_PATH + File.separator + tempFileName;
        String originalFilePath =
                Global.getAppDataDir() + File.separator + pAppFolder.getId() + File.separator + fileName;
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(originalFilePath);
            SecloreProtectUtil.writeTempFile(fileInputStream, tempFilePath);
        } finally {
            try {
                fileInputStream.close();
                fileInputStream = null;
            } catch (IOException ioExp) {
            }
        }

        Gson lGson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.KEY_HF_EXTN_REF_ID, pAppFolder.getId());
        String lstrExtnRefData = lGson.toJson(jsonObject);
        String lstrExtnHFid = HotFolderConfig.getExternalHotFolderId();

        // === Construct Protection details XML
        String protectionDetailsXML = "<hot-folder-extn-reference>" + "<extn-reference>" +
                //"<extn-ref-id>"+XMLUtil.escapeForXML(pAppFolder.getId()) +"</extn-ref-id>"+
                //"<extn-ref-name>"+XMLUtil.escapeForXML(pAppFolder.getName())+"</extn-ref-name>"+
                "<extn-ref-id>" + XMLUtil.escapeForXML(lstrExtnHFid) + "</extn-ref-id>" + "<extn-ref-name>" + XMLUtil
                .escapeForXML("") + "</extn-ref-name>" + "<extn-ref-data>" + XMLUtil.escapeForXML("")
                + "</extn-ref-data>" + "<extn-app-id>" + XMLUtil.escapeForXML("") + "</extn-app-id>"
                + "</extn-reference>" + "</hot-folder-extn-reference>" +

                "<file-extn-reference>" + "<extn-reference>" + "<extn-ref-id>" + XMLUtil.escapeForXML(pAppFile.getId())
                + "</extn-ref-id>" + "<extn-ref-name>" + XMLUtil.escapeForXML(pAppFile.getName()) + "</extn-ref-name>"
                + "<extn-ref-data>" + XMLUtil.escapeForXML(lstrExtnRefData) + "</extn-ref-data>" + "<extn-app-id>"
                + XMLUtil.escapeForXML("") + "</extn-app-id>" + "</extn-reference>" + "</file-extn-reference>";

        // ****** Protect TEMP File and Get protected content  ****** //
        String displayFileName = pAppFile.getName();
        fileInputStream = SecloreProtectUtil
                .protectFile(tempFilePath, displayFileName, protectionDetailsXML, pAppFolder.getHtmlWrapEnabled());

        // ****** Protection END ******** //

        // ******* Upload Protected content on PS Web Connect ****** //
        String responseXML = sendUploadRequestByHttpConnection(fileInputStream, pAppFile,
                pAppFolder.getHtmlWrapEnabled());
        LoggerUtil.logDebug("WebConnect Upload Responxe XML: " + responseXML);
        return responseXML;
    }

    private String sendUploadRequestByHttpConnection(InputStream pInputStream, AppFile pAppFile,
            boolean pHtmlWrapEnabled) throws IOException, FSHelperException {
        HttpsURLConnection urlConnection = null;
        OutputStream connOutputStrem = null;

        // Get configured Policy Server Url
        PSConnection psConnection = FSHelperLibrary.getPSConnection();
        String lstrPSUrl = psConnection.getURL();
        String lstrJSESSIONID = psConnection.getSessionId();

        try {
            URL url = new URL(lstrPSUrl + "/uploadtoview.do");

            Proxy proxy = null;
            String proxyServerName = SecloreWSClientConfig.getProxyServerName();
            int proxyServerPort = SecloreWSClientConfig.getProxyServerPort();

            if (proxyServerName != null && !proxyServerName.trim().isEmpty() && proxyServerPort != -1) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyServerName, proxyServerPort));
            }

            if (proxy != null) {
                urlConnection = (HttpsURLConnection) url.openConnection(proxy);
            } else {
                urlConnection = (HttpsURLConnection) url.openConnection();
            }

            String MULTIPART_BOUNDARY = "------------------563i2ndDfv2rTHiSsdfsdbouNdArYfORhxcvxcvefj3q2f";
            String EOL = "\r\n";

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + MULTIPART_BOUNDARY);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.addRequestProperty("Cookie", "JSESSIONID=" + lstrJSESSIONID);

            connOutputStrem = new BufferedOutputStream(urlConnection.getOutputStream());

            // ----- Write ----- psp_file_display_name ----- START //
            connOutputStrem.write(("--" + MULTIPART_BOUNDARY + EOL).getBytes());
            connOutputStrem.write(("Content-Disposition: form-data; name=\"psp_file_display_name\"" + EOL).getBytes());
            connOutputStrem.write(("Content-Type: text/plain; charset=UTF-8" + EOL).getBytes());
            connOutputStrem.write(("Content-Length: " + pAppFile.getName().length() + EOL).getBytes());
            connOutputStrem.write((EOL).getBytes());
            connOutputStrem.write((pAppFile.getName() + EOL).getBytes());
            // ----- Write psp_file_display_name ----- END //

            // ----- Write psp_ extn_ref_id ----- Start
            connOutputStrem.write(("--" + MULTIPART_BOUNDARY + EOL).getBytes());
            connOutputStrem.write(("Content-Disposition: form-data; name=\"psp_extn_ref_id\"" + EOL).getBytes());
            connOutputStrem.write(("Content-Type: text/plain; charset=UTF-8" + EOL).getBytes());
            connOutputStrem.write(("Content-Length: " + pAppFile.getId().length() + EOL).getBytes());
            connOutputStrem.write((EOL).getBytes());
            connOutputStrem.write((pAppFile.getId() + EOL).getBytes());
            // ----- Write psp_ extn_ref_id ----- END //

            // ----- Write psp_file_content ----- START //
            connOutputStrem.write(("--" + MULTIPART_BOUNDARY + EOL).getBytes());
            connOutputStrem
                    .write(("Content-Disposition:form-data;name=\"psp_file_content\";filename=\"" + pAppFile.getName()
                            + (pHtmlWrapEnabled == true ? ".html" : "") + "\"\r\nContent-Type: " + pAppFile
                            .getContentType() + EOL + EOL).getBytes());
            byte lByteArray[] = new byte[8021];

            while (pInputStream.available() > 0) {
                connOutputStrem.write(pInputStream.read());
                int len = pInputStream.read(lByteArray);
                connOutputStrem.write(lByteArray, 0, len);
                try {
                    connOutputStrem.flush();
                } catch (IOException e) {
                }
            }

            connOutputStrem.write((EOL + "--" + MULTIPART_BOUNDARY + "--" + EOL + EOL).getBytes());

            // Close input and output stream
            try {
                connOutputStrem.flush();
            } catch (IOException e) {
            }
            try {
                connOutputStrem.close();
            } catch (IOException e) {
            }

            try {
                pInputStream.close();
            } catch (IOException e) {
            }

            // ----- Write psp_file_content ----- END //

            // Connect
            try {
                urlConnection.connect();
            } catch (IOException exception) {
            }

            if (urlConnection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                InputStream errorStream = urlConnection.getErrorStream();
                String errorString = inputStreamToString(errorStream);
                LoggerUtil.logError("Error While sending the file" + errorString);
                return "";
            }

            InputStream inputStream = urlConnection.getInputStream();
            return inputStreamToString(inputStream);

        } finally {
            if (connOutputStrem != null) {
                try {
                    connOutputStrem.close();
                    connOutputStrem = null;
                } catch (IOException e) {
                }
            }

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            FSHelperLibrary.releasePSConnection(psConnection);
        }

    }

    /**
     * Convert InputStream to String
     *
     * @param pInputStream
     * @return
     * @throws IOException
     */
    private String inputStreamToString(InputStream pInputStream) throws IOException {
        if (pInputStream != null) {
            StringBuffer responseStr = new StringBuffer();
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(pInputStream, Charset.forName("UTF-16LE")));
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    responseStr.append(inputLine);
                }
                return responseStr.toString().trim();
            } finally {
                try {
                    pInputStream.close();
                } catch (IOException ioException) {
                }
            }
        }
        return null;
    }

    /**
     * Create Temporary Folder if not exist
     *
     * @param pTempFolderPath
     */
    private void createTempFolder(String pTempFolderPath) {
        File tempFolder = new File(pTempFolderPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
    }
}
