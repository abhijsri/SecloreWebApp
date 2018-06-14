package com.seclore.sample.dms.core;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "classifications") @XmlAccessorType(XmlAccessType.FIELD) public class ClassificationData
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "classification") private Set<Classification> classifications;

    public Set<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(Set<Classification> classifications) {
        this.classifications = classifications;
    }
}
