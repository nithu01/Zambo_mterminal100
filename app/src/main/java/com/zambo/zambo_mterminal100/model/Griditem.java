package com.zambo.zambo_mterminal100.model;

public class Griditem {

//    String[] name;
  //  Integer[] image;

    String name;

    int image;

    public Griditem(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
