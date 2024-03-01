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
import javafx.scene.layout.HBox;
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
    private TextField seasonField = new TextField();

    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private AdminController ac;
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public CreateSerie() {
        ac = new AdminController();
    }

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

        // GridPane para el formulario principal
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Labels y campos de texto del formulario principal
        Label labelName = new Label("Movie name:");
        Label labelDirector = new Label("Director name:");
        Label labelDescription = new Label("Description:");
        Label labelCategory = new Label("Category:");

        text1.setPrefWidth(300);
        text2.setPrefWidth(300);

        choiceBox.setMaxSize(300, 20);

        gridPane.add(labelName, 0, 0);
        gridPane.add(labelDirector, 0, 1);
        gridPane.add(labelDescription, 0, 2);
        gridPane.add(labelCategory, 0, 4);

        gridPane.add(text1, 1, 0);
        gridPane.add(text2, 1, 1);
        gridPane.add(text3, 1, 2);
        gridPane.add(choiceBox, 1, 4);

        // HBox para el campo de texto adicional y el botón "Agregar"
        HBox seasonBox = new HBox();
        seasonBox.setAlignment(Pos.CENTER);
        seasonBox.setSpacing(10);

        seasonField.setPrefWidth(250);
        Button addButton = new Button("+");

        seasonBox.getChildren().addAll(new Label("Name season:"), seasonField, addButton);

        // ChoiceBox para los botones adicionales
        ChoiceBox<String> additionalOptions = new ChoiceBox<>();
        additionalOptions.getItems().addAll("Option 1", "Option 2");
        additionalOptions.setPrefWidth(150);

        // Botones adicionales
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");

        button1.setPrefWidth(100);
        button2.setPrefWidth(100);

        // HBox para los botones adicionales
        HBox additionalButtons = new HBox(10, additionalOptions, button1, button2);
        additionalButtons.setAlignment(Pos.CENTER);

        // Botones "Guardar" y "Cancelar"
        Button acceptButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        acceptButton.setPrefWidth(150);
        cancelButton.setPrefWidth(150);

        acceptButton.setOnAction(event -> addNewSerie());
        cancelButton.setOnAction(event -> cancelNewMovie());

        // HBox para los botones
        HBox buttonPane = new HBox(10, acceptButton, cancelButton);
        buttonPane.setAlignment(Pos.CENTER);

        // Agregar todos los elementos al VBox principal
        formPane.getChildren().addAll(gridPane, seasonBox, additionalButtons, buttonPane);

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

    private boolean addNewSerie() {
        Boolean saved = false;
        // ventana de confirmacion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText(null);
        alert.setContentText("You want to save to changes?");
        ac.addSerie(text1.getText(), text2.getText(), text3.getText(),
                ac.createSeasons(ac.getCurrentSerie().getId(), seasonField.getText(), null), STYLESHEET_CASPIAN);

        return saved;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
