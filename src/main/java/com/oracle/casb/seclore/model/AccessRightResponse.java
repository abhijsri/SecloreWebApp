package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-response-get-access-right")
@XmlAccessorType(XmlAccessType.FIELD)

public class AccessRightResponse {

    private final static long serialVersionUID = 1L;

    @XmlElement(name = "ara-response-header", required = true)
    @JacksonXmlProperty(localName = "ara-response-header")
    protected AraHeader araResponseHeader;
    @XmlElement(name = "ara-response-details-get-access-right", required = true)
    @JacksonXmlProperty(localName = "ara-response-details-get-access-right")
    protected AraResponseDetailsGetAccessRight araResponseDetailsGetAccessRight;
    @XmlAttribute(name = "type", required = true)
    @XmlSchemaType(name = "unsignedByte")
    @JacksonXmlProperty(localName = "type")
    protected short type;

    @JacksonXmlProperty(localName = "ara-response-header")
    public AraHeader getAraResponseHeader() {
        return araResponseHeader;
    }

    public void setAraResponseHeader(AraHeader araResponseHeader) {
        this.araResponseHeader = araResponseHeader;
    }

    @JacksonXmlProperty(localName = "ara-response-details-get-access-right")
    public AraResponseDetailsGetAccessRight getAraResponseDetailsGetAccessRight() {
        return araResponseDetailsGetAccessRight;
    }

    public void setAraResponseDetailsGetAccessRight(AraResponseDetailsGetAccessRight araResponseDetailsGetAccessRight) {
        this.araResponseDetailsGetAccessRight = araResponseDetailsGetAccessRight;
    }

    @JacksonXmlProperty(localName = "type")
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
