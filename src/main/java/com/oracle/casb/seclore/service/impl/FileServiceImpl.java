package com.oracle.casb.seclore.service.impl;

import com.oracle.casb.seclore.model.*;
import com.oracle.casb.seclore.repository.FileRepository;
import com.oracle.casb.seclore.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created By : abhijsri
 * Date  : 19/06/18
 **/
public class FileServiceImpl implements FileService {
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private FileRepository fileRepository;

    public FileServiceImpl(SecuredFiles files) {
        fileRepository = new FileRepository(files);
    }

    @Override public FileInfoResponse getFileInfo(FileInfoRequest request) {
        FileInfoResponse  response = new FileInfoResponse();
        response.setAraHeader(request.getAraHeader());
        response.getAraHeader().setStatus((short) 1);
        AraResponseDetailsGetFileInfo fileInfo = new AraResponseDetailsGetFileInfo();
        fileInfo.setAraClassificationDetails(request.getRequestDetailsGetFileInfo().getAraClassificationDetails());
        ARAOwnerDetails owner = new ARAOwnerDetails();
        ARAUserDetails user = new ARAUserDetails();
        user.setEmailId(request.getRequestDetailsGetFileInfo().getAraOwnerDetails().getAraUserDetails().getEmailId());
        owner.setAraUserDetails(user);
        fileInfo.setAraOwnerDetails(owner);
        response.setResponseDetailsGetFileInfo(fileInfo);
        return response;
    }
}
