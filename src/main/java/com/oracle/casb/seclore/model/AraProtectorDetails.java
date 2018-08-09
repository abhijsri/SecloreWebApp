package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-protector-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class AraProtectorDetails implements Serializable {
    /**
     * File Owner Details
     */
    @XmlElement(name = "ara-user-details")
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
