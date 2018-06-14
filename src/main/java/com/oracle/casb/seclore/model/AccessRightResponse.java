package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.*;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-response-get-access-right")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "araResponseHeader",
        "araResponseDetailsGetAccessRight"
})
public class AccessRightResponse {

    private final static long serialVersionUID = 1L;

    @XmlElement(name = "ara-response-header", required = true)
    protected AraHeader araResponseHeader;
    @XmlElement(name = "ara-response-details-get-access-right", required = true)
    protected AraResponseDetailsGetAccessRight araResponseDetailsGetAccessRight;
    @XmlAttribute(name = "type", required = true)
    @XmlSchemaType(name = "unsignedByte")
    protected short type;

    public AraHeader getAraResponseHeader() {
        return araResponseHeader;
    }

    public void setAraResponseHeader(AraHeader araResponseHeader) {
        this.araResponseHeader = araResponseHeader;
    }

    public AraResponseDetailsGetAccessRight getAraResponseDetailsGetAccessRight() {
        return araResponseDetailsGetAccessRight;
    }

    public void setAraResponseDetailsGetAccessRight(AraResponseDetailsGetAccessRight araResponseDetailsGetAccessRight) {
        this.araResponseDetailsGetAccessRight = araResponseDetailsGetAccessRight;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
