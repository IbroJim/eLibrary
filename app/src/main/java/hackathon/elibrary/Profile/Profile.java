package hackathon.elibrary.Profile;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("id")
    private Integer id;

    @SerializedName("trusted")
    private Boolean trusted;
    @SerializedName("banned")
    private Boolean banned;

    @SerializedName("userId")
    private Integer userId;

    public Integer getId() {
        return id;
    }
}
