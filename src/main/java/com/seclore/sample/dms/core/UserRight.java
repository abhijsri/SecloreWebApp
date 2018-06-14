package com.seclore.sample.dms.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user-right")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserRight
        implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement(name = "user-id")
    private String userId;

    @XmlElement(name = "usage-rights")
    private Integer usageRights;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String pUserId) {
        this.userId = pUserId;
    }

    public Integer getUsageRights() {
        return usageRights;
    }

    public void setUsageRights(Integer usageRights) {
        this.usageRights = usageRights;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserRight other = (UserRight) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
