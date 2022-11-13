package com.example.npm_visualiser.utils;

import java.util.ArrayList;

public class ParseObject {
    String name, version;
    ArrayList<String> strings;

    public ParseObject(String name, String version, ArrayList<String> strings) {
        this.name = name;
        this.version = version;
        this.strings = strings;
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

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }
}
