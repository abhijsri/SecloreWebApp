package com.seclore.sample.dms.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.core.Classification;
import com.seclore.sample.dms.core.FileActivityLog;
import com.seclore.sample.dms.core.UserRight;
import com.seclore.sample.dms.util.xml.XMLDBService;

public class PortalUtil {
    /**
     * @param usersRight Set<UserRight>
     * @return Example :- "{\"usersRights\":[{\"userId\":\"2\",\"usageRights\":\"1\"},{\"userId\":\"1\",\"usageRights\":\"2\"}]}";
     */
    public static String getJsonString(Set<UserRight> usersRight) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("usersRights", gson.toJsonTree(usersRight));
        return gson.toJson(jsonObject);
    }

    public static void setRequestAttributes(HttpServletRequest pRequest, AppFile pFile, AppFolder pFolder) {
        // Get the classifications from Policy Server
        Set<Classification> lClassifications = null;
        try {
            lClassifications = SecloreProtectUtil.getClassifications();
        } catch (Exception e) {
            LoggerUtil
                    .logError("Error while getting classification from Policy Server, Taking classification from local",
                            e);
            // Get the classifications from local (cached)
            lClassifications = XMLDBService.getClassifications();
        }

        pRequest.setAttribute("selectedUsersRights", pFile.getUserRightList());
        pRequest.setAttribute("rightsMap", XMLDBService.getAppRights());
        pRequest.setAttribute("usersMap", XMLDBService.getAppUsers());
        pRequest.setAttribute("folderName", pFolder.getName());
        pRequest.setAttribute("folderId", pFolder.getId());
        pRequest.setAttribute("file", pFile);
        pRequest.setAttribute("classifications", lClassifications);
    }

    public static void setRequestAttributes(HttpServletRequest pRequest, List<FileActivityLog> lliFileActivityLog) {
        pRequest.setAttribute("fileactivitylog", lliFileActivityLog);
    }

    /**
     * This method encodes the url value
     *
     * @param Value
     * @return
     */

    public static String getEncodedParamValue(String pParamValue) {
        try {
            String lstrRetVal = URLEncoder.encode(pParamValue, "UTF-8");
            return lstrRetVal;
        } catch (UnsupportedEncodingException e) {
            return pParamValue;
        }
    }

    public static String getApplicationUrl(HttpServletRequest request) {

        String applicationUrl = "";
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        Integer serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        if (serverPort == 80 || serverPort == 443) {
            applicationUrl = scheme + "://" + serverName + contextPath;
        } else {
            applicationUrl = scheme + "://" + serverName + ":" + serverPort + contextPath;
        }

        return applicationUrl;
    }
}
