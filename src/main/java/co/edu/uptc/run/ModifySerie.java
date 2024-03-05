package co.edu.uptc.run;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Screen;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;

public class ModifySerie {
    private Scene Scene2, Scene3, Scene4, Scene5, SceneReturn;
    private Scene newSerieScene;
    private Stage primaryStage;
    private TextField text1 = new TextField();
    private TextField text2 = new TextField();
    private TextField text3 = new TextField();
    private TextField textNameChapter = new TextField();
    private TextField textDurationChapter = new TextField();
    private TextField textDescriptionChapter = new TextField();
    private TextField seasonField = new TextField();
    TableView<MultimediaContent> tablaChapters = new TableView<>();
    ObservableList<String> seasonsList = FXCollections.observableArrayList();
    ObservableList<String> multimediaContentList = FXCollections.observableArrayList();
    ArrayList<MultimediaContent> chapterList = new ArrayList<>();
    Serie serieModify;
    ChoiceBox<String> additionalOptions = new ChoiceBox<>(seasonsList);
    ChoiceBox<String> additionalOptionsMultimediaContent = new ChoiceBox<>(multimediaContentList);

    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private AdminController ac;
    private CategoryController categoryC;
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    public ModifySerie(Stage primaryStage, AdminController adminC) {
        this.primaryStage = primaryStage;
        ac = new AdminController();
        categoryC = new CategoryController();
        categoryC.getCategories().forEach(
                category -> choiceBox.getItems().add(category.getName()));

    }

    public void llamarEntryWindowSerie() {
        EntryWindow entryWindow = new EntryWindow();
        entryWindow.entryWindowSerie();
    }

    public Scene newSerieScene(Serie serie) {
        serieModify = serie;
        TextField textModify1 = new TextField(serie.getName());
        TextField textModify2 = new TextField(serie.getAuthor());
        TextField textModify3 = new TextField(serie.getDescription());
        BorderPane root2 = new BorderPane();
        root2.setId("root2");

        GridPane gridPane = new GridPane();

        textModify1.setPrefWidth(300);
        textModify2.setPrefWidth(300);
        textModify3.setPrefWidth(300);

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

        GridPane.setConstraints(textModify1, 1, 0);
        GridPane.setConstraints(textModify2, 1, 1);
        GridPane.setConstraints(textModify3, 1, 2);
        GridPane.setConstraints(choiceBox, 1, 4);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, textModify1, labelDirector, textModify2, labelDescription, textModify3,
                labelCategory,
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
        acceptButton.setOnAction(event -> {
            // Modificar la serie actual con los datos del formulario
            ac.modifySeries(textModify3.getText(), textModify1.getText(), textModify2.getText(),
                    serie.getId());

            formularySeason();
        });

        // Cancel buttton
        Button cancelButton = new Button();
        GridPane.setConstraints(cancelButton, 1, 5);
        cancelButton.setTranslateY(100);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);

        cancelButton.setOnAction(event -> {
            llamarEntryWindowSerie();
        });

        gridPane.getChildren().addAll(acceptButton, cancelButton);

        // Crear la escena
        newSerieScene = new Scene(root2, screenWidth, screenHeight);
        // aplicar CSS
        newSerieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");

        primaryStage.setTitle("New Serie Scene");

        return newSerieScene;

    }

    private void formularySeason() {
        // Crear el formulario
        VBox formPane = createFormPane();
        formPane.getStyleClass().add("formPane");

        // Botón de retorno
        ImageView iconoReturn = new ImageView(new Image("file:" + "src\\prograIconos\\volver.png"));
        iconoReturn.setFitWidth(24);
        iconoReturn.setFitHeight(24);

        Button returnButton = new Button();

        returnButton.setGraphic(iconoReturn);
        returnButton.getStyleClass().add("boton-return");
        returnButton.setOnAction(event -> {
            SceneReturn = newSerieScene(serieModify);
            primaryStage.setScene(SceneReturn);
        });

        returnButton.setPrefWidth(100);

        // Crear un BorderPane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #191919;");

        // Colocar el botón de retorno en la esquina superior izquierda
        BorderPane.setAlignment(returnButton, Pos.TOP_LEFT);
        root.setTop(returnButton);

        // Colocar el formulario en el centro
        root.setCenter(formPane);

        // Crear la escena
        Scene2 = new Scene(root, screenWidth, screenHeight);
        Scene2.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());

        // Establecer la escena en la ventana
        primaryStage.setScene(Scene2);
        primaryStage.setTitle("New Movie Scene");
        primaryStage.show();
    }

    private VBox createFormPane() {
        System.out.println(serieModify.toString());
        VBox formPane = new VBox();
        formPane.setAlignment(Pos.CENTER);
        formPane.setSpacing(20);
        formPane.setPadding(new Insets(20));

        formPane.setMaxWidth(500); // Establecer un ancho máximo
        formPane.setMaxHeight(400); // Establecer una altura máxima
        formPane.setMinWidth(500); // Establecer un ancho mínimo
        formPane.setMinHeight(400); // Establecer una altura mínima

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
        addButton.getStyleClass().add("boton-add");

        additionalOptions.setPrefWidth(250);
        additionalOptionsMultimediaContent.setPrefWidth(250);

        for (Season season : serieModify.getSeasons()) {
            additionalOptions.getItems().add(season.getSeasonName());
        }

        // Listener para actualizar los episodios cuando se selecciona una temporada
        additionalOptions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Obtener la temporada seleccionada
                Season selectedSeason = serieModify.getSeasons().stream()
                        .filter(season -> season.getSeasonName().equals(newValue))
                        .findFirst()
                        .orElse(null);
                if (selectedSeason != null && selectedSeason.getchapters() != null) {
                    multimediaContentList.clear();
                    for (MultimediaContent chapter : selectedSeason.getchapters()) {
                        multimediaContentList.add(chapter.getName());
                    }
                } else {
                    multimediaContentList.clear();
                }
            }
        });

        addButton.setOnAction(event -> {
            String seasonName = seasonField.getText();
            ac.addSeason(serieModify.getId(), seasonName, null);
            seasonsList.add(seasonName);
            seasonField.clear();
        });

        // Añade los elementos a la vista
        seasonBox.getChildren().addAll(new Label("Name season:"), seasonField, addButton, additionalOptions);

        ImageView iconoDeleteSeason = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        ImageView iconoModifySeason = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));
        ImageView iconoDeleteChapter = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        ImageView iconoModifyChapter = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));

        iconoDeleteSeason.setFitWidth(16);
        iconoDeleteSeason.setFitHeight(16);

        iconoModifySeason.setFitWidth(16);
        iconoModifySeason.setFitHeight(16);

        iconoDeleteChapter.setFitWidth(16);
        iconoDeleteChapter.setFitHeight(16);

        iconoModifyChapter.setFitWidth(16);
        iconoModifyChapter.setFitHeight(16);

        Button buttonDeleteSeason = new Button();

        buttonDeleteSeason.setGraphic(iconoDeleteSeason);
        buttonDeleteSeason.getStyleClass().add("boton-delete");
        Button buttonModifySeason = new Button();
        buttonModifySeason.setGraphic(iconoModifySeason);
        buttonModifySeason.getStyleClass().add("boton-modify");

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
                        ac.deleteSeasonName(selectedSeason, serieModify.getId());
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

        Button buttonDeleteChapter = new Button();
        buttonDeleteChapter.setGraphic(iconoDeleteChapter);
        buttonDeleteChapter.getStyleClass().add("boton-delete");

        buttonDeleteChapter.setOnAction(event -> {
            String selectedSeasonName = additionalOptions.getValue();
            String selectedChapterName = additionalOptionsMultimediaContent.getValue();
            if (selectedSeasonName != null && selectedChapterName != null) {
                // Mostrar un cuadro de diálogo de confirmación
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Delete Chapter");
                confirmationDialog.setContentText("Are you sure you want to delete this chapter?");

                // Obtener la respuesta del usuario
                confirmationDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Eliminar el capítulo de la temporada seleccionada
                        ac.deleteChapterName(selectedSeasonName, serieModify.getId(), selectedChapterName);

                        // Actualizar la lista observable de capítulos
                        additionalOptionsMultimediaContent.getItems().remove(selectedChapterName);

                        // Limpiar la lista de capítulos local
                        chapterList.removeIf(chapter -> chapter.getName().equals(selectedChapterName));

                        // Después de eliminar, selecciona la primera temporada (o cualquier otra
                        // lógica)
                        if (!additionalOptionsMultimediaContent.getItems().isEmpty()) {
                            additionalOptionsMultimediaContent.getSelectionModel().selectFirst();
                        }
                    }
                });
            }
        });

        Button buttonModifyChapter = new Button();
        buttonModifyChapter.setGraphic(iconoModifyChapter);
        buttonModifyChapter.getStyleClass().add("boton-modify");

        buttonModifyChapter.setOnAction(event -> {
            String selectedSeasonName = additionalOptions.getValue();
            String selectedChapterName = additionalOptionsMultimediaContent.getValue();
            modifyChapter(selectedSeasonName, selectedChapterName);

        });

        buttonDeleteChapter.setPrefWidth(50);
        buttonModifyChapter.setPrefWidth(50);

        // HBox para los botones adicionales
        HBox additionalButtonsChapters = new HBox(10, additionalOptionsMultimediaContent, buttonDeleteChapter,
                buttonModifyChapter);
        additionalButtonsChapters.setAlignment(Pos.CENTER);

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
                        ac.modifySeasonName(newName, serieModify.getId(), selectedSeason);

                        // Actualizar el nombre de la temporada en la ntSerie()lista observable
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

        acceptButton.setOnAction(event -> {

            // Crear y mostrar la ventana emergente
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("Se ha modificado correctamente la serie.");
            alert.showAndWait();

            llamarEntryWindowSerie();

        });

        cancelButton.setOnAction(event -> {
            // Crear una ventana de confirmación
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmación");
            confirmationDialog.setHeaderText(null);
            confirmationDialog.setContentText("¿Está seguro de que desea cancelar? Se eliminará toda la serie.");

            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    // Problema

                    llamarEntryWindowSerie();
                }
            });
        });

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

        Button viewChapterButton = new Button("View Chapters");

        viewChapterButton.setOnAction(event -> {
            String selectedSeasonName = additionalOptions.getValue();
            if (selectedSeasonName != null) {
                tableChapters(selectedSeasonName);
            } else {
                System.out.println("");
            }
        });

        viewChapterButton.setPrefWidth(370);
        // VBox para el botón "Add Chapter"
        VBox viewChapterBox = new VBox(viewChapterButton);
        viewChapterBox.setAlignment(Pos.CENTER);

        // Agregar todos los elementos al VBox principal
        formPane.getChildren().addAll(seasonBox, additionalButtons, addChapterBox, additionalButtonsChapters,
                viewChapterBox,
                buttonPane);
        cancelButton.setId("button");
        acceptButton.setId("button");

        return formPane;
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
        acceptButton.setOnAction(event -> {
            // Obtener la temporada seleccionada del ChoiceBox
            String selectedSeasonName = additionalOptions.getValue();
            Season selectedSeasonObj = null;

            // Buscar la temporada seleccionada en la lista de temporadas
            for (Season season : serieModify.getSeasons()) {
                if (season.getSeasonName().equals(selectedSeasonName)) {
                    selectedSeasonObj = season;
                    break;
                }
            }

            // Verificar si se encontró la temporada seleccionada
            if (selectedSeasonObj != null) {
                // Agregar el nuevo capítulo a la temporada seleccionada
                ac.addChapterName(textNameChapter.getText(), textDescriptionChapter.getText(),
                        Integer.parseInt(textDurationChapter.getText()), serieModify.getId(),
                        selectedSeasonName);

                // Actualizar el ChoiceBox de capítulos con los capítulos de la temporada
                // seleccionada
                multimediaContentList.clear();
                for (MultimediaContent chapter : selectedSeasonObj.getchapters()) {
                    multimediaContentList.add(chapter.getName());
                }
            }

            cambiarAEscena1();

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

        cancelButton.setOnAction(event -> formularySeason());
        gridPane.getChildren().addAll(acceptButton, cancelButton);

        // Crear la escena
        Scene3 = new Scene(root3, screenWidth, screenHeight);
        // aplicar CSS
        Scene3.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());
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

    private void modifyChapter(String selectedSeason, String selectedChapter) {
        BorderPane root3 = new BorderPane();

        TextField textNameChapterModify = new TextField(serieModify.getSeasons()
                .get(ac.seasonNameFound(selectedSeason, serieModify.getId())).getchapters()
                .get(ac.chapterNameFound(selectedSeason, serieModify.getId(), selectedChapter)).getName());
        TextField textDurationChapterModify = new TextField(String.valueOf(serieModify.getSeasons()
                .get(ac.seasonNameFound(selectedSeason, serieModify.getId())).getchapters()
                .get(ac.chapterNameFound(selectedSeason, serieModify.getId(), selectedChapter))
                .getDuration()));
        TextField textDescriptionChapterModify = new TextField(serieModify.getSeasons()
                .get(ac.seasonNameFound(selectedSeason, serieModify.getId())).getchapters()
                .get(ac.chapterNameFound(selectedSeason, serieModify.getId(), selectedChapter))
                .getDescription());

        root3.setId("root2");

        GridPane gridPane = new GridPane();

        textNameChapterModify.setPrefWidth(300);
        textDurationChapterModify.setPrefWidth(300);
        textDescriptionChapterModify.setPrefWidth(300);

        Label labelName = new Label("Chapter name:");
        Label labelDuration = new Label("Duration:");
        Label labelDescription = new Label("Description:");

        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDuration, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);

        GridPane.setConstraints(textNameChapterModify, 1, 0);
        GridPane.setConstraints(textDurationChapterModify, 1, 1);
        GridPane.setConstraints(textDescriptionChapterModify, 1, 2);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, textNameChapterModify, labelDuration, textDurationChapterModify,
                labelDescription,
                textDescriptionChapterModify);
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
        acceptButton.setOnAction(event -> {
            // Obtener la temporada seleccionada del ChoiceBox
            String selectedSeasonName = additionalOptions.getValue();
            Season selectedSeasonObj = null;

            // Buscar la temporada seleccionada en la lista de temporadas
            for (Season season : serieModify.getSeasons()) {
                if (season.getSeasonName().equals(selectedSeasonName)) {
                    selectedSeasonObj = season;
                    break;
                }
            }

            // Verificar si se encontró la temporada seleccionada
            if (selectedSeasonObj != null) {
                // Agregar el nuevo capítulo a la temporada seleccionada

                int duration = 0;
                if (!textDurationChapterModify.getText().isEmpty()) {
                    duration = Integer.parseInt(textDurationChapterModify.getText());
                }
                ac.modifyChaptersName(textDescriptionChapterModify.getText(), textNameChapterModify.getText(),
                        duration, serieModify.getId(), selectedSeason,
                        selectedChapter);

                // Actualizar el ChoiceBox de capítulos con los capítulos de la temporada
                // seleccionada
                multimediaContentList.clear();
                for (MultimediaContent chapter : selectedSeasonObj.getchapters()) {
                    multimediaContentList.add(chapter.getName());
                }
            }

            cambiarAEscena1();

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

        cancelButton.setOnAction(event -> {
            formularySeason();
        });
        gridPane.getChildren().addAll(acceptButton, cancelButton);

        // Crear la escena
        Scene4 = new Scene(root3, screenWidth, screenHeight);
        // aplicar CSS
        Scene4.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");

        // Establecer la escena en la ventana
        primaryStage.setScene(Scene4);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("New chapter Scene");
        primaryStage.show();

    }

    private void modifySerie() {
        TextField textModify1 = new TextField(ac.getCurrentSerie().getName());
        TextField textModify2 = new TextField(ac.getCurrentSerie().getAuthor());
        TextField textModify3 = new TextField(ac.getCurrentSerie().getDescription());
        BorderPane root2 = new BorderPane();
        root2.setId("root2");

        GridPane gridPane = new GridPane();

        textModify1.setPrefWidth(300);
        textModify2.setPrefWidth(300);
        textModify3.setPrefWidth(300);

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

        GridPane.setConstraints(textModify1, 1, 0);
        GridPane.setConstraints(textModify2, 1, 1);
        GridPane.setConstraints(textModify3, 1, 2);
        GridPane.setConstraints(choiceBox, 1, 4);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, textModify1, labelDirector, textModify2, labelDescription, textModify3,
                labelCategory,
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
        acceptButton.setOnAction(event -> {
            // Modificar la serie actual con los datos del formulario
            ac.modifySeries(textModify3.getText(), textModify1.getText(), textModify2.getText(),
                    ac.getCurrentSerie().getId());

            // Cambiar a la escena anterior
            formularySeason();
        });

        // Cancel buttton
        Button cancelButton = new Button();
        GridPane.setConstraints(cancelButton, 1, 5);
        cancelButton.setTranslateY(100);
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.RIGHT);

        cancelButton.setOnAction(event -> {
            ac.deleteSerie(ac.getCurrentSerie().getId());
            llamarEntryWindowSerie();
        });

        gridPane.getChildren().addAll(acceptButton, cancelButton);

        // Crear la escena
        Scene5 = new Scene(root2, screenWidth, screenHeight);
        // aplicar CSS
        Scene5.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");

        // Establecer la escena en la ventana
        primaryStage.setScene(Scene5);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("New Serie Scene");
        primaryStage.show();

    }

    private void tableChapters(String selectedSeasonName) {
        Season selectedSeason = serieModify.getSeasons()
                .get(ac.seasonNameFound(selectedSeasonName, serieModify.getId()));

        if (selectedSeason != null && selectedSeason.getchapters() != null && !selectedSeason.getchapters().isEmpty()) {
            // Si hay capítulos en la temporada, mostrar la tabla como antes
            ObservableList<MultimediaContent> chapters = FXCollections
                    .observableArrayList(selectedSeason.getchapters());

            TableView<MultimediaContent> tableView = new TableView<>(chapters);

            TableColumn<MultimediaContent, String> idColumn = new TableColumn<>("Id");
            TableColumn<MultimediaContent, String> nameColumn = new TableColumn<>("Name");
            TableColumn<MultimediaContent, String> durationColumn = new TableColumn<>("Duration");

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

            tableView.getColumns().addAll(idColumn, nameColumn, durationColumn);

            // Crear un título para la tabla
            Label titleLabel = new Label("Chapters of " + selectedSeasonName);
            titleLabel.setAlignment(Pos.CENTER);
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            // Crear un BorderPane para incluir el título y la tabla
            BorderPane borderPane = new BorderPane();
            borderPane.setTop(titleLabel);
            borderPane.setCenter(tableView);

            Scene scene = new Scene(borderPane, 400, 500);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Chapters of " + selectedSeasonName);
            stage.show();
        } else {
            // Si no hay capítulos en la temporada, mostrar un mensaje
            Label messageLabel = new Label("There are no chapters in this season.");
            messageLabel.setAlignment(Pos.CENTER);
            messageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            Scene scene = new Scene(messageLabel, 400, 500);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("No Chapters");
            stage.show();
        }
    }

}
