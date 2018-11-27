package hackathon.elibrary.Util;

public class BookUtils {
    private Integer id;
    private String title;
    private String lastAutor;
    private String firstAutor;
    private String genre;

    public BookUtils(Integer id, String title,String firstAutor, String lastAutor,  String genre) {
        this.id = id;
        this.title = title;
        this.firstAutor = firstAutor;
        this.lastAutor = lastAutor;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getLastAutor() {
        return lastAutor;
    }

    public String getFirstAutor() {
        return firstAutor;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getId() {
        return id;
    }
}
