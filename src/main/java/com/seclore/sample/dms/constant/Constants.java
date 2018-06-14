package com.seclore.sample.dms.constant;

import java.io.File;

public class Constants {
    public static final String DATA_DIR = "data" + File.separator + "FileStore";

    public static final int FILE_NOT_EXIST = -1;
    public static final int NO_RIGHTS = 0;

    /* Max file size allowed to upload in MB*/
    public static final long MAX_FILE_SIZE = 50;

    public static final String KEY_HF_EXTN_REF_ID = "hotfolder-extn-ref-id";
}
