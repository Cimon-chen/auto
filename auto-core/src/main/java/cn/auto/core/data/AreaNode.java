package cn.auto.core.data;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by chenmeng on 2019/10/28.
 */
public class AreaNode {
    private Integer areaId;
    private Integer pAreaId;
    private SimpleStringProperty name = new SimpleStringProperty();

    public AreaNode(Integer areaId, String name, Integer pAreaId) {
        setAreaId(areaId);
        setName(name);
        setpAreaId(pAreaId);
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getpAreaId() {
        return pAreaId;
    }

    public void setpAreaId(Integer pAreaId) {
        this.pAreaId = pAreaId;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return "AreaNode{" +
                "areaId=" + getAreaId() +
                ", pAreaId=" + getpAreaId() +
                ", name=" + getName() +
                '}';
    }
}
