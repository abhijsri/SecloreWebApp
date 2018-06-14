package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents Policy Server classification information.
 *
 * @author harindra.chaudhary
 */
@XmlRootElement(name = "ara-classification-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class ARAClassificationDetails {
    /**
     * Unique identifier of the Classification generated by FileSecure.
     */
    @XmlElement(name = "fs-id")
    private String fsId;

    /**
     * Name of the classification in FileSecure
     */
    @XmlElement(name = "name")
    private String name;

    public String getFsId() {
        return fsId;
    }

    public void setFsId(String fsId) {
        this.fsId = fsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}