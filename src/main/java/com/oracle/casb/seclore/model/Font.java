package com.oracle.casb.seclore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created By : abhijsri
 * Date  : 15/06/18
 **/

@XmlRootElement(name = "font")
@XmlAccessorType(XmlAccessType.FIELD)
public class Font {

    @XmlElement(name = "face")
    private String face;
    @XmlElement(name = "bold")
    private boolean bold;
    @XmlElement(name = "italic")
    private boolean italic;

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }
}
