package cintlex.mvd;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("Enable regional difficulty maxing")
    public boolean configregional = true;

    @SerializedName("Enable clamped regional difficulty maxing")
    public boolean configclampedregional = true;

    public Config() {
    }
}