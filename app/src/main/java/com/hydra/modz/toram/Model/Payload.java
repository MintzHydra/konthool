package com.hydra.modz.toram.Model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload implements Serializable
{

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("expiry")
    @Expose
    private String expiry;
    @SerializedName("expired")
    @Expose
    private boolean expired;
    private final static long serialVersionUID = 3504539230924682233L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}