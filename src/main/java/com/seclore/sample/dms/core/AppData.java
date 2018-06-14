package com.seclore.sample.dms.core;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "app-data")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(FolderMapAdapter.class)
    @XmlElement(name = "app-folders")
    private Map<String, AppFolder> folderMap;

    public AppData() {
        folderMap = new LinkedHashMap<String, AppFolder>();
    }

    public Map<String, AppFolder> getFolderMap() {
        return folderMap;
    }

    public void setFolderMap(Map<String, AppFolder> mapFolders) {
        this.folderMap = mapFolders;
    }

}
