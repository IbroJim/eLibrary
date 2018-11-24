package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("activated")
    private Boolean activated;

    @SerializedName("authorities")
    private List<String> authorities = null;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("id")
    private Integer id;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("langKey")
    private String langKey;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy;

    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    public User(boolean activated,String email, String lastName, String firstName, String login, String password,String langKey) {
        this.activated=activated;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.password = password;
        this.langKey=langKey;
    }

}
