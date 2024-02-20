package co.edu.uptc.model;

public class Movie extends MultimediaContent {
    String category;

    public Movie(int id, String name, String author, String description, int duration, String category) {
        super(id, name, duration, author, description);
        this.category = category;
    }

    @Override
    public String toString() {
        return "Name movie: " + getName() +
                "\nAuthor: " + getAuthor() +
                "\nDuration:  " + getDuration() +
                "\nDescription: " + getDescription() +
                "\nCategory: " + getCategory();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
