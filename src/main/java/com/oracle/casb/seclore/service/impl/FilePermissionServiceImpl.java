package com.oracle.casb.seclore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.oracle.casb.seclore.model.*;
import com.oracle.casb.seclore.repository.FilePermissionRepository;
import com.oracle.casb.seclore.repository.FileRepository;
import com.oracle.casb.seclore.repository.UserRepository;
import com.oracle.casb.seclore.service.FilePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/

public class FilePermissionServiceImpl implements FilePermissionService {


    Logger logger = LoggerFactory.getLogger(FilePermissionServiceImpl.class);

    private UserRepository userRepository;

    private FileRepository fileRepository;

    private FilePermissionRepository filePermissionRepository;

    public FilePermissionServiceImpl(ObjectMapper objectMapper) throws IOException {
        userRepository = new UserRepository(objectMapper);
        fileRepository = new FileRepository(objectMapper);
        filePermissionRepository = new FilePermissionRepository(objectMapper);
    }

    @Override
    public AccessRightResponse getFileAccessPermissions(AccessRightRequest request) {
        Set<Roles> roles = null;
        User user = userRepository.getUser(request.getAraRequestDetailsGetAccessRight().getAraUserDetails().getExtId());
        File file = fileRepository.getFile(request.getAraRequestDetailsGetAccessRight().getAraFileDetails().getFsId());
        if (user == null || file == null) {
            logger.debug("User is null or File is null");
            roles = ImmutableSet.<Roles>of(Roles.EXTERNAL);
        } else {
            logger.debug("User Id {}", user.getExtId());
            logger.debug("File Id {}", file.getExtName());
            FileAccessRight accessRight = filePermissionRepository.getAccessRights(user.getId(), file.getExtId());
            roles = accessRight.getPermissions();
        }
        return createFileAccessResponse(request, roles);
    }

    private AccessRightResponse createFileAccessResponse(AccessRightRequest request, Set<Roles> roles) {
        AccessRightResponse response = new AccessRightResponse();
        response.setAraResponseHeader(request.getAraHeader());
        response.setAraResponseDetailsGetAccessRight(createAraResponseDetailsGetAccessRight(request, roles));
        return response;
    }

    private AraResponseDetailsGetAccessRight createAraResponseDetailsGetAccessRight(AccessRightRequest request,
            Set<Roles> roles) {
        AraResponseDetailsGetAccessRight araResponseDetailsGetAccessRight
                = new AraResponseDetailsGetAccessRight();
        araResponseDetailsGetAccessRight.setAraAccessRightDetails(createAccessRightDetails(request, roles));
        araResponseDetailsGetAccessRight
                .setAraClassificationDetails(request.getAraRequestDetailsGetAccessRight().getAraClassificationDetails());
        araResponseDetailsGetAccessRight
                .setAraOwnerDetails(request.getAraRequestDetailsGetAccessRight().getAraOwnerDetails());
        return araResponseDetailsGetAccessRight;
    }

    private AraAccessRightDetails createAccessRightDetails(AccessRightRequest request, Set<Roles> roles) {
        AraAccessRightDetails araAccessRightDetails = new AraAccessRightDetails();
        Set<FSPermissions> fsPermissionSet
                = roles.stream().filter(e -> e.getPermissions() != null)
                .flatMap(e -> e.getPermissions().stream()).collect(Collectors.toSet());
        araAccessRightDetails.setPrimaryAccessRight(FSPermissions.calculateFilePermissions(fsPermissionSet));
        araAccessRightDetails.setOfflineAccessRight(Boolean.TRUE);
        return araAccessRightDetails;
    }
}
