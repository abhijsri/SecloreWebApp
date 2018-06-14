package com.seclore.sample.dms.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.seclore.sample.dms.util.LoggerUtil;

public class HotFolderConfig {
    private static final String HF_CONFIG_PATH = "config" + File.separator + "hotFolderConfig.properties";
    private static boolean sbInitializeHFConfig = false;

    /*** Commented below variables- not required in Fully federated Cabinet */
    private static String sstrExtnHotFolderId;
	
	/*
	private static String sUseAra 				= "";
	private static String sClassificationId		= "";
	private static String sOwnerRepoCode		= "";
	private static String sOwnerId				= "";
	private static String sProtectorXML			= "";
	private static String sApplicationId		= "";
	*/

    public static boolean isHFConfigInitialized() {
        return sbInitializeHFConfig;
    }

    public static void initializeHFConfig(String pAppPath) {
        InputStream lInputStream = null;
        try {

            Properties lProps = new Properties();

            String lstrHFConfigFile = pAppPath + File.separator + HF_CONFIG_PATH;
            lInputStream = new FileInputStream(lstrHFConfigFile);
            lProps.load(lInputStream);

            // Fetch hot folder external id
            String lstrHFExtnRefId = lProps.getProperty("hotFolder.external.ref.id", "");
            if (lstrHFExtnRefId == null || lstrHFExtnRefId.trim().isEmpty()) {
                throw new Exception("HotFolder External Reference id is not configured!");
            }
            sstrExtnHotFolderId = lstrHFExtnRefId.trim();
            sbInitializeHFConfig = true;
        } catch (Exception lEx) {
            LoggerUtil.logError("Error while initialize HotFolder config properties", lEx);
        } finally {
            if (lInputStream != null) {
                try {
                    lInputStream.close();
                } catch (IOException ioException) { /*Ignore */}
            }
        }
    }

    public static String getExternalHotFolderId() {
        return sstrExtnHotFolderId;
    }

    /* Commented below code - Not required in Fully federated Cabinet */
	/*
	public static void initializeHFConfig(String pAppPath)
	{
		InputStream lInputStream = null;
		boolean isValid = true;
		try
    	{
    		
    		Properties lProps = new Properties();       
    		
        	String lstrHFConfigFile = pAppPath + File.separator + HF_CONFIG_PATH;
        	lInputStream = new FileInputStream(lstrHFConfigFile);
        	lProps.load(lInputStream);
        	
        	// Fetch hot folder owner details
        	sOwnerRepoCode = lProps.getProperty("hotFolder.owner.repoCode","");
        	sOwnerId = lProps.getProperty("hotFolder.owner.id","");

        	if( sOwnerRepoCode.trim().isEmpty() )
        	{
        		LoggerUtil.logError("Hot folder owner repository code is missing");
        		isValid = false;
        	}
        	if( sOwnerId.trim().isEmpty() )
        	{
        		LoggerUtil.logError("Hot folder owner id is missing");
        		isValid = false;
        	}
        	
        	// Fetch hot folder protector details
        	String protectorRepoCode = lProps.getProperty("hotFolder.protector.repoCode","");
        	String protectorId = lProps.getProperty("hotFolder.protector.id","");
        	
        	if( !protectorRepoCode.trim().isEmpty() && !protectorId.trim().isEmpty() )
        	{
        		sProtectorXML =  "<protector><entity><rep-code>"+protectorRepoCode+"</rep-code><id>"+XMLUtil.escapeForXML(protectorId)+"</id></entity></protector>";
        	}
        	
        	sClassificationId = lProps.getProperty("classification.id","");
        	if( sClassificationId.trim().isEmpty() )
        	{
        		LoggerUtil.logError("Hot folder classification id is missing");
        		isValid = false;
        	}
        	
        	sApplicationId = lProps.getProperty("application.id","");
        	if( sApplicationId.trim().isEmpty() )
        	{
        		LoggerUtil.logError("Hot folder application id is missing");
        		isValid = false;
        	}
        	
        	sUseAra = lProps.getProperty("use.application.rights","0");
        	if( !"0".equals(sUseAra) && !"1".equals("1") )
        	{
        		sUseAra = "0";
        		LoggerUtil.logError("Value for use application rights is invalid, Default");
        		isValid = false;
        	}
        	
        	//String credentials = lProps.getProperty("credential.ids","");
    	}
    	catch(Exception lEx)
    	{
    		LoggerUtil.logError("Error while initialize HotFolder config properties", lEx);
    	}
		finally
		{
			if( lInputStream != null)
			{
				try
				{
					lInputStream.close();
				}
				catch(IOException ioException)
				{
					// Ignore
				}
			}
			
			if( isValid == true )
			{
				sbInitializeHFConfig = true;
			}
			else
			{
				sbInitializeHFConfig = false;
			}
		}
	}
	*/

    /*
     * @param hotFolderName Hot Folder name
     * @param description Description
     * @param extRefId External folder Id
     * @param extRefName External reference name
     * @param extRefData External reference data
     * @param extAppId External Application Id
     * @return xml as string.
     * @throws FSHelperException
     */
	/*
	public static String getCreateHotFolderXML(String hotFolderName, String description,  String extRefId, String extRefName, String extRefData, String extAppId) throws FSHelperException
    {
		PSConnection lPSConnection = FSHelperLibrary.getPSConnection();
		String lstrPSid = lPSConnection.getPSId();
		FSHelperLibrary.releasePSConnection(lPSConnection);
		
        String lstrXML = "<request>" +
	        				  "<request-header>" +
	                     			"<protocol-version>2</protocol-version>" +
	                     	  "</request-header>" +
                     		
        				"<request-details>" +
        				"<ps-id>"+lstrPSid +"</ps-id>" +
        				"<hot-folder>" +
        					"<name>"+ XMLUtil.escapeForXML(hotFolderName) +"</name>" +
        					"<description>"+ XMLUtil.escapeForXML(description) +"</description>" +
        					
        					"<use-ara>"+sUseAra+"</use-ara>" +
        					
        					"<location>LOGICALHOTFOLDER</location><inherits>0</inherits><parent-id>0</parent-id><recursive>1</recursive><type>1</type><process-all-supported>1</process-all-supported>" +
        					
        					"<protection-details>" +
        						"<classification><id>"+sClassificationId+"</id></classification>" +
        						"<owner>" +
        							"<entity>" +
        								"<rep-code>"+ sOwnerRepoCode +"</rep-code>" +
        								"<id>"+ XMLUtil.escapeForXML(sOwnerId) +"</id>" +
        							"</entity>" +
        						"</owner>" +
        						sProtectorXML +
        					"</protection-details>" +
        						
        					"<file-server>" +
        						"<id>"+ sApplicationId +"</id>" +
        					"</file-server>" +
        						
        					"<extn-reference>" +
        						"<extn-ref-id>"+ XMLUtil.escapeForXML(extRefId) +"</extn-ref-id>" +
        						"<extn-ref-name>"+ XMLUtil.escapeForXML(extRefName) +"</extn-ref-name>" +
        						"<extn-ref-data>"+ XMLUtil.escapeForXML(extRefData) +"</extn-ref-data>" +
        						"<extn-app-id>"+ XMLUtil.escapeForXML(extAppId) +"</extn-app-id>" +
        					"</extn-reference>" +
        						
        				"</hot-folder>" +
        				"</request-details>" +
        				"</request>";
                        
        return lstrXML;
    }
	*/

}
