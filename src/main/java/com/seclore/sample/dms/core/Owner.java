package com.seclore.sample.dms.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "owner") @XmlAccessorType(XmlAccessType.FIELD) public class Owner implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "email-id") private String emailId;

    @XmlElement(name = "name") private String name;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public int hashCode() {
        int i = (emailId == null ? 0 : emailId.hashCode());
        final int prime = 31;
        int result = 1;
        result = prime * result + i;
        return result;
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Owner other = (Owner) obj;
        if (emailId == null && other.emailId != null) {
            return false;
        } else if (!emailId.equals(other.emailId)) {
            return false;
        }
        return true;
    }
}
