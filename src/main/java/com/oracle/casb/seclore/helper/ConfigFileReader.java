package com.oracle.casb.seclore.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

/**
 * Created By : abhijsri
 * Date  : 19/06/18
 **/
public class ConfigFileReader {
    private ObjectMapper mapper;

    public ConfigFileReader() {
        this.mapper = new ObjectMapper(new YAMLFactory());;
    }

    public  <T> T readFromFile(String fileName, Class<T> clazz) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return mapper.readValue(classloader.getResourceAsStream(fileName), clazz);
    }
}
