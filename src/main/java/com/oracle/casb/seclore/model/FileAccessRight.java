package com.oracle.casb.seclore.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created By : abhijsri
 * Date  : 05/06/18
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_id",
        "file_id",
        "permissions"
})
public class FileAccessRight {

    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("roles")
    private Set<Roles> permissions = new HashSet<>();

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("file_id")
    public String getFileId() {
        return fileId;
    }

    @JsonProperty("file_id")
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @JsonProperty("roles")
    public Set<Roles> getPermissions() {
        return permissions;
    }

    @JsonProperty("roles")
    public void setPermissions(Set<Roles> permissions) {
        this.permissions = permissions;
    }
}
