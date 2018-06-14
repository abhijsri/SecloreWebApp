package com.oracle.casb.seclore.service;

import com.oracle.casb.seclore.model.AccessRightRequest;
import com.oracle.casb.seclore.model.AccessRightResponse;

public interface FilePermissionService {

    public AccessRightResponse getFileAccessPermissions(AccessRightRequest request);

}
