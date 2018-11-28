package hackathon.elibrary.POJO;

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

    @SerializedName("userLastName")
    private String userLastName;

    @SerializedName("userLogin")
    private String userLogin;

    @SerializedName("userFirstName")
    private String userFirstName;

    public Boolean getBanned() {
        return banned;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public Integer getId() {
        return id;
    }
}
