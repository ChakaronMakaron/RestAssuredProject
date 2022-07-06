package com.lemakhno.mytests.entity.serializationbigbody;

public class Location {
    
    private float lat;
    private float lng;
    
    public float getLat() {
        return lat;
    }
    public void setLat(float lat) {
        this.lat = lat;
    }
    public float getLng() {
        return lng;
    }
    public void setLng(float lng) {
        this.lng = lng;
    }
    @Override
    public String toString() {
        return "Coordinates [lat=" + lat + ", lng=" + lng + "]";
    }
}
