package com.oracle.casb.seclore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By : abhijsri
 * Date  : 15/06/18
 **/
@XmlRootElement(name = "ara-watermark-details")
@XmlAccessorType(XmlAccessType.FIELD)
public class AraWaterMarkDetails {

    @XmlElementWrapper(name = "line")
    @XmlElement(name = "lines")
    @JacksonXmlProperty(localName = "lines")
    private List<String> lines = new ArrayList<>();

    @XmlElement(name = "font")
    @JacksonXmlProperty(localName = "font")
    private Font font;


    @XmlElement(name = "color")
    @JacksonXmlProperty(localName = "color")


    private String  color;
    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
