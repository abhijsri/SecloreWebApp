package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Represents Policy Server User details
 *
 * @author harindra.chaudhary
 */
@XmlRootElement(name = "ara-user-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class ARAUserDetails
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "ext-id")
    private String extId;

    @XmlElement(name = "rep-code")
    private String repoCode;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "email-id")
    private String emailId;

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getRepoCode() {
        return repoCode;
    }

    public void setRepoCode(String repoCode) {
        this.repoCode = repoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
