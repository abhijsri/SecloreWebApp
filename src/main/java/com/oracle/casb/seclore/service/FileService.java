package com.oracle.casb.seclore.service;

import com.oracle.casb.seclore.model.FileInfoRequest;
import com.oracle.casb.seclore.model.FileInfoResponse;

public interface FileService {
    /**
     * @param request
     * @return
     */
    public FileInfoResponse getFileInfo(FileInfoRequest request);
}
