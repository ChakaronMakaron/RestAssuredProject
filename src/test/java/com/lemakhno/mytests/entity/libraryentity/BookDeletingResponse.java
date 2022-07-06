package com.lemakhno.mytests.entity.libraryentity;

public class BookDeletingResponse {
    
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BookDeletingResponse [msg=" + msg + "]";
    }
}
