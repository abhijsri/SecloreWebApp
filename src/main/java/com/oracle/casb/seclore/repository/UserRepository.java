package com.oracle.casb.seclore.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.casb.seclore.model.User;
import com.oracle.casb.seclore.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
public class UserRepository {

    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final String fileName = "User.yaml";

    private Users users;

    public UserRepository(Users users) {
        this.users = users;
    }

    public UserRepository(ObjectMapper mapper) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            users = mapper.readValue(classloader.getResourceAsStream(fileName), Users.class);
        } catch (IOException e) {
            logger.error("Unable to read user repo file {}", fileName);
            throw e;
        }
    }

    public User getUser(String extId) {
        for (User user : users.getUsers().values()) {
            if (user.getExtId().equalsIgnoreCase(extId)) {
                return user;
            }
        }
        return null;
    }
}
