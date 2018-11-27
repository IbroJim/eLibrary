package hackathon.elibrary.POJO;

import com.google.gson.annotations.SerializedName;

public class FavoriteBook {

    @SerializedName ( "bookAuthorFirstName" )
    private String bookAuthorFirstName ;

    @SerializedName ( "bookAuthorLastName" )
    private String bookAuthorLastName ;

    @SerializedName ( "bookCoverPath" )
    private String bookCoverPath ;

    @SerializedName ( "bookCreatedBy" )
    private String bookCreatedBy ;

    @SerializedName ( "bookDescription" )
    private String bookDescription ;

    @SerializedName( "bookGenreName" )
    private String bookGenreName ;

    @SerializedName( "bookId" )
    private int bookId ;

    @SerializedName ( "bookPages" )
    private int bookPages ;

    @SerializedName ( "bookPath" )
    private String bookPath ;

    @SerializedName ( "bookTitle" )
    private String bookTitle ;

    @SerializedName ( "bookYearOfPublishing" )
    private int bookYearOfPublishing ;

    @SerializedName ( "id" )
    private int id ;


    @SerializedName ( "profileId" )
    private int profileId ;

    public String getBookAuthorFirstName() {
        return bookAuthorFirstName;
    }

    public String getBookAuthorLastName() {
        return bookAuthorLastName;
    }

    public String getBookCoverPath() {
        return bookCoverPath;
    }

    public String getBookCreatedBy() {
        return bookCreatedBy;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public String getBookGenreName() {
        return bookGenreName;
    }

    public int getBookId() {
        return bookId;
    }

    public int getBookPages() {
        return bookPages;
    }

    public String getBookPath() {
        return bookPath;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getBookYearOfPublishing() {
        return bookYearOfPublishing;
    }

    public int getId() {
        return id;
    }

    public int getProfileId() {
        return profileId;
    }
}
