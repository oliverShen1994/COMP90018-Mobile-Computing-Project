package com.android.group_12.crushy;

public class PersonalInfo {
    private int imageId;
    private String name;
    public PersonalInfo(int image, String name){
        this.imageId = image;
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getImageId(){
        return this.imageId;
    }
    public String getName(){
        return this.name;
    }

}
