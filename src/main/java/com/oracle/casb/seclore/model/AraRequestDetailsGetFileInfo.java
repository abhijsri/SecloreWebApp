package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ara-request-details-get-file-information")
public class AraRequestDetailsGetFileInfo implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ara-file-details", required = true)
    protected ARAFileDetails araFileDetails;
    @XmlElement(name = "ara-hot-folder-details", required = true)
    protected ARAHotFolderDetails araHotFolderDetails;
    /*@XmlElement(name = "ara-user-details", required = true)
    protected ARAUserDetails araUserDetails;*/
    @XmlElement(name = "ara-owner-details", required = true)
    protected ARAOwnerDetails araOwnerDetails;
   /* @XmlElement(name = "ara-protector-details", required = true)
    protected AraProtectorDetails araProtectorDetails;
    @XmlElement(name = "ara-client-details", required = true)
    protected  AraClientDetails araClientDetails;
    @XmlElement(name = "ara-environment-details", required = true)
    protected AraEnvironmentDetails araEnvironmentDetails;
    @XmlElement(name = "ara-file-usage-details", required = true)
    protected  AraFileUsageDetails araFileUsageDetails;*/
    @XmlElement(name = "ara-classification-details", required = true)
    protected   ARAClassificationDetails araClassificationDetails;

    public ARAFileDetails getAraFileDetails() {
        return araFileDetails;
    }

    public void setAraFileDetails(ARAFileDetails araFileDetails) {
        this.araFileDetails = araFileDetails;
    }

    public ARAHotFolderDetails getAraHotFolderDetails() {
        return araHotFolderDetails;
    }

    public void setAraHotFolderDetails(ARAHotFolderDetails araHotFolderDetails) {
        this.araHotFolderDetails = araHotFolderDetails;
    }

    public ARAOwnerDetails getAraOwnerDetails() {
        return araOwnerDetails;
    }

    public void setAraOwnerDetails(ARAOwnerDetails araOwnerDetails) {
        this.araOwnerDetails = araOwnerDetails;
    }

    public ARAClassificationDetails getAraClassificationDetails() {
        return araClassificationDetails;
    }

    public void setAraClassificationDetails(ARAClassificationDetails araClassificationDetails) {
        this.araClassificationDetails = araClassificationDetails;
    }
}