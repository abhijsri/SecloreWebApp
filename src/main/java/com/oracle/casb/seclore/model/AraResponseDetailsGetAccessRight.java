package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-response-details-get-access-right")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "araAccessRightDetails",
        "araOwnerDetails",
        "araClassificationDetails"
})
public class AraResponseDetailsGetAccessRight implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ara-access-right-details", required = true)
    protected AraAccessRightDetails araAccessRightDetails;
    @XmlElement(name = "ara-owner-details", required = true)
    protected  ARAOwnerDetails araOwnerDetails;
    @XmlElement(name = "ara-classification-details", required = true)
    protected ARAClassificationDetails araClassificationDetails;

    public AraAccessRightDetails getAraAccessRightDetails() {
        return araAccessRightDetails;
    }

    public void setAraAccessRightDetails(AraAccessRightDetails araAccessRightDetails) {
        this.araAccessRightDetails = araAccessRightDetails;
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
