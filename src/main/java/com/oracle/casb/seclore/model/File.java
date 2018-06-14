package com.oracle.casb.seclore.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created By : abhijsri
 * Date  : 05/06/18
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "fs-id",
        "ext-id",
        "ext-name",
        "ext-app-id"
})
public class File {
    @JsonProperty("fs-id")
    private Long fileServerId;

    @JsonProperty("ext-id")
    private String extId;
    @JsonProperty("ext-name")
    private String extName;
    @JsonProperty("ext-app-id")
    private String extAppId;

    @JsonProperty("fs-id")
    public Long getFileServerId() {
        return fileServerId;
    }
    @JsonProperty("fs-id")
    public void setFileServerId(Long fileServerId) {
        this.fileServerId = fileServerId;
    }
    @JsonProperty("ext-id")
    public String getExtId() {
        return extId;
    }

    @JsonProperty("ext-id")
    public void setExtId(String extId) {
        this.extId = extId;
    }

    @JsonProperty("ext-name")
    public String getExtName() {
        return extName;
    }

    @JsonProperty("ext-name")
    public void setExtName(String extName) {
        this.extName = extName;
    }

    @JsonProperty("ext-app-id")
    public String getExtAppId() {
        return extAppId;
    }

    @JsonProperty("ext-app-id")
    public void setExtAppId(String extAppId) {
        this.extAppId = extAppId;
    }
}
