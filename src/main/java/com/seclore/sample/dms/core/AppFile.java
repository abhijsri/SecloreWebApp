package com.seclore.sample.dms.core;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "app-file")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private String id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "owner")
    private Owner owner;

    @XmlElement(name = "classification")
    private Classification classification;

    @XmlElement(name = "content-type")
    private String contentType;

    @XmlElementWrapper(name = "users-rights")
    @XmlElement(name = "user-right")
    private Set<UserRight> usersRights;

    public AppFile() {
        usersRights = new LinkedHashSet<UserRight>();
        classification = null;
        owner = null;
    }

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Set<UserRight> getUserRightList() {
        return usersRights;
    }

    public void setUserRightList(Set<UserRight> pUsersRights) {
        if (pUsersRights == null) {
            pUsersRights = new LinkedHashSet<UserRight>();
        }
        this.usersRights = pUsersRights;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        AppFile other = (AppFile) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
