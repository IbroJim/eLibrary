package hackathon.elibrary.Account;

import com.google.gson.annotations.SerializedName;

public class User {

    private String email;
    private String lastName;
    private String firstName;
    private String login;
    private String password;
    @SerializedName("langKey")
    private String langKey;
    private Integer id;

    public User(String email, String lastName, String firstName, String login, String password,String langKey) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.password = password;
        this.langKey=langKey;
    }

    public Integer getId() {
        return id;
    }

    public void setRu(String ru) {
        this.langKey = ru;
    }

    public String getFirstName() {
        return firstName;
    }
}
