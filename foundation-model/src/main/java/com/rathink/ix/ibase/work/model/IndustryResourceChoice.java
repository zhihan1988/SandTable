package com.rathink.ix.ibase.work.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
@Entity
@Table(name = "industry_resource_choice")
public class IndustryResourceChoice {
    private String id;
    private String serial;
    private String name;
    private String value;
    private String value2;
    private String img;
    private String type;//提供给子类使用的类型字段 每个类型有不同的定义
    private IndustryResource industryResource;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "serial")
    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "value2")
    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @Column(name = "img")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "industry_resource_id")
    public IndustryResource getIndustryResource() {
        return industryResource;
    }

    public void setIndustryResource(IndustryResource industryResource) {
        this.industryResource = industryResource;
    }
}
