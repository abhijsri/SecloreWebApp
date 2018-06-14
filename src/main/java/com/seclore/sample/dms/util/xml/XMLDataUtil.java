package com.seclore.sample.dms.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.seclore.sample.dms.core.AppData;
import com.seclore.sample.dms.core.ClassificationData;
import com.seclore.sample.dms.core.master.AppRights;
import com.seclore.sample.dms.core.master.AppUsers;
import com.seclore.sample.dms.util.Global;
import com.seclore.sample.dms.util.LoggerUtil;

public class XMLDataUtil {

    private static final String APP_DATA_PATH = "data" + File.separator + "AppDataDB.xml";
    private static final String APP_USERS_PATH = "data" + File.separator + "UserMaster.xml";
    private static final String APP_RIGHTS_PATH = "data" + File.separator + "RightsMaster.xml";
    private static final String APP_CLASSIFICATION_PATH = "data" + File.separator + "Classification.xml";

    private static Marshaller sAppDataMarshaller = null;
    private static Marshaller sAppUsersMarshaller = null;
    private static Marshaller sClassificationDataMarshaller = null;

    public static void initializeAppData(String appPath) {
        // Initialize sample application data
        AppData lAppData = loadAppData(appPath);

        // Initialize sample application users
        AppUsers lAppUsers = loadAppUsers(appPath);

        //Initialize sample application rights
        AppRights lAppRights = loadAppRights(appPath);

        //Initialize classifications
        ClassificationData lClassificationData = loadClassificationData(appPath);

        XMLDBService.initializeAppData(lAppData, lAppUsers, lAppRights, lClassificationData);
    }

    /**
     * Initialize sample application rights
     *
     * @param appPath
     * @return
     */
    private static AppRights loadAppRights(String appPath) {
        AppRights lAppRights = null;
        FileInputStream inputStream = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AppRights.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            inputStream = new FileInputStream(appPath + File.separator + APP_RIGHTS_PATH);
            lAppRights = (AppRights) jaxbUnmarshaller.unmarshal(inputStream);
        } catch (JAXBException jaxbException) {
            jaxbException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (lAppRights == null) {
                lAppRights = new AppRights();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }

        return lAppRights;
    }

    /**
     * Initialize sample application users
     *
     * @param appPath
     * @return
     */
    private static AppUsers loadAppUsers(String appPath) {
        AppUsers lAppUsers = null;
        FileInputStream inputStream = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AppUsers.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            inputStream = new FileInputStream(appPath + File.separator + APP_USERS_PATH);
            lAppUsers = (AppUsers) jaxbUnmarshaller.unmarshal(inputStream);
        } catch (JAXBException jaxbException) {
            jaxbException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (lAppUsers == null) {
                lAppUsers = new AppUsers();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return lAppUsers;
    }

    /**
     * Initialize sample application data
     *
     * @param appPath
     * @return
     */
    private static AppData loadAppData(String appPath) {
        AppData lAppData = null;
        InputStream inputStream = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AppData.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            inputStream = new FileInputStream(appPath + File.separator + APP_DATA_PATH);
            lAppData = (AppData) jaxbUnmarshaller.unmarshal(inputStream);
        } catch (FileNotFoundException lFileNotFoundException) {
            // lFileNotFoundException.printStackTrace();
        } catch (JAXBException jaxbException) {
            jaxbException.printStackTrace();
            LoggerUtil.logError("Error while loading AppData", jaxbException);
        } finally {
            if (lAppData == null) {
                lAppData = new AppData();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return lAppData;
    }

    /**
     * Update AppDataDB.xml
     *
     * @param pAppData
     * @return
     */
    public static boolean updateAppData(AppData pAppData) {
        OutputStream outputStream = null;
        try {
            if (sAppDataMarshaller == null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(AppData.class);
                sAppDataMarshaller = jaxbContext.createMarshaller();
            }
            outputStream = new FileOutputStream(Global.getAppRealPath() + File.separator + APP_DATA_PATH);
            sAppDataMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            sAppDataMarshaller.marshal(pAppData, outputStream);
            return true;
        } catch (JAXBException jaxbException) {
            LoggerUtil.logError("Error while updating AppData", jaxbException);
            jaxbException.printStackTrace();
        } catch (IOException e) {
            LoggerUtil.logError("Error while updating AppData", e);
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e1) {
                    // Ignore
                }

                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return false;
    }

    /**
     * Update UserMaster.xml
     *
     * @param pAppUsers
     * @return
     */
    public static boolean updateAppUsers(AppUsers pAppUsers) {
        OutputStream outputStream = null;
        try {
            if (sAppUsersMarshaller == null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(AppUsers.class);
                sAppUsersMarshaller = jaxbContext.createMarshaller();
            }
            outputStream = new FileOutputStream(Global.getAppRealPath() + File.separator + APP_USERS_PATH);
            sAppUsersMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            sAppUsersMarshaller.marshal(pAppUsers, outputStream);
            return true;
        } catch (JAXBException jaxbException) {
            LoggerUtil.logError("Error while updating AppUsers", jaxbException);
            jaxbException.printStackTrace();
        } catch (IOException e) {
            LoggerUtil.logError("Error while updating AppUsers", e);
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e1) {
                    // Ignore
                }

                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return false;
    }

    private static ClassificationData loadClassificationData(String appPath) {
        ClassificationData lClassificationData = null;
        InputStream inputStream = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ClassificationData.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            inputStream = new FileInputStream(appPath + File.separator + APP_CLASSIFICATION_PATH);
            lClassificationData = (ClassificationData) jaxbUnmarshaller.unmarshal(inputStream);
        } catch (FileNotFoundException lFileNotFoundException) {
            // lFileNotFoundException.printStackTrace();
        } catch (JAXBException jaxbException) {
            jaxbException.printStackTrace();
            LoggerUtil.logError("Error while loading AppData", jaxbException);
        } finally {
            if (lClassificationData == null) {
                lClassificationData = new ClassificationData();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return lClassificationData;
    }

    public static boolean updateClassificationData(ClassificationData pClassificationData) {
        OutputStream outputStream = null;
        try {
            if (sClassificationDataMarshaller == null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(ClassificationData.class);
                sClassificationDataMarshaller = jaxbContext.createMarshaller();
            }
            outputStream = new FileOutputStream(Global.getAppRealPath() + File.separator + APP_CLASSIFICATION_PATH);
            sClassificationDataMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            sClassificationDataMarshaller.marshal(pClassificationData, outputStream);
            return true;
        } catch (JAXBException jaxbException) {
            LoggerUtil.logError("Error while updating ClassificationData", jaxbException);
            jaxbException.printStackTrace();
        } catch (IOException e) {
            LoggerUtil.logError("Error while updating ClassificationData", e);
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e1) {
                    // Ignore
                }

                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return false;
    }
}
