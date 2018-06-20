package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author harindra.chaudhary
 * Represents Policy Server File Owner information
 */
@XmlRootElement(name = "ara-owner-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class ARAOwnerDetails {
    /**
     * File Owner Details
     */
    @XmlElement(name = "ara-user-details")
    @JacksonXmlProperty(localName = "ara-user-details")
    private ARAUserDetails araUserDetails;

    /**
     * Get File Owner user
     *
     * @return
     */
    public ARAUserDetails getAraUserDetails() {
        return araUserDetails;
    }

    /**
     * Set File Owner user
     *
     * @param araUserDetails
     */
    public void setAraUserDetails(ARAUserDetails araUserDetails) {
        this.araUserDetails = araUserDetails;
    }

}
