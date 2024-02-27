package co.edu.uptc.model;

import java.util.ArrayList;

public class Season {

    private int id;
    private String seasonName;
    private ArrayList<MultimediaContent> chapters;

    public Season(int id, String seasonName, ArrayList<MultimediaContent> chapters) {
        this.id = id;
        this.seasonName = seasonName;
        this.chapters = chapters;
    }

    public ArrayList<MultimediaContent> getchapters() {
        return chapters;
    }

    public void setchapters(ArrayList<MultimediaContent> chapters) {
        this.chapters = chapters;
    }

    public void addchapters(MultimediaContent chapter) {
        this.chapters.add(chapter);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Season [id=" + id + ", seasonName=" + seasonName + "]";
    }
}
