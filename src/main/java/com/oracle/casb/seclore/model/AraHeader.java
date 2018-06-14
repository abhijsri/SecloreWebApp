package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class AraHeader implements Serializable {

    private final static long serialVersionUID = 1L;

    @XmlElement(name = "protocol-version")
    @XmlSchemaType(name = "unsignedByte")
    protected short protocolVersion;
    @XmlElement(name = "request-id", required = true)
    protected String requestId;

    public short getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(short protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
