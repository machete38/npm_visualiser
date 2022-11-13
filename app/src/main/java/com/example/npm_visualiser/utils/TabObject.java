package com.example.npm_visualiser.utils;

import java.util.ArrayList;

public class TabObject {
    public String name, version;
    public ArrayList<TabObject> list;

    public TabObject(String name, String version, ArrayList<TabObject> list) {
        this.name = name;
        this.version = version;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<TabObject> getList() {
        return list;
    }

    public void setList(ArrayList<TabObject> list) {
        this.list = list;
    }
}
