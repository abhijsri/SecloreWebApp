package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By : abhijsri
 * Date  : 05/06/18
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "FileAccessRights"
})
public class FileAccessRights {
    @JsonProperty("FileAccessRights")
    private List<FileAccessRight> fileAccessRights = new ArrayList<FileAccessRight>();

    @JsonProperty("FileAccessRights")
    public List<FileAccessRight> getFileAccessRights() {
        return fileAccessRights;
    }

    @JsonProperty("FileAccessRights")
    public void setFileAccessRights(List<FileAccessRight> fileAccessRights) {
        this.fileAccessRights = fileAccessRights;
    }
}
