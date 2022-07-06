package com.lemakhno.mytests.entity.libraryentity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookCreationResponse {
    
    @JsonProperty("Msg")
    private String msg;

    @JsonProperty("ID")
    private String id;
    
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "BookCreationResponse [id=" + id + ", msg=" + msg + "]";
    }
}
