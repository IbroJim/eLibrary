package hackathon.elibrary.Book;

import com.google.gson.annotations.SerializedName;

public class CreateBook {
    @SerializedName("approved")
    private Boolean approved;
    @SerializedName("authorFirstName")
    private String authorFirstName;
    @SerializedName("authorLastName")
    private String authorLastName;
    @SerializedName("coverPath")
    private String coverPath;
    @SerializedName("createdBy")
    private String createdBy;
    @SerializedName("createdDate")
    private String createdDate;
    @SerializedName("description")
    private String description;
    @SerializedName("genreId")
    private Integer genreId;
    @SerializedName("genreName")
    private String genreName;
    @SerializedName("id")
    private Integer id;
    @SerializedName("lastModifiedBy")
    private String lastModifiedBy;
    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;
    @SerializedName("pages")
    private Integer pages;
    @SerializedName("path")
    private String path;
    @SerializedName("profileId")
    private Integer profileId;
    @SerializedName("title")
    private String title;
    @SerializedName("yearOfPublishing")
    private Integer yearOfPublishing;

    public CreateBook(Boolean approved, String authorFirstName, String authorLastName, String description, Integer genreId, Integer pages, Integer profileId, String title, Integer yearOfPublishing) {
        this.approved = approved;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.description = description;
        this.genreId = genreId;
        this.pages = pages;
        this.profileId = profileId;
        this.title = title;
        this.yearOfPublishing = yearOfPublishing;
    }

    public Integer getId() {
        return id;
    }
}
