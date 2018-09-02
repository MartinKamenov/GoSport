package com.kamenov.martin.gosportbg.models;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "settingsConfiguration")
public class SettingsConfiguration {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "mapType")
    private String mapType;

    public SettingsConfiguration() {

    }

    public SettingsConfiguration(String mapType) {
        this.mapType = mapType;
    }

    @Generated(hash = 96385911)
    public SettingsConfiguration(Long id, String mapType) {
        this.id = id;
        this.mapType = mapType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
