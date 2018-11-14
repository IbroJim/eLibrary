package hackathon.elibrary.Account;

import com.google.gson.annotations.SerializedName;

public class LogIn {

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;





    public LogIn(String username, String password) {
        this.username = username;
        this.password = password;

    }
}
