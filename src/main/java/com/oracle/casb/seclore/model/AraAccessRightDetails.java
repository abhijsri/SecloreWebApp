package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-access-right-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class AraAccessRightDetails implements Serializable {
    private final static long serialVersionUID = 1L;
    @XmlElement(name = "primary-access-right")
    @XmlSchemaType(name = "unsignedByte")
    @JacksonXmlProperty(localName = "primary-access-right")
    protected short primaryAccessRight;
    @XmlElement(name = "offline-access-right")
    @JacksonXmlProperty(localName = "offline-access-right")
    protected boolean offlineAccessRight;

    public short getPrimaryAccessRight() {
        return primaryAccessRight;
    }

    public void setPrimaryAccessRight(short primaryAccessRight) {
        this.primaryAccessRight = primaryAccessRight;
    }

    public boolean isOfflineAccessRight() {
        return offlineAccessRight;
    }

    public void setOfflineAccessRight(boolean offlineAccessRight) {
        this.offlineAccessRight = offlineAccessRight;
    }
}
