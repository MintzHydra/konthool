package com.hydra.modz.toram.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("messages")
    @Expose
    private List<Object> messages = null;
    @SerializedName("payload")
    @Expose
    private Payload payload;
    private final static long serialVersionUID = 1451494177952448196L;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Object> getMessages() {
        return messages;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

}