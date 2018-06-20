package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-response-details-get-access-right")
@XmlAccessorType(XmlAccessType.FIELD)

public class AraResponseDetailsGetAccessRight implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ara-access-right-details", required = true)
    @JacksonXmlProperty(localName = "ara-access-right-details")
    protected AraAccessRightDetails araAccessRightDetails;
    @XmlElement(name = "ara-watermark-details")
    @JacksonXmlProperty(localName = "ara-watermark-details")
    protected AraWaterMarkDetails araWaterMarkDetails;
    @XmlElement(name = "ara-owner-details", required = true)
    @JacksonXmlProperty(localName = "ara-owner-details")
    protected  ARAOwnerDetails araOwnerDetails;
    @XmlElement(name = "ara-classification-details", required = true)
    @JacksonXmlProperty(localName = "ara-classification-details")
    protected ARAClassificationDetails araClassificationDetails;

    @XmlElement(name = "ara-display-message", required = true)
    @JacksonXmlProperty(localName = "ara-display-message")
    protected String displayMessage;

    public AraWaterMarkDetails getAraWaterMarkDetails() {
        return araWaterMarkDetails;
    }

    public void setAraWaterMarkDetails(AraWaterMarkDetails araWaterMarkDetails) {
        this.araWaterMarkDetails = araWaterMarkDetails;
    }

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

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
