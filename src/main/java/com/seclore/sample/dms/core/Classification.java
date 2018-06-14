package com.seclore.sample.dms.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "classification") @XmlAccessorType(XmlAccessType.FIELD) public class Classification
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true) private String id;

    @XmlElement(name = "name") private String name;

    @XmlElement(name = "description") private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Classification other = (Classification) obj;
        if (id == null && other.id != null) {
            return false;
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
