package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-request-get-access-right")
@XmlAccessorType(XmlAccessType.FIELD)

public class AccessRightRequest implements Serializable {
    private final static long serialVersionUID = 1L;

    @XmlElement(name = "ara-request-header", required = true)
    protected AraHeader araHeader;

    @XmlElement(name = "ara-request-details-get-access-right", required = true)
    protected AraRequestDetailsGetAccessRight araRequestDetailsGetAccessRight;

    @XmlAttribute(name = "type", required = true)
    @XmlSchemaType(name = "unsignedByte")
    protected short type;

    public AraHeader getAraHeader() {
        return araHeader;
    }

    public void setAraHeader(AraHeader araHeader) {
        this.araHeader = araHeader;
    }

    public AraRequestDetailsGetAccessRight getAraRequestDetailsGetAccessRight() {
        return araRequestDetailsGetAccessRight;
    }

    public void setAraRequestDetailsGetAccessRight(AraRequestDetailsGetAccessRight araRequestDetailsGetAccessRight) {
        this.araRequestDetailsGetAccessRight = araRequestDetailsGetAccessRight;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
