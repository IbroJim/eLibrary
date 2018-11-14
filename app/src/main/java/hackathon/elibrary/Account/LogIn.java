package hackathon.elibrary.Account;

import com.google.gson.annotations.SerializedName;

public class LogIn {

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("id_token")
    private String id_token;





    public LogIn(String username, String password) {
        this.username = username;
        this.password = password;
        }

    public String getId_token() {
        return id_token;
    }
}
