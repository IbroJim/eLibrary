package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

public class BookDetails {
    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("pages")
    private Integer pages;

    @SerializedName("approved")
    private Boolean approved;

    @SerializedName("path")
    private Object path;

    @SerializedName("coverPath")
    private Object coverPath;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy;

    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;

    @SerializedName("yearOfPublishing")
    private Integer yearOfPublishing;

    @SerializedName("authorFirstName")
    private String authorFirstName;

    @SerializedName("authorLastName")
    private String authorLastName;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getYearOfPublishing() {
        return yearOfPublishing;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    @SerializedName("profileId")
    private Object profileId;

    @SerializedName("genreId")
    private Integer genreId;

    @SerializedName("genreName")
    private String genreName;

    public String getGenreName() {
        return genreName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Integer getId() {
        return id;
    }
}
