package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

public class Favorite {

    @SerializedName("bookId")
    private Integer bookId;

    @SerializedName("id")
    private Integer id;

    @SerializedName("profileId")
    private Integer profileId;

    public Favorite(Integer bookId, Integer profileId) {
        this.bookId = bookId;
        this.profileId = profileId;
    }

    public Integer getId() {
        return id;
    }
}
