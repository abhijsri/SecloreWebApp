package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-environment-details")
@XmlAccessorType(XmlAccessType.FIELD)
/*@XmlType(name = "", propOrder = {
        "requestIpAddress"
})*/
public class AraEnvironmentDetails implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "request-ip-address", required = true)
    protected String requestIpAddress;

    public String getRequestIpAddress() {
        return requestIpAddress;
    }

    public void setRequestIpAddress(String requestIpAddress) {
        this.requestIpAddress = requestIpAddress;
    }
}
