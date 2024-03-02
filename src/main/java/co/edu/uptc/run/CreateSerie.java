package co.edu.uptc.run;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;

public class CreateSerie extends Application {
    private Scene Scene1, Scene2, Scene3;
    private Stage primaryStage;
    private TextField text1 = new TextField();
    private TextField text2 = new TextField();
    private TextField text3 = new TextField();
    private TextField textNameChapter = new TextField();
    private TextField textDurationChapter = new TextField();
    private TextField textDescriptionChapter = new TextField();
    private TextField seasonField = new TextField();
    TableView<MultimediaContent> tablaMovie = new TableView<>();
    // Declara un observable list para almacenar las temporadas
    ObservableList<String> seasonsList = FXCollections.observableArrayList();

    // Añade el ChoiceBox y la lista observable de temporadas
    ChoiceBox<String> additionalOptions = new ChoiceBox<>(seasonsList);

    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
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
        primaryStage.setTitle("New Serie Scene");
        primaryStage.show();
    }

    private void formularySeason() {
        // Crear el formulario en la mitad izquierda
        VBox formPane = createFormPane();

        // Crear la tabla en la mitad derecha
        TableView<MultimediaContent> tablaMovie = createTable();

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

        additionalOptions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Actualizar la tabla con los capítulos de la nueva temporada seleccionada
            if (newValue != null) {
                refreshChapterTable(newValue);
            }
        });

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
        ImageView iconoAddSeason = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));

        iconoAddSeason.setFitWidth(20);
        iconoAddSeason.setFitHeight(20);

        Button addButton = new Button();

        addButton.setGraphic(iconoAddSeason);

        additionalOptions.setPrefWidth(250);

        // Añadir temporadas al ChoiceBox de temporadas (cambia este código según la
        // fuente de tus temporadas)
        ArrayList<Season> seasonList = new ArrayList<>(); // Supongamos que tienes una lista de temporadas
        for (Season season : seasonList) {
            additionalOptions.getItems().add(season.getSeasonName());
        }

        // Función para agregar una temporada y actualizar el ChoiceBox
        addButton.setOnAction(event -> {
            String seasonName = seasonField.getText();
            ac.addSeason(ac.getCurrentSerie().getId(), seasonName, null);
            // Agrega la temporada a la lista observable
            seasonsList.add(seasonName);
            seasonField.clear();
        });

        // Añade los elementos a la vista
        seasonBox.getChildren().addAll(new Label("Name season:"), seasonField, addButton, additionalOptions);

        // Botones adicionales

        ImageView iconoDeleteSeason = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        ImageView iconoModifySeason = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));

        iconoDeleteSeason.setFitWidth(16);
        iconoDeleteSeason.setFitHeight(16);

        iconoModifySeason.setFitWidth(16);
        iconoModifySeason.setFitHeight(16);

        Button buttonDeleteSeason = new Button();

        buttonDeleteSeason.setGraphic(iconoDeleteSeason);
        Button buttonModifySeason = new Button();
        buttonModifySeason.setGraphic(iconoModifySeason);

        buttonDeleteSeason.setOnAction(event -> {
            String selectedSeason = additionalOptions.getValue(); // Obtener la temporada seleccionada
            if (selectedSeason != null) {
                // Mostrar un cuadro de diálogo de confirmación
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Delete Season");
                confirmationDialog.setContentText("Are you sure you want to delete this season?");

                // Obtener la respuesta del usuario
                confirmationDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Eliminar la temporada del ChoiceBox y de la lista observable
                        ac.deleteSeasonName(selectedSeason, ac.getCurrentSerie().getId());
                        additionalOptions.getItems().remove(selectedSeason);
                        seasonsList.remove(selectedSeason);

                        // Después de eliminar, selecciona la primera temporada (o cualquier otra
                        // lógica)
                        if (!additionalOptions.getItems().isEmpty()) {
                            additionalOptions.getSelectionModel().selectFirst();
                        }
                    }
                });
            }
        });

        buttonDeleteSeason.setPrefWidth(50);
        buttonModifySeason.setPrefWidth(50);

        // HBox para los botones adicionales
        HBox additionalButtons = new HBox(10, additionalOptions, buttonDeleteSeason, buttonModifySeason);
        additionalButtons.setAlignment(Pos.CENTER);

        // Botones "Guardar" y "Cancelar"
        Button acceptButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        buttonModifySeason.setOnAction(event -> {
            String selectedSeason = additionalOptions.getValue(); // Obtener la temporada seleccionada
            if (selectedSeason != null) {
                // Crear un cuadro de diálogo para modificar la temporada
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Modify Season Name");

                // Crear el contenido del cuadro de diálogo
                VBox dialogContent = new VBox();
                dialogContent.setSpacing(10);

                // Campo de texto para introducir el nuevo nombre de la temporada
                TextField newNameField = new TextField();
                newNameField.setPromptText("New Season Name");

                // Mensaje de confirmación
                Label confirmationLabel = new Label("Are you sure you want to make these changes?");

                dialogContent.getChildren().addAll(new Label("Change the name of the season '" + selectedSeason + "'"),
                        newNameField,
                        confirmationLabel);

                dialog.getDialogPane().setContent(dialogContent);

                // Botones de OK y Cancelar
                ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton2);

                // Manejar la acción del botón OK
                dialog.setResultConverter(buttonType -> {
                    if (buttonType == okButton) {
                        return newNameField.getText();
                    }
                    return null;
                });

                // Mostrar el diálogo y manejar la respuesta
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newName -> {
                    if (!newName.isEmpty()) {
                        // Actualizar la temporada con el nuevo nombre
                        ac.modifySeasonName(newName, ac.getCurrentSerie().getId(), selectedSeason);

                        // Actualizar el nombre de la temporada en la lista observable
                        int selectedIndex = additionalOptions.getItems().indexOf(selectedSeason);
                        seasonsList.set(selectedIndex, newName);

                        // Actualizar el ChoiceBox con la lista actualizada
                        additionalOptions.setItems(seasonsList);

                        // Volver a seleccionar la temporada modificada
                        additionalOptions.getSelectionModel().select(newName);
                    }
                });
            }
        });

        acceptButton.setPrefWidth(150);
        cancelButton.setPrefWidth(150);

        cancelButton.setOnAction(event -> cancelNewMovie());

        // HBox para los botones
        HBox buttonPane = new HBox(10, acceptButton, cancelButton);
        buttonPane.setAlignment(Pos.CENTER);

        Button addChapterButton = new Button("Add Chapter");
        addChapterButton.setOnAction(event -> {
            formularyChapter();
        });
        addChapterButton.setPrefWidth(370);

        // VBox para el botón "Add Chapter"
        VBox addChapterBox = new VBox(addChapterButton);
        addChapterBox.setAlignment(Pos.CENTER);

        // Agregar todos los elementos al VBox principal
        formPane.getChildren().addAll(gridPane, seasonBox, additionalButtons, addChapterBox, buttonPane);

        return formPane;
    }

    private TableView<MultimediaContent> createTable() {

        // Crear las columnas de la tabla
        TableColumn<MultimediaContent, String> IdChapterColumn = new TableColumn<>("Id Chapter");
        TableColumn<MultimediaContent, String> NameChapterColumn = new TableColumn<>("Name Chapter");
        TableColumn<MultimediaContent, Void> accionesColumna = new TableColumn<>("Actions");

        // Agregar las columnas a la tabla
        tablaMovie.getColumns().addAll(IdChapterColumn, NameChapterColumn, accionesColumna);

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

    private void formularyChapter() {
        BorderPane root3 = new BorderPane();
        root3.setId("root2");

        GridPane gridPane = new GridPane();

        text1.setPrefWidth(300);
        text2.setPrefWidth(300);
        text3.setPrefWidth(300);

        Label labelName = new Label("Chapter name:");
        Label labelDuration = new Label("Duration:");
        Label labelDescription = new Label("Description:");

        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDuration, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);

        GridPane.setConstraints(textNameChapter, 1, 0);
        GridPane.setConstraints(textDurationChapter, 1, 1);
        GridPane.setConstraints(textDescriptionChapter, 1, 2);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, textNameChapter, labelDuration, textDurationChapter, labelDescription,
                textDescriptionChapter);
        root3.setCenter(gridPane);

        root3.setStyle("-fx-background-color: #191919;");
        gridPane.setStyle("-fx-background-color: white;");

        // Save buttton
        Button acceptButton = new Button();

        GridPane.setConstraints(acceptButton, 0, 5);
        acceptButton.setTranslateY(100);
        acceptButton.setText("Accept");
        acceptButton.setPrefWidth(150);
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);
        String selectedSeason = additionalOptions.getValue();
        acceptButton.setOnAction(event -> {
            // Obtener la temporada seleccionada del ChoiceBox
            String selectedSeasonName = additionalOptions.getValue();
            Season selectedSeasonObj = null;

            // Buscar la temporada seleccionada en la lista de temporadas
            for (Season season : ac.getCurrentSerie().getSeasons()) {
                if (season.getSeasonName().equals(selectedSeasonName)) {
                    selectedSeasonObj = season;
                    break;
                }
            }

            // Verificar si se encontró la temporada seleccionada
            if (selectedSeasonObj != null) {
                // Agregar el nuevo capítulo a la temporada seleccionada
                ac.addChapterName(textNameChapter.getText(), textDescriptionChapter.getText(),
                        Integer.parseInt(textDurationChapter.getText()), ac.getCurrentSerie().getId(),
                        selectedSeasonName);

                // Actualizar la tabla con los capítulos de la temporada seleccionada
                refreshChapterTable(selectedSeasonName);
                cambiarAEscena1();
            }

            // Limpiar los campos de texto del capítulo
            textNameChapter.clear();
            textDurationChapter.clear();
            textDescriptionChapter.clear();
        });

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
        Scene3 = new Scene(root3, screenWidth, screenHeight);
        // aplicar CSS
        Scene3.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");

        // Establecer la escena en la ventana
        primaryStage.setScene(Scene3);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("New chapter Scene");
        primaryStage.show();

    }

    private void cambiarAEscena1() {
        primaryStage.setScene(Scene2);
    }

    private void refreshChapterTable(String selectedSeasonName) {
        // Limpiar la tabla
        tablaMovie.getItems().clear();
        tablaMovie.getColumns().clear();

        // Crear las columnas de la tabla
        TableColumn<MultimediaContent, String> IdChapterColumn = new TableColumn<>("Id Chapter");
        TableColumn<MultimediaContent, String> NameChapterColumn = new TableColumn<>("Name Chapter");
        TableColumn<MultimediaContent, Void> accionesColumna = new TableColumn<>("Actions");

        // Agregar las columnas a la tabla
        tablaMovie.getColumns().addAll(IdChapterColumn, NameChapterColumn, accionesColumna);

        // Obtener los capítulos de la temporada seleccionada
        ArrayList<MultimediaContent> chapters = ac.getCurrentSerie().getSeasons()
                .get(ac.seasonNameFound(selectedSeasonName, ac.getCurrentSerie().getId())).getchapters();

        // Crear una lista observable de capítulos
        ObservableList<MultimediaContent> chapterList = FXCollections.observableArrayList(chapters);

        // Asignar los valores a las celdas de la tabla
        IdChapterColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameChapterColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Agregar los datos a la tabla
        tablaMovie.setItems(chapterList);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
