package co.edu.uptc.run;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;

import co.edu.uptc.controller.AdminController;

public class DisplayMultimediaScreen {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private String filePath = "src/multimediaVideos/";
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    
    private Stage primaryStage;
    public DisplayMultimediaScreen(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public Scene multimediaScene(String nameFile) {
        String videoFile = filePath + nameFile + ".mp4";

        BorderPane root = new BorderPane(); 
      
        Media media = new Media(new File(videoFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);

        // Buttons
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button rewindButton = new Button("<< 5s");
        Button fastForwardButton = new Button("5s >>");
        Button homeButton = new Button("Home");

        playButton.setOnAction(event -> mediaPlayer.play());
        pauseButton.setOnAction(event -> mediaPlayer.pause());
        stopButton.setOnAction(event -> mediaPlayer.stop());

        rewindButton.setOnAction(event -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(javafx.util.Duration.seconds(5)));
        });

        fastForwardButton.setOnAction(event -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(5)));
        });

        homeButton.setOnAction(event -> {
            mediaPlayer.stop();
            returnScene();
        });

        playButton.setPrefWidth(100);
        pauseButton.setPrefWidth(100);
        stopButton.setPrefWidth(100);
        rewindButton.setPrefWidth(100);
        fastForwardButton.setPrefWidth(100);
        homeButton.setPrefWidth(125);
        
        ImageView homeIcon = new ImageView(new Image("file:" + "src/prograIconos/home2.png"));
        homeIcon.setFitWidth(22);
        homeIcon.setFitHeight(22);
        homeButton.setGraphic(homeIcon);
        //

        HBox buttonBox = new HBox(homeButton, playButton, pauseButton, stopButton, rewindButton, fastForwardButton);
        buttonBox.setSpacing(80);
        buttonBox.setMaxHeight(70);
        buttonBox.setAlignment(Pos.CENTER);
        
        //ajuste de tamano
        mediaView.setFitHeight(600);//650
        mediaView.setFitHeight(600);
        //

        BorderPane.setMargin(buttonBox, new Insets(0, 0, 0, 0));
        BorderPane.setAlignment(mediaView, Pos.TOP_CENTER);
        root.setTop(mediaView);
        root.setCenter(buttonBox);
        
        // CSS

        Scene displayScene = new Scene(root, screenWidth, screenHeight);
        displayScene.getStylesheets().add(new File("src/main/java/co/styles/display.css").toURI().toString());
        playButton.setId("button");
        stopButton.setId("button");
        pauseButton.setId("button");
        rewindButton.setId("button");
        fastForwardButton.setId("button");
        root.setId("root");
        buttonBox.setId("hbox");
        homeButton.setId("home-button");

        return displayScene;
    }

    private void returnScene() {
        EntryWindow main = new EntryWindow();
        main.getScene1();
    }

}
