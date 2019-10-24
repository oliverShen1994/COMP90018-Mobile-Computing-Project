package com.android.group_12.crushy;

import android.net.Uri;

public class PersonalInfo {
    private Uri imageUri;
    private String name;
    public PersonalInfo(String imageURI, String name){
        this.imageUri = Uri.parse(imageURI);
        this.name = name;
    }

    public void setImageId(Uri imageUri) {
        this.imageUri = imageUri;
    }
    public void setName(String name){
        this.name = name;
    }
    public Uri getImageId(){
        return this.imageUri;
    }
    public String getName(){
        return this.name;
    }

}
