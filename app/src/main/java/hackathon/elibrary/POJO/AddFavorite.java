package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

public class AddFavorite {

    @SerializedName("bookId")
    private Integer bookId;

    @SerializedName("id")
    private Integer id;

    @SerializedName("profileId")
    private Integer profileId;

    public AddFavorite(Integer bookId, Integer profileId) {
        this.bookId = bookId;
        this.profileId = profileId;
    }

    public Integer getId() {
        return id;
    }
}
