package com.seclore.sample.dms.core.master;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.seclore.sample.dms.core.master.UserMapAdapter.AdaptedUser;

public class UserMapAdapter extends XmlAdapter<AdaptedUser, Map<String, User>> {

    public static class AdaptedUser {
        @XmlElement(name = "user") private Set<User> user = new LinkedHashSet<User>();
    }

    @Override public Map<String, User> unmarshal(AdaptedUser adaptedUser) throws Exception {
        Map<String, User> userMap = new LinkedHashMap<String, User>();
        for (User user : adaptedUser.user) {
            String id = user.getId();
            if (id != null && !id.trim().isEmpty()) {
                userMap.put(id, user);
            }
        }
        return userMap;
    }

    @Override public AdaptedUser marshal(Map<String, User> map) throws Exception {
        AdaptedUser adaptedUser = new AdaptedUser();
        adaptedUser.user = new LinkedHashSet<User>(map.values());
        return adaptedUser;
    }

}
