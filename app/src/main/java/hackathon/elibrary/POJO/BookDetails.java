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

   // public BookDetails() {}

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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setPath(Object path) {
        this.path = path;
    }

    public void setCoverPath(Object coverPath) {
        this.coverPath = coverPath;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setYearOfPublishing(Integer yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public void setProfileId(Object profileId) {
        this.profileId = profileId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Integer getId() {
        return id;
    }
}
