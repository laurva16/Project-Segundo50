package co.edu.uptc.model;

public class MultimediaContent {
    private int id;
    private String name;
    private int duration;
    private String author;
    private String description;
    private String fileVideo;
    private String coverImage;

    public MultimediaContent() {
    }

    public MultimediaContent(int id, String name, String author, String description, String coverImage) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.coverImage = coverImage;
    }

    // Movie

    public MultimediaContent(int id, String name, int duration, String author, String description) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.author = author;
        this.description = description;
    }

    // Chapters
    public MultimediaContent(int id, int duration, String name, String description) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
    }

    public MultimediaContent(int id, String name, int duration, String description, String fileVideo,
            String coverImage) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.fileVideo = fileVideo;
        this.coverImage = coverImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "name: " + name + "\nDescription: " + description + "\nDuration: " + duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileVideo() {
        return fileVideo;
    }

    public void setFileVideo(String fileVideo) {
        this.fileVideo = fileVideo;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

}
