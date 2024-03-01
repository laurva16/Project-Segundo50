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

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.Season;

public class CreateSerie extends Application {
    private Scene Scene1, Scene2;
    private Stage primaryStage;
    private TextField text1 = new TextField();
    private TextField text2 = new TextField();
    private TextField text3 = new TextField();
    private TextField seasonField = new TextField();

    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private ChoiceBox<Season> choiceSeason = new ChoiceBox<>();
    private AdminController ac;
    private CategoryController categoryC;
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public CreateSerie() {
        ac = new AdminController();
        categoryC = new CategoryController();
        categoryC.getCategories().forEach(
                category -> choiceBox.getItems().add(category.getName()));

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        BorderPane root2 = new BorderPane();
        root2.setId("root2");

        GridPane gridPane = new GridPane();

        text1.setPrefWidth(300);
        text2.setPrefWidth(300);
        text3.setPrefWidth(300);

        Label labelName = new Label("Serie name:");
        Label labelDirector = new Label("Director name:");
        Label labelDescription = new Label("Description:");
        Label labelCategory = new Label("Category:");

        choiceBox.setMaxSize(300, 20);

        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelCategory, 0, 4);

        GridPane.setConstraints(text1, 1, 0);
        GridPane.setConstraints(text2, 1, 1);
        GridPane.setConstraints(text3, 1, 2);
        GridPane.setConstraints(choiceBox, 1, 4);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, text1, labelDirector, text2, labelDescription, text3, labelCategory,
                choiceBox);
        root2.setCenter(gridPane);

        root2.setStyle("-fx-background-color: #191919;");
        gridPane.setStyle("-fx-background-color: white;");

        // Save buttton
        Button acceptButton = new Button();

        GridPane.setConstraints(acceptButton, 0, 5);
        acceptButton.setTranslateY(100);
        acceptButton.setText("Next");
        acceptButton.setPrefWidth(150);
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);
        acceptButton.setOnAction(event -> addNewSerie());

        // Cancel buttton
        Button cancelButton = new Button();
        GridPane.setConstraints(cancelButton, 1, 5);
        cancelButton.setTranslateY(100);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);

        cancelButton.setOnAction(event -> cancelNewMovie());
        gridPane.getChildren().addAll(acceptButton, cancelButton);

        // Crear la escena
        Scene1 = new Scene(root2, screenWidth, screenHeight);
        // aplicar CSS
        Scene1.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");

        // Establecer la escena en la ventana
        primaryStage.setScene(Scene1);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("New Movie Scene");
        primaryStage.show();
    }

    private void formularySeason() {
        // Crear el formulario en la mitad izquierda
        VBox formPane = createFormPane();

        // Crear la tabla en la mitad derecha
        TableView<Movie> tablaMovie = createTable();

        // Dividir la ventana en dos partes con un SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(formPane, tablaMovie);
        splitPane.setDividerPositions(0.5); // Posición del divisor (50% de cada lado)

        // Crear la escena
        Scene2 = new Scene(splitPane, screenWidth, screenHeight);

        // Establecer la escena en la ventana
        primaryStage.setScene(Scene2);
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

        // HBox para el campo de texto adicional y el botón "Agregar"
        HBox seasonBox = new HBox();
        seasonBox.setAlignment(Pos.CENTER);
        seasonBox.setSpacing(10);

        seasonField.setPrefWidth(250);
        Button addButton = new Button("+");
        // Declara un observable list para almacenar las temporadas
        ObservableList<String> seasonsList = FXCollections.observableArrayList();

        // Añade el ChoiceBox y la lista observable de temporadas
        ChoiceBox<String> additionalOptions = new ChoiceBox<>(seasonsList);
        additionalOptions.setPrefWidth(150);

        // Función para agregar una temporada y actualizar el ChoiceBox
        addButton.setOnAction(event -> {
            String seasonName = seasonField.getText();
            ac.addSeason(ac.getCurrentSerie().getId(), seasonName, null);
            // Agrega la temporada a la lista observable
            seasonsList.add(seasonName);
        });

        // Añade los elementos a la vista
        seasonBox.getChildren().addAll(new Label("Name season:"), seasonField, addButton, additionalOptions);

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
        // Ventana de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText(null);
        alert.setContentText("¿Deseas guardar los cambios?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Crear un ArrayList vacío de temporadas
            ArrayList<Season> emptySeasons = new ArrayList<>();

            // Crear la serie con el ArrayList vacío de temporadas
            ac.addSerie(text1.getText(), text2.getText(), text3.getText(), emptySeasons, choiceBox.getValue());

            // Continuar con el formulario de las temporadas
            formularySeason();

            // Cambiar a la segunda escena
            primaryStage.setScene(Scene2);

            saved = true;
        }

        return saved;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
