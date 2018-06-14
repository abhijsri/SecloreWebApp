package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-client-details")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "clientNumber",
        "clientName"
})
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
