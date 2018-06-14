package com.seclore.sample.dms.user.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seclore.fs.helper.library.FSHelperLibrary;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.XMLUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@WebServlet({ "/searchUser.do" }) public class SearchUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoggerUtil.logDebug("SearchUserServlet:doPost::START");
        String lstrEmailId = request.getParameter("emailId");
        String lstrName = null;
        if ((lstrEmailId == null) || (lstrEmailId.trim().isEmpty())) {
            writeErrorResponse(response, -1, "Please enter the user's email id");
            return;
        }
        lstrEmailId = lstrEmailId.trim();

        String lstrSearchEntityXML =
                "<request><request-header><protocol-version>2</protocol-version></request-header><request-details><email-id>"
                        + XMLUtil.escapeForXML(lstrEmailId) + "</email-id></request-details></request>";
        try {
            String responseXML = FSHelperLibrary.sendRequest(null, 74, lstrSearchEntityXML);
            LoggerUtil.logError("SearchUserServlet:doPost::Response XML:- " + responseXML);
            Node rootNode = XMLUtil.getRootNode(responseXML);
            Node requestStatusNode = XMLUtil.parseNode("request-status", rootNode);
            String returnValue = XMLUtil.parseString("return-value", requestStatusNode);
            if (!"1".equals(returnValue)) {
                if ("-220372".equals(returnValue)) {
                    writeErrorResponse(response, -1, "User with this email id does not exist");
                    return;
                }
                String errorMessage = XMLUtil.parseString("error-message", requestStatusNode);
                String displayMessage = XMLUtil.parseString("display-message", requestStatusNode);
                if ((displayMessage == null) || (displayMessage.trim().isEmpty())) {
                    displayMessage = errorMessage;
                }
                writeErrorResponse(response, -1, displayMessage);
                return;
            }
            Node entitiesNode = XMLUtil.parseNode("entities", rootNode);
            if (entitiesNode == null) {
                writeErrorResponse(response, -1, "User with this email id does not exist");
                return;
            }
            NodeList entitiesList = XMLUtil.parseNodeList("entity", entitiesNode);
            if ((entitiesList == null) || (entitiesList.getLength() == 0)) {
                writeErrorResponse(response, -1, "User with this email id does not exist");
                return;
            }

            Node entity = entitiesList.item(0);
            lstrName = XMLUtil.parseString("name", entity);
            if (lstrName == null) {
                lstrName = lstrEmailId;
            }

            if (XMLDBService.getAppUserById(lstrEmailId) == null) {
                XMLDBService.addNewAppUser(lstrEmailId, lstrName);
            }
            writeSuccessResponse(response, lstrEmailId, lstrName);
            LoggerUtil.logDebug("SearchUserServlet:doPost::END");
            return;
        } catch (Exception e) {
            LoggerUtil.logError("SearchUserServlet:doPost::Error while searching the entity", e);
            writeErrorResponse(response, -1, e.getMessage());
            LoggerUtil.logDebug("SearchUserServlet:doPost::END");
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int pStatus, String pErrorMsg) {
        LoggerUtil.logDebug("SearchUserServlet:doPost:writeErrorResponse::START");
        try {
            ServletOutputStream out = response.getOutputStream();
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", Integer.valueOf(pStatus));
            jsonObject.addProperty("errorMessage", pErrorMsg);
            out.print(gson.toJson(jsonObject));
            out.flush();
        } catch (IOException localIOException) {
        }
        LoggerUtil.logDebug("SearchUserServlet:doPost:writeErrorResponse::END");
    }

    private void writeSuccessResponse(HttpServletResponse response, String pEmailId, String pName) {
        LoggerUtil.logDebug("SearchUserServlet:doPost:writeSuccessResponse::START");
        try {
            ServletOutputStream out = response.getOutputStream();
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", Integer.valueOf(1));
            jsonObject.addProperty("name", pName);
            jsonObject.addProperty("emailId", pEmailId);
            out.print(gson.toJson(jsonObject));
            out.flush();
        } catch (IOException localIOException) {
        }
        LoggerUtil.logDebug("SearchUserServlet:doPost:writeSuccessResponse::END");
    }
}
