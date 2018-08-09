package com.oracle.casb.seclore.model;

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
@XmlRootElement(name = "ara-client-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class AraClientDetails implements Serializable {
    private final static long serialVersionUID = 1L;
    @XmlElement(name = "client-number")
    @XmlSchemaType(name = "unsignedByte")
    protected short clientNumber;
    @XmlElement(name = "client-name", required = true)
    protected String clientName;

    public short getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(short clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
