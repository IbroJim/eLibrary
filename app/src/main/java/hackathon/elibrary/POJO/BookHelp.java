package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

public class BookHelp {
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


        public Integer getPages() {
            return pages;
        }

        public String getGenreName() {
            return genreName;
        }

        public String getAuthorFirstName() {
            return authorFirstName;
        }

        public String getAuthorLastName() {
            return authorLastName;
        }

        public Integer getYearOfPublishing() {
            return yearOfPublishing;
        }

        public String getTitle() {
            return title;
        }

        public Integer getId() {
            return id;
        }


}
