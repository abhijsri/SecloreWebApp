package com.seclore.sample.dms.core.master;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "rights-master") @XmlAccessorType(XmlAccessType.FIELD) public class AppRights
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(RightsMapAdapter.class) @XmlElement(name = "rights-list") private Map<String, Rights> rightsMap;

    public AppRights() {
        rightsMap = new LinkedHashMap<String, Rights>();
    }

    public Map<String, Rights> getRightsMap() {
        return rightsMap;
    }

    public void setRightsMap(Map<String, Rights> rightsMap) {
        this.rightsMap = rightsMap;
    }

}
