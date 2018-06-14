package com.seclore.sample.dms.core;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.seclore.sample.dms.core.FileMapAdapter.AdaptedFileMap;

public class FileMapAdapter extends XmlAdapter<AdaptedFileMap, Map<String, AppFile>> {

    public static class AdaptedFileMap {
        @XmlElement(name = "app-file")
        private Set<AppFile> fileList = new LinkedHashSet<AppFile>();
    }

    @Override
    public Map<String, AppFile> unmarshal(AdaptedFileMap adaptedFileMap) throws Exception {
        Map<String, AppFile> fileMap = new LinkedHashMap<String, AppFile>();
        for (AppFile appFile : adaptedFileMap.fileList) {
            String fileId = appFile.getId();
            if (fileId != null && !fileId.trim().isEmpty()) {
                fileMap.put(fileId, appFile);
            }
        }
        return fileMap;
    }

    @Override
    public AdaptedFileMap marshal(Map<String, AppFile> mapFiles) throws Exception {
        AdaptedFileMap adaptedFileMap = new AdaptedFileMap();
        adaptedFileMap.fileList = new LinkedHashSet<AppFile>(mapFiles.values());
        return adaptedFileMap;
    }
}  
	    

