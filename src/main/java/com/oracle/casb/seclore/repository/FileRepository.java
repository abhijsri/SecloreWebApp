package com.oracle.casb.seclore.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.casb.seclore.model.SecuredFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
public class FileRepository {

    Logger logger = LoggerFactory.getLogger(FileRepository.class);

    private final String fileName = "FileList.yaml";

    private SecuredFiles files;


    public FileRepository(ObjectMapper mapper) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            files = mapper.readValue(classloader.getResourceAsStream(fileName), SecuredFiles.class);
        } catch (IOException e) {
            logger.error("Unable to read user repo file {}", fileName);
            throw e;
        }
    }


    public com.oracle.casb.seclore.model.File getFile(Long fsId) {
        return files.getFiles().get(fsId);
    }

}
