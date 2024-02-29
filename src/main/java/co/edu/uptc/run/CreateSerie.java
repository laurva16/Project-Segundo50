package co.edu.uptc.run;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import java.util.Optional;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.model.Movie;

public class CreateSerie extends Application {
    private Scene movieScene;
    private TextField text1 = new TextField();
    private TextField text2 = new TextField();
    private TextField text3 = new TextField();
    private TextField text4 = new TextField();
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private AdminController adminC;
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    @Override
    public void start(Stage primaryStage) {
        // Crear el formulario en la mitad izquierda
        VBox formPane = createFormPane();

        // Crear la tabla en la mitad derecha
        TableView<Movie> tablaMovie = createTable();

        // Dividir la ventana en dos partes con un SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(formPane, tablaMovie);
        splitPane.setDividerPositions(0.5); // Posición del divisor (50% de cada lado)

        // Crear la escena
        Scene newMovieScene = new Scene(splitPane, screenWidth, screenHeight);

        // Establecer la escena en la ventana
        primaryStage.setScene(newMovieScene);
        primaryStage.setTitle("New Movie Scene");
        primaryStage.show();
    }

    private VBox createFormPane() {
        VBox formPane = new VBox();
        formPane.setAlignment(Pos.CENTER);
        formPane.setSpacing(20);
        formPane.setPadding(new Insets(20));

        Label labelName = new Label("Movie name:");
        Label labelDirector = new Label("Director name:");
        Label labelDescription = new Label("Description:");
        Label labelDuration = new Label("Duration:");
        Label labelCategory = new Label("Category:");

        text1.setPrefWidth(300);
        text2.setPrefWidth(300);
        text3.setPrefWidth(300);
        text4.setPrefWidth(300);

        choiceBox.setMaxSize(300, 20);

        Button acceptButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        formPane.getChildren().addAll(
                labelName, text1, labelDirector, text2, labelDescription, text3, labelDuration,
                text4, labelCategory, choiceBox, acceptButton, cancelButton);

        acceptButton.setOnAction(event -> addNewMovie());
        cancelButton.setOnAction(event -> cancelNewMovie());

        return formPane;
    }

    private TableView<Movie> createTable() {
        TableView<Movie> tablaMovie = new TableView<>();
        tablaMovie.setMaxWidth(600); // Establecer ancho máximo para la tabla

        TableColumn<Movie, String> IdColumn = new TableColumn<>("Id");
        TableColumn<Movie, String> facultyColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> nombreGrupoColumn = new TableColumn<>("Director");

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nombreGrupoColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Agregar columna de botones
        TableColumn<Movie, Void> accionesColumna = new TableColumn<>("Actions");
        // accionesColumna.setCellFactory(param -> new BotonCelda());
        tablaMovie.getColumns().addAll(IdColumn, facultyColumn, nombreGrupoColumn, accionesColumna);

        return tablaMovie;
    }

    private void cancelNewMovie() {
        // primaryStage.setScene(movieScene);
    }

    private boolean addNewMovie() {
        Boolean saved = false;
        // ventana de confirmacion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText(null);
        alert.setContentText("You want to save to changes?");

        return saved;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
