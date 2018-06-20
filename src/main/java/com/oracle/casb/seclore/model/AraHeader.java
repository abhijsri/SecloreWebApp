package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

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
    @JacksonXmlProperty(localName = "protocol-version")
    protected short protocolVersion;
    @XmlElement(name = "request-id", required = true)
    @JacksonXmlProperty(localName = "request-id")
    protected String requestId;

    @XmlElement(name = "error-message")
    @JacksonXmlProperty(localName = "error-message")
    protected String errorMessage;

    @XmlElement(name = "display-message")
    @JacksonXmlProperty(localName = "display-message")
    protected String displayMessage;

    @XmlElement(name = "status")
    @XmlSchemaType(name = "unsignedByte")
    @JacksonXmlProperty(localName = "status")
    protected short status;

    public short getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(short protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @JacksonXmlProperty(localName = "request-id")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
