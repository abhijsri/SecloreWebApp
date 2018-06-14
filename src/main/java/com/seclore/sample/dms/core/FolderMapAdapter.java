package com.seclore.sample.dms.core;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.seclore.sample.dms.core.FolderMapAdapter.AdaptedFolderMap;

public class FolderMapAdapter extends XmlAdapter<AdaptedFolderMap, Map<String, AppFolder>> {

    public static class AdaptedFolderMap {
        @XmlElement(name = "app-folder") private Set<AppFolder> folderList = new LinkedHashSet<AppFolder>();
    }

    @Override public Map<String, AppFolder> unmarshal(AdaptedFolderMap adaptedFolderMap) throws Exception {
        Map<String, AppFolder> folderMap = new LinkedHashMap<String, AppFolder>();

        for (AppFolder appFolder : adaptedFolderMap.folderList) {
            String folderId = appFolder.getId();
            if (folderId != null && !folderId.trim().isEmpty()) {
                folderMap.put(folderId, appFolder);
            }
        }
        return folderMap;
    }

    @Override public AdaptedFolderMap marshal(Map<String, AppFolder> mapFolders) throws Exception {
        AdaptedFolderMap adaptedFolderMap = new AdaptedFolderMap();
        adaptedFolderMap.folderList = new LinkedHashSet<AppFolder>(mapFolders.values());
        return adaptedFolderMap;
    }

}
