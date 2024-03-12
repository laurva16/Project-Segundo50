package co.edu.uptc.model;

import java.util.ArrayList;

public class Serie extends MultimediaContent {
    private ArrayList<Season> seasons;
    String category;


    public Serie(int id, String name, String author, String description, ArrayList<Season> seasons, String category, String coverImage) {
        super(id, name, author, description, coverImage);
        this.seasons = seasons;
        this.category = category;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    public void removeSeasons(Season season) {
        this.seasons.remove(season);
    }

    @Override
    public String toString() {
        return "Id:" + getId() + "  name: " + getName() + "\nAuthor: " + getAuthor() + "\nDescription: "
                + getDescription() + "Category: " + getCategory();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
