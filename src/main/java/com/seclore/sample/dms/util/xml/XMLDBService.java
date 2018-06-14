package com.seclore.sample.dms.util.xml;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.seclore.sample.dms.constant.Constants;
import com.seclore.sample.dms.core.AppData;
import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.core.Classification;
import com.seclore.sample.dms.core.ClassificationData;
import com.seclore.sample.dms.core.Owner;
import com.seclore.sample.dms.core.UserRight;
import com.seclore.sample.dms.core.master.AppRights;
import com.seclore.sample.dms.core.master.AppUsers;
import com.seclore.sample.dms.core.master.Rights;
import com.seclore.sample.dms.core.master.User;
import com.seclore.sample.dms.util.Global;
//import jdk.management.resource.internal.inst.InitInstrumentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLDBService {

    private static Logger logger = LoggerFactory.getLogger(XMLDBService.class);

    private static AppData sAppData = null;
    private static AppUsers sAppUsers = null;
    private static AppRights sAppRights = null;
    private static ClassificationData sClassificationData = null;

    /**
     * Initialize application data.
     *
     * @param pAppData
     * @param pAppUsers
     * @param pAppRights
     */
    public static void initializeAppData(AppData pAppData, AppUsers pAppUsers, AppRights pAppRights,
            ClassificationData pClassificationData) {
        sAppData = pAppData;
        sAppUsers = pAppUsers;
        sAppRights = pAppRights;
        sClassificationData = pClassificationData;
    }

    /**
     * Return Map of rights,right id as key and Rights holds right id and name.
     *
     * @return Map<String   ,       Rights>
     */
    public static Map<String, Rights> getAppRights() {
        return sAppRights.getRightsMap();
    }

    /**
     * Return Map of users,user id as key and User holds user id and name.
     *
     * @return
     */
    public static Map<String, User> getAppUsers() {
        return sAppUsers.getUserMap();
    }

    /**
     * Get user by given user id (email id).
     *
     * @return
     */
    public static User getAppUserById(String userId) {
        return sAppUsers.getUserMap().get(userId);
    }

    /**
     * Save new user
     *
     * @return
     */
    public static boolean addNewAppUser(String pUserId, String pName) {
        if (pUserId == null || pUserId.trim().isEmpty()) {
            return false;
        }
        if (pName == null || pName.trim().isEmpty()) {
            return false;
        }

        User lUser = new User();
        lUser.setId(pUserId);
        lUser.setName(pName);
        synchronized (sAppUsers) {
            sAppUsers.getUserMap().put(pUserId, lUser);
            boolean isSaved = XMLDataUtil.updateAppUsers(sAppUsers);
            if (isSaved == false) {
                sAppUsers.getUserMap().remove(pUserId);
            }
            return isSaved;
        }

    }

    public static boolean updateAppUser(String pUserId, String pName) {
        if (pUserId == null || pUserId.trim().isEmpty()) {
            return false;
        }
        if (pName == null || pName.trim().isEmpty()) {
            return false;
        }

        User lUser = getAppUserById(pUserId);
        String lstrOldName = lUser.getName();
        lUser.setName(pName);
        synchronized (sAppUsers) {
            sAppUsers.getUserMap().put(pUserId, lUser);
            boolean isSaved = XMLDataUtil.updateAppUsers(sAppUsers);
            if (isSaved == false) {
                lUser.setName(lstrOldName);
                sAppUsers.getUserMap().remove(pUserId);
            }
            return isSaved;
        }

    }

    public static boolean deleteAppUser(String pUserId) {
        if (pUserId == null || pUserId.trim().isEmpty()) {
            return false;
        }

        User lUser = getAppUserById(pUserId);

        synchronized (sAppUsers) {
            sAppUsers.getUserMap().remove(pUserId);
            boolean isSaved = XMLDataUtil.updateAppUsers(sAppUsers);
            if (isSaved == false) {
                sAppUsers.getUserMap().put(pUserId, lUser);
            }
            return isSaved;
        }

    }

    /**
     * @return
     */
    public static Collection<AppFolder> getFolderList() {
        return sAppData.getFolderMap().values();
    }

    /**
     * Get folder instance of given folder Id.
     *
     * @param folderId
     * @return AppFolder
     */
    public static AppFolder getFolder(String folderId) {
        AppFolder appFolder = sAppData.getFolderMap().get(folderId);
        return appFolder;

    }

    /**
     * Get all files of given folder Id.
     *
     * @param folderId
     * @return Collection<AppFile>
     */
    public static Collection<AppFile> getFileList(String folderId) {
        AppFolder appFolder = getFolder(folderId);
        if (appFolder == null) {
            return null;
        }
        return appFolder.getFileMap().values();
    }

    /**
     * Get file for given Id.
     *
     * @param folderId
     * @param fileId
     * @return Boolean
     */
    public static AppFile getFile(String folderId, String fileId) {
        AppFolder appFolder = getFolder(folderId);
        if (appFolder == null) {
            return null;
        }

        return appFolder.getFileMap().get(fileId);
    }

    /**
     * Create new folder.
     *
     * @param pAppFolder
     * @return Boolean
     */
    public static boolean createNewFolder(AppFolder pAppFolder) {
        if (pAppFolder == null) {
            return false;
        }

        String folderId = UUID.randomUUID().toString();
        String folderPath = Global.getAppDataDir() + File.separator + folderId;

        File lfile = new File(folderPath);
        if (!lfile.mkdirs()) {
            return false;
        }

        pAppFolder.setId(folderId);

        synchronized (sAppData) {
            sAppData.getFolderMap().put(pAppFolder.getId(), pAppFolder);
            boolean isSaved = XMLDataUtil.updateAppData(sAppData);
            if (isSaved == false) {
                sAppData.getFolderMap().remove(pAppFolder.getId());
                lfile.delete();
                pAppFolder.setId(null);

                // Delete created folder
                if (lfile.exists()) {
                    try {
                        lfile.delete();
                    } catch (SecurityException se) {
                        // Ignore
                    }
                }

            }
            return isSaved;
        }

    }

    /**
     * Update the folder.
     *
     * @param pFolder
     * @return Boolean
     */
    public static boolean updateFolder(AppFolder pFolder) {
        if (pFolder == null) {
            return false;
        }
        synchronized (sAppData) {
            AppFolder existingFolder = XMLDBService.getFolder(pFolder.getId());
            String oldName = existingFolder.getName();
            Boolean isIrmEnabled = existingFolder.getIrmEnabled();

            existingFolder.setName(pFolder.getName());
            existingFolder.setIrmEnabled(pFolder.getIrmEnabled());
            existingFolder.setHtmlWrapEnabled(pFolder.getHtmlWrapEnabled());
            String folderPath = Global.getAppDataDir() + File.separator + pFolder.getId();

            File lfile = new File(folderPath);
            if (!lfile.exists()) {
                lfile.mkdirs();
            }

            boolean isSaved = XMLDataUtil.updateAppData(sAppData);
            if (isSaved == false) {
                existingFolder.setName(oldName);
                existingFolder.setIrmEnabled(isIrmEnabled);
            }
            return isSaved;
        }

    }

    /**
     * Delete the folder.
     *
     * @param pFolder
     * @return Boolean
     */
    public static boolean deleteFolder(AppFolder pFolder) {
        if (pFolder == null) {
            return false;
        }

        String folderPath = Global.getAppDataDir() + File.separator + pFolder.getId();

        synchronized (sAppData) {
            sAppData.getFolderMap().remove(pFolder.getId());
            boolean isSaved = XMLDataUtil.updateAppData(sAppData);
            if (isSaved == false) {
                sAppData.getFolderMap().put(pFolder.getId(), pFolder);
            } else {
                File lfile = new File(folderPath);
                deleteFolder(lfile);
            }
            return isSaved;
        }
    }

    /**
     * Add new file in given folder.
     *
     * @param folderId - Folder Id in which new file will be created.
     * @param pNewFile - New AppFile instance
     * @return Boolean
     */
    public static boolean addNewFile(String folderId, AppFile pNewFile) {
        AppFolder folder = XMLDBService.getFolder(folderId);
        if (folder == null) {
            return false;
        }

        synchronized (sAppData) {
            folder.getFileMap().put(pNewFile.getId(), pNewFile);
            boolean isSaved = XMLDataUtil.updateAppData(sAppData);
            if (isSaved == false) {
                folder.getFileMap().remove(pNewFile.getId());
                pNewFile.setId(null);
                String filePath =
                        Global.getAppDataDir() + File.separator + folderId + File.separator + pNewFile.getName();

                File lFile = new File(filePath);
                if (lFile.exists()) {
                    lFile.delete();
                }
            }
            return isSaved;
        }
    }

    /**
     * Update file. Not implemented yet.
     *
     * @param folderId     Folder Id in which file will be stored.
     * @param pUpdatedFile
     * @return Boolean
     */
    public static boolean updateFile(String folderId, AppFile pUpdatedFile) {
        AppFile lExistingFile = XMLDBService.getFile(folderId, pUpdatedFile.getId());
        if (lExistingFile == null) {
            return false;
        }

        synchronized (sAppData) {
            Set<UserRight> oldUserRightList = lExistingFile.getUserRightList();
            Owner oldOwner = lExistingFile.getOwner();
            Classification oldClassification = lExistingFile.getClassification();

            lExistingFile.setUserRightList(pUpdatedFile.getUserRightList());
            lExistingFile.setOwner(pUpdatedFile.getOwner());
            lExistingFile.setClassification(pUpdatedFile.getClassification());
            boolean isSaved = XMLDataUtil.updateAppData(sAppData);
            if (isSaved == false) {
                lExistingFile.setUserRightList(oldUserRightList);
                lExistingFile.setOwner(oldOwner);
                lExistingFile.setClassification(oldClassification);
            }
            return isSaved;
        }
    }

    /**
     * Update users and their rights for given file.
     *
     * @param folderId Folder Id from which file belongs.
     * @param fileId    File id for which users and right will be changed.
     * @return Boolean
     */
    public static boolean updateUsersRightsOnFile(String folderId, String fileId, Set<UserRight> newUserRights) {
        AppFile lFile = XMLDBService.getFile(folderId, fileId);

        if (lFile == null) {
            return false;
        }

        synchronized (sAppData) {
            Set<UserRight> oldUserRightList = lFile.getUserRightList();
            lFile.setUserRightList(newUserRights);
            boolean isSaved = XMLDataUtil.updateAppData(sAppData);
            if (isSaved == false) {
                lFile.setUserRightList(oldUserRightList);
            }
            return isSaved;
        }
    }

    /**
     * Delete the file.
     *
     * @param folderId
     * @param pFile
     * @return
     */
    public static boolean deleteFile(String folderId, AppFile pFile) {
        AppFolder folder = XMLDBService.getFolder(folderId);
        if (folder == null) {
            return false;
        }

        synchronized (sAppData) {
            folder.getFileMap().remove(pFile.getId());
            boolean isSaved = XMLDataUtil.updateAppData(sAppData);
            if (isSaved == false) {
                folder.getFileMap().put(pFile.getId(), pFile);
            } else {
                String filePath = Global.getAppDataDir() + File.separator + folderId + File.separator + pFile.getName();

                File lFile = new File(filePath);
                if (lFile.exists()) {
                    lFile.delete();
                }
            }
            return isSaved;
        }
    }

    /**
     * Get usages rights for given file and user.
     *
     * @param userId
     * @param emailId
     * @param folderId
     * @param fileId
     * @return Integer usage rights value.
     */
    public static Integer getUserRights(String userId, String emailId, String folderId, String fileId) {

        logger.debug("User ID " + userId);
        logger.debug("Email ID " + emailId);
        logger.debug("Folder ID " + folderId);
        logger.debug("File ID " + fileId);
        AppFile lFile = XMLDBService.getFile(folderId, fileId);
        if (lFile == null) {
            // File does not exist
            return Constants.FILE_NOT_EXIST;
        }

        Set<UserRight> usersRights = lFile.getUserRightList();
        if (usersRights == null) {
            // No user has rights to the file
            return Constants.NO_RIGHTS;
        }

        for (UserRight userRight : usersRights) {
            // Check both userId and email, because in this Sample application we can put email id or other value for user identifier.
            // It depends on Client's Application.
            // Policy Server will pass user's ext id and email id.

            // if( userRight.getUserId().equals(userId) )
            if (userRight.getUserId().equals(userId) || userRight.getUserId().equals(emailId)) {
                Integer right = userRight.getUsageRights();
                if (right == null) {
                    return Constants.NO_RIGHTS;
                }
                return right;
            }
        }

        return Constants.NO_RIGHTS;

    }

    public static void deleteFolder(File pFolder) {
        if (pFolder == null) {
            return;
        }

        // Delete folder
        if (pFolder.exists()) {
            File[] files = pFolder.listFiles();
            if (files != null & files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFolder(f);
                    } else {
                        try {
                            f.delete();
                        } catch (SecurityException se) {
                        }
                    }
                }
            }

            try {
                pFolder.delete();
            } catch (SecurityException se) {
                // Ignore
            }
        }
    }

    public static Set<Classification> getClassifications() {
        return sClassificationData.getClassifications();
    }

    public static boolean updateClassifications(Set<Classification> pClassifications) {
        synchronized (sClassificationData) {
            sClassificationData.setClassifications(pClassifications);
            return XMLDataUtil.updateClassificationData(sClassificationData);
        }
    }

    public static Classification getClassification(String liClasifn) {
        Set<Classification> lsetClasifn = sClassificationData.getClassifications();
        for (Classification classification : lsetClasifn) {
            if (classification.getId().equals(liClasifn)) {
                return classification;
            }
        }
        return null;
    }

}
