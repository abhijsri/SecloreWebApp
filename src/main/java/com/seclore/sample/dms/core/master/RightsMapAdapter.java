package com.seclore.sample.dms.core.master;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.seclore.sample.dms.core.master.RightsMapAdapter.AdaptedRights;

public class RightsMapAdapter extends XmlAdapter<AdaptedRights, Map<Integer, Rights>> {

    public static class AdaptedRights {
        @XmlElement(name = "rights") private Set<Rights> rights = new LinkedHashSet<Rights>();
    }

    @Override public Map<Integer, Rights> unmarshal(AdaptedRights adaptedRights) throws Exception {
        Map<Integer, Rights> rightsMap = new LinkedHashMap<Integer, Rights>();

        for (Rights rights : adaptedRights.rights) {
            Integer id = rights.getId();
            if (id != null) {
                rightsMap.put(id, rights);
            }
        }
        return rightsMap;
    }

    @Override public AdaptedRights marshal(Map<Integer, Rights> map) throws Exception {
        AdaptedRights adaptedRights = new AdaptedRights();
        adaptedRights.rights = new LinkedHashSet<Rights>(map.values());
        return adaptedRights;
    }

}
