package co.edu.uptc.run;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.util.Duration;
import java.io.File;

public class DisplayMultimediaScreen {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private String filePath = "src/multimediaVideos/";
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public Scene multimediaScene(String nameFile) {
        String videoFile = filePath + nameFile + ".mp4";
        VBox root = new VBox();

        // Crear un objeto Media
        Media media = new Media(new File(videoFile).toURI().toString());

        // Crear un objeto MediaPlayer
        mediaPlayer = new MediaPlayer(media);

        // Crear un objeto MediaView y asociarlo al MediaPlayer
        mediaView = new MediaView(mediaPlayer);

        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button rewindButton = new Button("<< 5s");
        Button fastForwardButton = new Button("5s >>");
        Button homeButton = new Button("Home");

        
        // Buttons actions
        playButton.setOnAction(event -> mediaPlayer.play());
        pauseButton.setOnAction(event -> mediaPlayer.pause());
        stopButton.setOnAction(event-> mediaPlayer.stop());

        rewindButton.setOnAction(event -> {
            Duration currentTime = mediaPlayer.getCurrentTime();
            mediaPlayer.seek(currentTime.subtract(Duration.seconds(5)));
        });

        fastForwardButton.setOnAction(event -> {
            Duration currentTime = mediaPlayer.getCurrentTime();
            mediaPlayer.seek(currentTime.add(Duration.seconds(5)));
        });
        homeButton.setOnAction(event ->{
            mediaPlayer.stop();
            returnScene();
        });
        //

        playButton.setPrefWidth(100);
        pauseButton.setPrefWidth(100);
        stopButton.setPrefWidth(100);
        rewindButton.setPrefWidth(100);
        fastForwardButton.setPrefWidth(100);
        homeButton.setPrefWidth(125);

        ImageView homeIcon = new ImageView(new Image("file:" + "src\\prograIconos\\home2.png"));
        homeIcon.setFitWidth(22);
        homeIcon.setFitHeight(22);
        homeButton.setGraphic(homeIcon);

        HBox buttonBox = new HBox(homeButton, playButton, pauseButton, stopButton, rewindButton, fastForwardButton);
        buttonBox.setSpacing(80);
        buttonBox.setPrefHeight(60);
        buttonBox.setAlignment(Pos.CENTER); 
        
        root.setAlignment(Pos.CENTER);
        Scene displayScene = new Scene(root, screenWidth, screenHeight);

        root.getChildren().setAll(mediaView, buttonBox);

        //BorderPane.setMargin(buttonBox, new Insets(-25, 0, 0, 0));
        //BorderPane.setAlignment(mediaView, Pos.TOP_CENTER);

        // CSS
        displayScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\display.css").toURI().toString());
        playButton.setId("button");
        stopButton.setId("button");
        pauseButton.setId("button");
        rewindButton.setId("button");
        fastForwardButton.setId("button");
        root.setId("vbox");
        buttonBox.setId("hbox");
        homeButton.setId("home-button");

        return displayScene;
    }

    private void returnScene() {
        EntryWindow main = new EntryWindow();
        main.getScene1();
    }

}
