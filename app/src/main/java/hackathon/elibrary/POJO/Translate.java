package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Translate {
    @SerializedName("key")
    private String key;

    @SerializedName("value")
    private List<String> value = null;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }


}
