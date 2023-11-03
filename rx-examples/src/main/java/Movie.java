public class Movie {

    private final int index;

    private final String title;

    private final String description;

    private final int releaseYear;

    private final int length;

    private final String rating;

    public Movie(int index, String title, String description, int releaseYear, int length, String rating) {
        this.index = index;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.length = length;
        this.rating = rating;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getLength() {
        return length;
    }

    public String getRating() {
        return rating;
    }

    @Override public String toString() {
        return String.format("index=%d, title=%s, description=%s, releaseYear=%d, length=%d, rating=%s", index, title, description, releaseYear,
                length, rating);
    }
}
