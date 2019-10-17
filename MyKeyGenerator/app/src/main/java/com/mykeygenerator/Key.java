package com.mykeygenerator;


import androidx.annotation.NonNull;

public class Key {
    String id,name,key;

    public Key(String id, String name, String key) {
        super();
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return this.id+" "+this.name+" "+this.key;
    }
}

