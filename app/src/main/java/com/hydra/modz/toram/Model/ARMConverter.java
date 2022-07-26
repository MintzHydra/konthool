
package com.hydra.modz.toram.Model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ARMConverter implements Serializable
{

    @SerializedName("hex")
    @Expose
    private Hex hex;
    @SerializedName("counter")
    @Expose
    private int counter;
    private final static long serialVersionUID = -1662914744654121404L;

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ARMConverter.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("hex");
        sb.append('=');
        sb.append(((this.hex == null)?"<null>":this.hex));
        sb.append(',');
        sb.append("counter");
        sb.append('=');
        sb.append(this.counter);
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
