package co.edu.uptc.model;

import java.util.ArrayList;

public class Season {

    private String seasonName;
    private ArrayList<MultimediaContent> chapters;

    public Season(String seasonName, ArrayList<MultimediaContent> chapters) {

        this.seasonName = seasonName;
        this.chapters = chapters;
    }

    public ArrayList<MultimediaContent> getchapters() {
        return chapters;
    }

    public void setchapters(ArrayList<MultimediaContent> chapters) {
        this.chapters = chapters;
    }

    public void addchapters(MultimediaContent chapters) {
        this.chapters.add(chapters);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    @Override
    public String toString() {
        return "Season Name: " + seasonName;
    }

}
