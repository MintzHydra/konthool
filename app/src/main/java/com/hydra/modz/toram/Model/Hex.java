package com.hydra.modz.toram.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hex implements Serializable
{

    @SerializedName("arm")
    @Expose
    private List<Object> arm = null;
    @SerializedName("thumb")
    @Expose
    private List<Object> thumb = null;
    @SerializedName("arm64")
    @Expose
    private List<Object> arm64 = null;
    private final static long serialVersionUID = -1623031004309118998L;

    public List<Object> getArm() {
        return arm;
    }

    public void setArm(List<Object> arm) {
        this.arm = arm;
    }

    public List<Object> getThumb() {
        return thumb;
    }

    public void setThumb(List<Object> thumb) {
        this.thumb = thumb;
    }

    public List<Object> getArm64() {
        return arm64;
    }

    public void setArm64(List<Object> arm64) {
        this.arm64 = arm64;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Hex.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("arm");
        sb.append('=');
        sb.append(((this.arm == null)?"<null>":this.arm));
        sb.append(',');
        sb.append("thumb");
        sb.append('=');
        sb.append(((this.thumb == null)?"<null>":this.thumb));
        sb.append(',');
        sb.append("arm64");
        sb.append('=');
        sb.append(((this.arm64 == null)?"<null>":this.arm64));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}