package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created By : abhijsri
 * Date  : 09/06/18
 **/
@XmlRootElement(name = "ara-file-usage-details")
@XmlAccessorType(XmlAccessType.FIELD)
/*@XmlType(name = "", propOrder = {
        "firstAccessTime"
})*/
public class AraFileUsageDetails implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "first-access-time", required = true)
    protected String firstAccessTime;

    public String getFirstAccessTime() {
        return firstAccessTime;
    }

    public void setFirstAccessTime(String firstAccessTime) {
        this.firstAccessTime = firstAccessTime;
    }
}
