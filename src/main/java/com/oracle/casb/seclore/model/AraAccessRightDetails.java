package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-access-right-details")
@XmlAccessorType(XmlAccessType.FIELD)
/*@XmlType(name = "", propOrder = {
        "primary-access-right",
        "offline-access-right"
})*/
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
