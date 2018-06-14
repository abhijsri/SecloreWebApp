package com.seclore.sample.dms.core.master;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "user-master") @XmlAccessorType(XmlAccessType.FIELD) public class AppUsers
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(UserMapAdapter.class) @XmlElement(name = "users") private Map<String, User> userMap;

    public AppUsers() {
        userMap = new LinkedHashMap<String, User>();
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

}
