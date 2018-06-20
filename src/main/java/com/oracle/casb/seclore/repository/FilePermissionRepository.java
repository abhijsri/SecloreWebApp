package com.oracle.casb.seclore.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.oracle.casb.seclore.model.FileAccessRight;
import com.oracle.casb.seclore.model.FileAccessRights;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
public class FilePermissionRepository {

    Logger logger = LoggerFactory.getLogger(FilePermissionRepository.class);

    private final String fileName = "UserFileAccess.yaml";

    private FileAccessRights fileAccessRights;

    Table<Long, String, FileAccessRight> userFileAccessTable;

    public FilePermissionRepository(FileAccessRights fileAccessRights) {
        this.fileAccessRights = fileAccessRights;
        userFileAccessTable = populateFileAccessTable();
    }

    public FilePermissionRepository(ObjectMapper mapper) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            fileAccessRights = mapper.readValue(classloader.getResourceAsStream(fileName), FileAccessRights.class);
            userFileAccessTable = populateFileAccessTable();
        } catch (IOException e) {
            logger.error("Unable to read user repo file {}", fileName);
            throw e;
        }
    }

    private Table<Long,String,FileAccessRight> populateFileAccessTable() {
        ImmutableTable.Builder builder = ImmutableTable.<Long,String,Integer>builder();
        for (FileAccessRight fileAccessEntry : fileAccessRights.getFileAccessRights()) {
            builder.put(fileAccessEntry.getUserId(), fileAccessEntry.getFileId(), fileAccessEntry);
        }
        return builder.build();
    }

    public FileAccessRight getAccessRights(Long id, String extId) {
        return userFileAccessTable.get(id, extId);
    }
}
