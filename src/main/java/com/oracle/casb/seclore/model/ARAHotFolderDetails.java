package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents Policy Server Folder information
 *
 * @author harindra.chaudhary
 */
@XmlRootElement(name = "ara-hot-folder-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class ARAHotFolderDetails {

    /**
     * Hot Folder id created by Policy Server
     */
    @XmlElement(name = "fs-id")
    private String fsId;

    /**
     * External application (i.e. DMS) folder Id
     */
    @XmlElement(name = "ext-id")
    private String extId;

    /**
     * External application (i.e. DMS) folder name
     */
    @XmlElement(name = "ext-name")
    private String extName;

    /**
     * Any external data which is mentioned at time of folder creation.
     */
    @XmlElement(name = "ext-data")
    private String extData;

    /**
     * Any external app id which is mentioned at time of folder creation.
     */
    @XmlElement(name = "ext-app-id")
    private String extAppId;

    public String getFsId() {
        return fsId;
    }

    public void setFsId(String fsId) {
        this.fsId = fsId;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    public String getExtAppId() {
        return extAppId;
    }

    public void setExtAppId(String extAppId) {
        this.extAppId = extAppId;
    }

}
