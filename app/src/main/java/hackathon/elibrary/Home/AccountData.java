package hackathon.elibrary.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.http.GET;

public class AccountData {


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
    public Boolean getActivated() {
        return activated;
    }

    public AccountData() {
    }

    public AccountData(Integer id){
        this.id=id;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
