package com.oracle.casb.seclore.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By : abhijsri
 * Date  : 05/06/18
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SecuredFiles"
})
public class SecuredFiles {

    @JsonProperty("SecuredFiles")
    private Map<Long, File> files = new HashMap<>();

    @JsonProperty("SecuredFiles")
    public Map<Long, File> getFiles() {
        return files;
    }

    @JsonProperty("SecuredFiles")
    public void setFiles(Map<Long, File> files) {
        this.files = files;
    }
}

