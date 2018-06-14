package com.seclore.sample.dms.core;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "app-folder")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppFolder implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private String id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "irm-enabled")
    private Boolean irmEnabled;

    @XmlElement(name = "htmlwrap-enabled")
    private Boolean htmlWrapEnabled;

    @XmlJavaTypeAdapter(FileMapAdapter.class)
    @XmlElement(name = "app-files")
    private Map<String, AppFile> fileMap;

    public AppFolder() {
        fileMap = new LinkedHashMap<String, AppFile>();
        irmEnabled = false;
    }

    public Map<String, AppFile> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, AppFile> fileMap) {
        this.fileMap = fileMap;
    }

	/*@XmlElementWrapper(name="app-files")
	@XmlElement(name="app-file")
	private Set<AppFile> fileList;
	
	public Set<AppFile> getFileList() {
		return fileList;
	}

	public void setFileList(Set<AppFile> fileList) {
		this.fileList = fileList;
	}*/

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

    public Boolean getHtmlWrapEnabled() {
        return htmlWrapEnabled;
    }

    public void setHtmlWrapEnabled(Boolean htmlWrapEnabled) {
        this.htmlWrapEnabled = htmlWrapEnabled;
    }

    public Boolean getIrmEnabled() {
        return irmEnabled;
    }

    public void setIrmEnabled(Boolean irmEnabled) {
        this.irmEnabled = irmEnabled;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AppFolder other = (AppFolder) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
