package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

public class FavoriteBookList {

    @SerializedName("bookId")
    private Integer bookId;

    @SerializedName("id")
    private Integer id;

    @SerializedName("profileId")
    private Integer profileId;
}
