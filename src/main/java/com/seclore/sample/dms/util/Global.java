package com.seclore.sample.dms.util;

import java.io.File;

import com.seclore.sample.dms.config.HotFolderConfig;
import com.seclore.sample.dms.constant.Constants;
import com.seclore.sample.dms.util.xml.XMLDataUtil;

public class Global {

    private static String mstrAppRealPath = "";

    public static void initializeApplication(String pAppRealPath) {
        mstrAppRealPath = pAppRealPath;

        // Initialize logger
        LoggerUtil.initializeLogger(pAppRealPath);

        // Initialize app-data
        XMLDataUtil.initializeAppData(pAppRealPath);

        HotFolderConfig.initializeHFConfig(pAppRealPath);
    }

    /**
     * Get application real path
     *
     * @return
     */
    public static String getAppRealPath() {
        return mstrAppRealPath;
    }

    /**
     * Get application data directory where all folder and files will be created.
     *
     * @return
     */
    public static String getAppDataDir() {
        return getAppRealPath() + File.separator + Constants.DATA_DIR + File.separator;
    }

}
