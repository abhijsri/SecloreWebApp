package com.seclore.sample.dms.servlet.folder;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Servlet implementation class SaveFolderServlet
 */
@WebServlet(name = "saveFolderServlet", urlPatterns = { "/saveFolder.do" }) public class SaveFolderServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveFolderServlet() {
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
        LoggerUtil.logDebug("SaveFolderServlet::doPost::START");

        HttpSession session = request.getSession();

        AppFolder lFolder = new AppFolder();
        if (!validateRequest(lFolder, request)) {
            request.setAttribute("folder", lFolder);
            request.getRequestDispatcher("/portal/pages/folder.jsp").forward(request, response);
            return;
        }

        // Create New folder
        if (lFolder.getId() == null || lFolder.getId().isEmpty()) {
            if (!XMLDBService.createNewFolder(lFolder)) {
                request.setAttribute("folder", lFolder);
                request.setAttribute("ERROR_MESSAGE", "Folder creation faild, Please try again!");
                request.getRequestDispatcher("/portal/pages/folder.jsp").forward(request, response);
                return;
            }

            session.setAttribute("SUCCESS_MESSAGE", "Folder has been created successfully!");

            // ********** Map Seclore policy if IRM Enabled ********* //
            /* Commented below code - Not required in Fully federated Cabinet */
			/*
			if( lFolder.getIrmEnabled() )
			{
				boolean isMapped = mapSeclorePolicy( lFolder );
				if( !isMapped )
				{
					session.setAttribute("SUCCESS_MESSAGE", "Folder has been created successfully, But faild to map with Seclore Policy");
					lFolder.setIrmEnabled(false);
					XMLDBService.updateFolder(lFolder);
				}
			}
			
			LoggerUtil.logInfo("User '"+session.getAttribute("name")+"' created the folder '"+lFolder.getName()+"'");
			*/
            // **************************************************** //

        }
        // Update existing folder
        else {
            AppFolder existingFolder = XMLDBService.getFolder(lFolder.getId());

            if (existingFolder == null) {
                session.setAttribute("ERROR_MESSAGE", "The folder you were updating, could not be found!");
                response.sendRedirect("folderList.do");
                return;
            }

            if (!XMLDBService.updateFolder(lFolder)) {
                request.setAttribute("folder", lFolder);
                request.setAttribute("ERROR_MESSAGE", "Folder updation faild, Please try again!");
                request.getRequestDispatcher("/portal/pages/folder.jsp").forward(request, response);
                return;
            }
            session.setAttribute("SUCCESS_MESSAGE", "Folder has been updated successfully!");

            // ********** map Seclore policy if IRM Enabled ********* //
            /* Commented below code - Not required in Fully federated Cabinet */
            // if already not mapped with seclore policy
			/*
			boolean isAlreadyIRMEnabled = existingFolder.getIrmEnabled();
			if( lFolder.getIrmEnabled() && !isAlreadyIRMEnabled )
			{
				boolean isMapped = mapSeclorePolicy( lFolder );
				if( !isMapped )
				{
					session.setAttribute("SUCCESS_MESSAGE", "Folder has been updated successfully, But failed to map with Seclore Policy");
					lFolder.setIrmEnabled(false);
					XMLDBService.updateFolder( lFolder );
				}
			}
			
			LoggerUtil.logInfo("User '"+session.getAttribute("name")+"' updated the folder '"+lFolder.getName()+"'");
			*/
            // **********************************************//

        }

        LoggerUtil.logDebug("SaveFolderServlet::doPost::END");
        response.sendRedirect("folderList.do");
        return;

    }

    private boolean validateRequest(AppFolder folder, HttpServletRequest request) {
        String folderName = request.getParameter("name");
        String folderId = request.getParameter("id");
        String irmEnabled = request.getParameter("irmEnabled");
        Boolean lbIrmEnabled = false;
        String htmlWrapEnabled = request.getParameter("htmlWrapEnabled");
        Boolean lbHtmlWrapEnabled = false;

        if ("true".equalsIgnoreCase(irmEnabled)) {
            lbIrmEnabled = true;
        }

        if ("true".equalsIgnoreCase(htmlWrapEnabled)) {
            lbHtmlWrapEnabled = true;
        }

        boolean flag = true;
        folder.setIrmEnabled(lbIrmEnabled);
        folder.setHtmlWrapEnabled(lbHtmlWrapEnabled);

        if (folderName == null || folderName.trim().isEmpty()) {
            request.setAttribute("ERROR_MESSAGE", "Please provide folder name!");
            flag = false;
        } else {
            folder.setName(folderName.trim());
        }

        // Just trim folder id if there
        if (folderId != null && !folderId.trim().isEmpty()) {
            folder.setId(folderId.trim());
        }

        return flag;
    }


    /* Commented below code - Not required in Fully federated Cabinet */
	/*
	private boolean mapSeclorePolicy(AppFolder folder)
	{
		LoggerUtil.logDebug("SaveFolderServlet::mapSeclorePolicy::START");
		if ( !FSHelperLibrary.isInitialized() )
		{
			LoggerUtil.logError("SaveFolderServlet::mapSeclorePolicy::Failed to map policy on folder '"+folder.getName()+"' because WS Client was not initialized.");
			return false;
		}
		
		if ( !HotFolderConfig.isHFConfigInitialized() )
		{
			LoggerUtil.logError("SaveFolderServlet::mapSeclorePolicy::Failed to map policy on folder '"+folder.getName()+"' because HotFolder configuration was not found");
			return false;
		}
		
		try
		{
			// hot folder name, description, ext-ref-id, ext-ref-name,ext-data, ext-app-id
			String hfXML = HotFolderConfig.getCreateHotFolderXML( folder.getName(), "Folder created from DMS",
						folder.getId(), folder.getName(), "", "" );
		
			LoggerUtil.logDebug("Create HF Requesr XML: "+ hfXML);
			String responseXML = FSHelperLibrary.sendRequest(null, RequestType.RT_CREATE_HOT_FOLDER, hfXML);
			LoggerUtil.logDebug("Create HF Response XML: "+ responseXML);
			
			Node rootNode = XMLUtil.getRootNode(responseXML);
			Node requestStatusNode = XMLUtil.parseNode("request-status", rootNode);
			String returnValue = XMLUtil.parseString("return-value", requestStatusNode);
			
			// If folder already mapped
			if( "-220302".equals(returnValue) || "1".equals(returnValue) )
			{
				LoggerUtil.logDebug("SaveFolderServlet::mapSeclorePolicy::END");
				return true;
			}
		}
		catch (FSHelperException fsExp)
		{
			LoggerUtil.logError("Error while creating Hot Folder", fsExp);
		}
		
		LoggerUtil.logDebug("SaveFolderServlet::mapSeclorePolicy::END");
		return false;
		
	}
	*/

}
