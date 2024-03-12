package co.edu.uptc.run;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;
import co.edu.uptc.utilities.FileManagement;

public class CreateSerie {
    private Scene Scene2, Scene3, Scene4, Scene5;
    private Scene newSerieScene;
    private Stage primaryStage;
    private TextField text1 = new TextField();
    private TextField text2 = new TextField();
    private TextField text3 = new TextField();
    private TextField textNameChapter = new TextField();
    private TextField textDurationChapter = new TextField();
    private TextField textDescriptionChapter = new TextField();
    private TextField seasonField = new TextField();
    Label labelWarning = new Label();
    TableView<MultimediaContent> tablaChapters = new TableView<>();
    ObservableList<String> seasonsList = FXCollections.observableArrayList();
    ObservableList<String> multimediaContentList = FXCollections.observableArrayList();
    ArrayList<MultimediaContent> chapterList = new ArrayList<>();

    ChoiceBox<String> additionalOptions = new ChoiceBox<>(seasonsList);
    ChoiceBox<String> additionalOptionsChooseChapters = new ChoiceBox<>(seasonsList);
    ChoiceBox<String> additionalOptionsMultimediaContent = new ChoiceBox<>(multimediaContentList);
    // file name Box
    ChoiceBox<String> fileBox = new ChoiceBox<>();

    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private AdminController ac;
    private CategoryController categoryC;
    private FileManagement fm;
    private Scene modifySerie2;
    private Scene formularySeason2;
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    File selectedFile;
    File selectedCover;

    public CreateSerie(Stage primaryStage, AdminController adminC) {
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

    public Scene newSerieScene() {
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
        GridPane.setConstraints(labelWarning, 1, 5);
        labelWarning.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, text1, labelDirector, text2, labelDescription, text3, labelCategory,
                choiceBox, labelWarning);
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

        cancelButton.setOnAction(event -> llamarEntryWindowSerie());
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
        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("formPane");
        formGrid.setStyle("-fx-background-color: #191919;");
        formGrid.setHgap(20); // Espacio horizontal entre las columnas
        formGrid.setVgap(20); // Espacio vertical entre las filas
        formGrid.setPadding(new Insets(20)); // Margen alrededor del GridPane

        // Botón de retorno
        ImageView iconoReturn = new ImageView(new Image("file:" + "src\\prograIconos\\volver.png"));
        iconoReturn.setFitWidth(24);
        iconoReturn.setFitHeight(24);

        // Crear un BorderPane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #191919;");

        // HBox para los botones

        // Botones "Guardar" y "Cancelar"
        Button acceptButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        acceptButton.setOnAction(event -> {
            String missingChapterSeason = ac.validateHaveChapter(ac.getCurrentSerie().getId());

            if (!ac.validateHaveSeason(ac.getCurrentSerie().getId())) {
                ac.showErrorTimeline(text1, labelWarning, " must have at least one season.");
                return;
            } else if (missingChapterSeason != null) {
                ac.showErrorTimeline(text1, labelWarning, "The selected season '" + missingChapterSeason
                        + "' must have at least one chapter.");
                return;
            }

            // Si la validación pasa, continuar con la lógica para guardar la serie y
            // mostrar la ventana emergente
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("The serie has been successfully created.");
            alert.showAndWait();

            // cambio de texto predet. en Alert
            Button buttonOK = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Button buttonCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            buttonOK.setText("Accept");
            buttonCancel.setText("Return");
            //
            llamarEntryWindowSerie();
        });

        cancelButton.setOnAction(event -> {
            // Crear una ventana de confirmación
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmación");
            confirmationDialog.setHeaderText(null);
            confirmationDialog.setContentText("Are you sure you want to cancel? The entire series will be deleted.");

            // cambio de texto predet. en Alert
            Button buttonOK = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.OK);
            Button buttonCancel = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            buttonOK.setText("Accept");
            buttonCancel.setText("Return");
            //

            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ac.deleteSerie(ac.getCurrentSerie().getId());
                    llamarEntryWindowSerie();
                }
            });
        });

        cancelButton.setId("button");
        acceptButton.setId("button");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(30);
        acceptButton.setPrefWidth(150);
        acceptButton.setPrefHeight(30);

        Insets returnButtonMargin = new Insets(15); // Ajusta el tamaño del margen según tus preferencias

        // Crear la escena
        Scene2 = new Scene(root, screenWidth, screenHeight);
        Scene2.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());

        // Establecer la escena en la ventana
        primaryStage.setScene(Scene2);
        primaryStage.setTitle("New Movie Scene");
        primaryStage.show();

        // Crear secciones izquierda y derecha del formulario
        VBox leftForm = createLeftFormSection();
        leftForm.getStyleClass().add("leftForm"); // Aplicar clase CSS para el color
        VBox rightForm = createRightFormSection();
        rightForm.getStyleClass().add("rightForm"); // Aplicar clase CSS para el color

        // Añadir secciones al GridPane
        formGrid.add(leftForm, 0, 0);
        formGrid.add(rightForm, 1, 0);
        formGrid.add(acceptButton, 0, 1); // Agrega el buttonPane en la columna 0 y la fila 1 del GridPane
        formGrid.add(cancelButton, 1, 1); // Agrega el buttonPane en la columna 0 y la fila 1 del GridPane
        GridPane.setHalignment(acceptButton, HPos.RIGHT);

        // Ajustar alineación del GridPane
        formGrid.setAlignment(Pos.CENTER);

        // Colocar el formulario en el centro del BorderPane
        root.setCenter(formGrid);
    }

    private VBox createLeftFormSection() {
        VBox leftFormSection = new VBox();
        leftFormSection.setAlignment(Pos.CENTER);
        leftFormSection.setSpacing(25);
        leftFormSection.setPadding(new Insets(20));

        leftFormSection.setMaxWidth(500); // Establecer un ancho máximo
        leftFormSection.setMaxHeight(350); // Establecer una altura máxima
        leftFormSection.setMinWidth(500); // Establecer un ancho mínimo
        leftFormSection.setMinHeight(350); // Establecer una altura mínima

        // HBox para el campo de texto adicional y el botón "Agregar"
        HBox seasonBox = new HBox();
        seasonBox.setAlignment(Pos.CENTER);
        seasonBox.setSpacing(10);

        Button addButton = new Button("Add Season");
        additionalOptions.setPrefWidth(260);
        addButton.setPrefWidth(380);
        addButton.setOnAction(event -> {
            String seasonName = seasonField.getText();
            if (seasonField.getText().isBlank()
                    || !ac.validateCharacterSpecialAllowNumberSpaceBlank(seasonField.getText())
                    || !ac.validateName(seasonField.getText())) {
                ac.showErrorTimeline(seasonField, labelWarning,
                        "Invalid name. Max 2 characters, no special characters.");
                return;

            } else if (!ac.validateNameSeason(seasonName, ac.getCurrentSerie().getId())) {
                ac.showErrorTimeline(seasonField, labelWarning,
                        "Invalid name,the name of this season already exists. Please enter another name.");
                return;
            }

            ac.addSeason(ac.getCurrentSerie().getId(), seasonName, null);
            seasonsList.add(seasonName);
            seasonField.clear();
        });

        Button buttonDeleteSeason = new Button();

        ImageView iconoDeleteSeason = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        ImageView iconoModifySeason = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));

        iconoDeleteSeason.setFitWidth(16);
        iconoDeleteSeason.setFitHeight(16);

        iconoModifySeason.setFitWidth(16);
        iconoModifySeason.setFitHeight(16);

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

                // cambio de texto predet. en Alert
                Button buttonOK = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.OK);
                Button buttonCancel = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                buttonOK.setText("Accept");
                buttonCancel.setText("Return");
                //

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
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptions, labelWarning, "Select a season");
            }
        });

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
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptions, labelWarning, "Select a season");

            }
        });

        buttonDeleteSeason.setPrefWidth(50);
        buttonModifySeason.setPrefWidth(50);

        // Añade los elementos a la vista
        seasonBox.getChildren().addAll(new Label("Name season:"), seasonField, additionalOptions);
        seasonField.setPrefWidth(280);

        // Botones adicionales
        HBox additionalButtons = new HBox(10, additionalOptions, buttonDeleteSeason, buttonModifySeason);
        additionalButtons.setAlignment(Pos.CENTER);

        // Agregar los elementos al VBox principal
        leftFormSection.getChildren().addAll(seasonBox, addButton, additionalButtons, labelWarning);

        return leftFormSection;
    }

    private VBox createRightFormSection() {
        VBox rightFormSection = new VBox();
        rightFormSection.setAlignment(Pos.CENTER);
        rightFormSection.setSpacing(25);
        rightFormSection.setPadding(new Insets(20));

        rightFormSection.setMaxWidth(500); // Establecer un ancho máximo
        rightFormSection.setMaxHeight(350); // Establecer una altura máxima
        rightFormSection.setMinWidth(500); // Establecer un ancho mínimo
        rightFormSection.setMinHeight(350); // Establecer una altura mínima

        ImageView iconoDeleteChapter = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        ImageView iconoModifyChapter = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));

        iconoDeleteChapter.setFitWidth(16);
        iconoDeleteChapter.setFitHeight(16);

        iconoModifyChapter.setFitWidth(16);
        iconoModifyChapter.setFitHeight(16);
        // Crear un Label
        Label labelSelect = new Label("Select a Season:");

        // Configurar el ChoiceBox
        additionalOptionsChooseChapters.setPrefWidth(250);

        // Crear el HBox con el Label y el ChoiceBox
        HBox chooseOption = new HBox(10, labelSelect, additionalOptionsChooseChapters);
        chooseOption.setAlignment(Pos.CENTER);
        chooseOption.setPrefWidth(400); // Ajusta el ancho según tus necesidades
        additionalOptionsMultimediaContent.setPrefWidth(250);

        ArrayList<Season> seasonList = new ArrayList<>();
        for (Season season : seasonList) {
            additionalOptionsChooseChapters.getItems().add(season.getSeasonName());

        }

        additionalOptionsChooseChapters.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Obtener la serie actual
                        Serie currentSerie = ac.getCurrentSerie();

                        // Buscar la temporada seleccionada
                        Season selectedSeason = currentSerie.getSeasons().stream()
                                .filter(season -> season.getSeasonName().equals(newValue))
                                .findFirst()
                                .orElse(null);

                        // Verificar si la temporada seleccionada no es nula y tiene capítulos
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

        Button addChapterButton = new Button("Add Chapter");
        addChapterButton.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            if (selectedSeasonName != null) {
                formularyChapter();
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptionsChooseChapters, labelWarning, "Select a season");
            }

        });
        addChapterButton.setPrefWidth(370);

        // VBox para el botón "Add Chapter"
        VBox addChapterBox = new VBox(addChapterButton);
        addChapterBox.setAlignment(Pos.CENTER);

        Button viewChapterButton = new Button("View Chapters");

        viewChapterButton.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            if (selectedSeasonName != null) {
                tableChapters(selectedSeasonName);
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptionsChooseChapters, labelWarning, "Select a season");
            }
        });

        viewChapterButton.setPrefWidth(370);
        // VBox para el botón "Add Chapter"
        VBox viewChapterBox = new VBox(viewChapterButton);
        viewChapterBox.setAlignment(Pos.CENTER);

        Button buttonDeleteChapter = new Button();
        buttonDeleteChapter.setGraphic(iconoDeleteChapter);
        buttonDeleteChapter.getStyleClass().add("boton-delete");

        buttonDeleteChapter.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            String selectedChapterName = additionalOptionsMultimediaContent.getValue();
            if (selectedSeasonName != null && selectedChapterName != null) {
                // Mostrar un cuadro de diálogo de confirmación
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Delete Chapter");
                confirmationDialog.setContentText("Are you sure you want to delete this chapter?");

                // cambio de texto predet. en Alert
                Button buttonOK = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.OK);
                Button buttonCancel = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                buttonOK.setText("Accept");
                buttonCancel.setText("Return");
                //

                // Obtener la respuesta del usuario
                confirmationDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Eliminar el capítulo de la temporada seleccionada
                        ac.deleteChapterName(selectedSeasonName, ac.getCurrentSerie().getId(), selectedChapterName);

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
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptionsMultimediaContent, labelWarning, "Select a chapter");

            }
        });

        Button buttonModifyChapter = new Button();
        buttonModifyChapter.setGraphic(iconoModifyChapter);
        buttonModifyChapter.getStyleClass().add("boton-modify");

        buttonModifyChapter.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            String selectedChapterName = additionalOptionsMultimediaContent.getValue();
            if (selectedChapterName == null) {
                ac.showErrorTimelineChoiceBox(additionalOptionsMultimediaContent, labelWarning, "Select a chapter");
                return;
            }
            modifyChapter(selectedSeasonName, selectedChapterName);

        });

        buttonDeleteChapter.setPrefWidth(50);
        buttonModifyChapter.setPrefWidth(50);

        // HBox para los botones adicionales
        HBox additionalButtonsChapters = new HBox(10, additionalOptionsMultimediaContent, buttonDeleteChapter,
                buttonModifyChapter);
        additionalButtonsChapters.setAlignment(Pos.CENTER);

        // Agregar todos los elementos al VBox principal
        rightFormSection.getChildren().addAll(chooseOption, addChapterBox,
                additionalButtonsChapters,
                viewChapterBox, labelWarning);

        return rightFormSection;
    }

    private void addNewSerie() {

        if (text1.getText().isBlank() && text2.getText().isBlank() && text3.getText().isBlank()
                && choiceBox.getValue() == null) {
            ac.showErrorTimeline(text1, labelWarning,
                    "* All fields must be filled!");
            ac.showErrorTimeline(text2, labelWarning, "* All fields must be filled!");
            ac.showErrorTimeline(text3, labelWarning, "* All fields must be filled!");
            ac.showErrorTimelineChoiceBox(choiceBox, labelWarning, "* All fields must be filled!");
            return; // Salir del método si hay campos vacíos
        } else if (text1.getText().isBlank() || !ac.validateName(text1.getText())
                || !ac.validateCharacterSpecialAllowNumberSpaceBlank(text1.getText())) {
            ac.showErrorTimeline(text1, labelWarning,
                    "Invalid name. Max 2 characters, no  numbers or special characters.");
            return;
        } else if (text2.getText().isBlank() || !ac.validateName(text2.getText())
                || !ac.validarSinCharacterSpecial(text2.getText())) {
            ac.showErrorTimeline(text2, labelWarning,
                    "Invalid Director. Max 2 characters, no special characters.");
            return;
        } else if (text3.getText().isBlank() || !ac.validateDescription(text3.getText())) {
            ac.showErrorTimeline(text3, labelWarning,
                    "Invalid description. Max 5 characters.");
            return;
        } else if (choiceBox.getValue() == null) {
            ac.showErrorTimelineChoiceBox(choiceBox, labelWarning, "Please select a category.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to save the changes?");

        // cambio de texto predet. en Alert
        Button buttonOK = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        Button buttonCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        buttonOK.setText("Accept");
        buttonCancel.setText("Return");
        //

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
        }
    }

    private void formularyChapter() {
        BorderPane root3 = new BorderPane();
        root3.setId("root2");
        GridPane gridPane = new GridPane();

        textNameChapter.setPrefWidth(340);
        textDurationChapter.setPrefWidth(340);
        textDescriptionChapter.setPrefWidth(340);

        Label labelName = new Label("Chapter name:");
        Label labelDuration = new Label("Duration:");
        Label labelDescription = new Label("Description:");
        Label labelFileName = new Label("File Video");
        Label labelImageCover = new Label("Image cover");

        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDuration, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);

        GridPane.setConstraints(textNameChapter, 1, 0);
        GridPane.setConstraints(textDurationChapter, 1, 1);
        GridPane.setConstraints(textDescriptionChapter, 1, 2);
        GridPane.setConstraints(labelWarning, 1, 5);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, textNameChapter, labelDuration, textDurationChapter, labelDescription,
                textDescriptionChapter, labelFileName, labelWarning);
        // FILE CHOOSER

        // File Video buttton
        Button fileButton = new Button();
        fileButton.setPrefWidth(50);
        fileButton.setOnAction(event -> chooseFileScreen());

        // Cover image button
        Button coverButton = new Button();
        coverButton.setPrefWidth(50);
        coverButton.setOnAction(event -> chooseImageScreen());

        ImageView fileIcon = new ImageView(new Image("file:" + "src/prograIconos/video.png"));
        fileIcon.setFitWidth(22);
        fileIcon.setFitHeight(22);
        fileButton.setGraphic(fileIcon);
        fileButton.setId("filebutton");

        ImageView coverIcon = new ImageView(new Image("file:" + "src/prograIconos/cover.png"));
        coverIcon.setFitWidth(22);
        coverIcon.setFitHeight(22);
        coverButton.setGraphic(coverIcon);
        coverButton.setId("filebutton");

        HBox fileHBox = new HBox(labelFileName, fileButton);
        HBox coverHBox = new HBox(labelImageCover, coverButton);
        fileHBox.setSpacing(25);
        coverHBox.setSpacing(25);
        GridPane.setConstraints(fileHBox, 0, 4);
        GridPane.setConstraints(coverHBox, 1, 4);
        coverHBox.setTranslateX(175);

        gridPane.getChildren().addAll(fileHBox, coverHBox);
        //

        root3.setCenter(gridPane);

        root3.setStyle("-fx-background-color: #191919;");
        gridPane.setStyle("-fx-background-color: white;");

        // Save button
        Button acceptButton = new Button();

        GridPane.setConstraints(acceptButton, 0, 5);
        acceptButton.setTranslateY(100);
        acceptButton.setText("Accept");
        acceptButton.setPrefWidth(150);
        GridPane.setHalignment(acceptButton, javafx.geometry.HPos.LEFT);
        acceptButton.setOnAction(event -> {
            // Obtener la temporada seleccionada del ChoiceBox
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
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
                Boolean numberValid;
                try {
                    Integer.parseInt(textDurationChapter.getText());
                    numberValid = true;
                } catch (Exception e) {
                    numberValid = false;
                }
                if (textNameChapter.getText().isBlank() && textDurationChapter.getText().isBlank()
                        && textDescriptionChapter.getText().isBlank() && (selectedFile == null)
                        && (selectedCover == null)) {
                    ac.showErrorTimeline(textNameChapter, labelWarning,
                            "* All fields must be filled!");
                    ac.showErrorTimeline(textDurationChapter, labelWarning, "* All fields must be filled!");
                    ac.showErrorTimeline(textDescriptionChapter, labelWarning, "* All fields must be filled!");
                    ac.showErrorTimelineFile(fileButton, labelWarning, "* Select the file video.");
                    ac.showErrorTimelineFile(coverButton, labelWarning, "* Select the file video.");

                    return; // Salir del método si hay campos vacíos
                } else if (textNameChapter.getText().isBlank()
                        || !ac.validateCharacterSpecialAllowNumberSpaceBlank(textNameChapter.getText())
                        || !ac.validateName(textNameChapter.getText())) {
                    ac.showErrorTimeline(textNameChapter, labelWarning,
                            "Invalid name. Max 2 characters, no special characters.");
                    return;

                } else if (textDurationChapter.getText().isBlank()
                        || !numberValid) {
                    ac.showErrorTimeline(textDurationChapter, labelWarning,
                            "* Duration format is invalid !");
                    return;
                } else if (textDescriptionChapter.getText().isBlank()
                        || !ac.validateDescription(textDescriptionChapter.getText())) {
                    ac.showErrorTimeline(textDescriptionChapter, labelWarning,
                            "Invalid description. Max 5 characters.");
                    return;
                } else if (selectedFile == null) {
                    ac.showErrorTimelineFile(fileButton, labelWarning, "* Select the file video.");
                    return;
                } else if (selectedCover == null) {
                    ac.showErrorTimelineFile(coverButton, labelWarning, "* Select the cover image.");
                    return;
                }
                ac.addChapterName(textNameChapter.getText(), textDescriptionChapter.getText(),
                        Integer.parseInt(textDurationChapter.getText()), ac.getCurrentSerie().getId(),
                        selectedSeasonName, selectedFile.getName(), selectedCover.getName());

                cambiarAEscena1();

                // Actualizar el ChoiceBox de capítulos con los capítulos de la temporada
                // seleccionada
                multimediaContentList.clear();
                for (MultimediaContent chapter : selectedSeasonObj.getchapters()) {
                    multimediaContentList.add(chapter.getName());
                }
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

    public void chooseFileScreen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4 Files", "*.mp4"));
        fileChooser.setTitle("Select the file Video");
        File initialDirectory = new File("src/multimediaVideos/Series");
        fileChooser.setInitialDirectory(initialDirectory);
        selectedFile = fileChooser.showOpenDialog(primaryStage);
    }

    public void chooseImageScreen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));
        fileChooser.setTitle("Select the cover image");
        File initialDirectory = new File("src/multimediaCovers/Series");
        fileChooser.setInitialDirectory(initialDirectory);
        selectedCover = fileChooser.showOpenDialog(primaryStage);
    }

    private void cambiarAEscena1() {
        primaryStage.setScene(Scene2);
    }

    private void modifyChapter(String selectedSeason, String selectedChapter) {
        BorderPane root3 = new BorderPane();

        TextField textNameChapterModify = new TextField(ac.getCurrentSerie().getSeasons()
                .get(ac.seasonNameFound(selectedSeason, ac.getCurrentSerie().getId())).getchapters()
                .get(ac.chapterNameFound(selectedSeason, ac.getCurrentSerie().getId(), selectedChapter)).getName());
        TextField textDurationChapterModify = new TextField(String.valueOf(ac.getCurrentSerie().getSeasons()
                .get(ac.seasonNameFound(selectedSeason, ac.getCurrentSerie().getId())).getchapters()
                .get(ac.chapterNameFound(selectedSeason, ac.getCurrentSerie().getId(), selectedChapter))
                .getDuration()));
        TextField textDescriptionChapterModify = new TextField(ac.getCurrentSerie().getSeasons()
                .get(ac.seasonNameFound(selectedSeason, ac.getCurrentSerie().getId())).getchapters()
                .get(ac.chapterNameFound(selectedSeason, ac.getCurrentSerie().getId(), selectedChapter))
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
        GridPane.setConstraints(labelWarning, 1, 3); // Define la posición del labelWarning en la fila 3 y columna 1

        GridPane.setConstraints(textNameChapterModify, 1, 0);
        GridPane.setConstraints(textDurationChapterModify, 1, 1);
        GridPane.setConstraints(textDescriptionChapterModify, 1, 2);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().addAll(labelName, textNameChapterModify, labelDuration, textDurationChapterModify,
                labelDescription, textDescriptionChapterModify, labelWarning);
        root3.setCenter(gridPane);

        root3.setStyle("-fx-background-color: #191919;");
        gridPane.setStyle("-fx-background-color: white;");

        // Save button
        Button acceptButton = new Button("Accept");
        acceptButton.setPrefWidth(150);
        GridPane.setConstraints(acceptButton, 0, 5);
        acceptButton.setTranslateY(100);
        GridPane.setHalignment(acceptButton, HPos.LEFT);

        acceptButton.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            Season selectedSeasonObj = null;

            for (Season season : ac.getCurrentSerie().getSeasons()) {
                if (season.getSeasonName().equals(selectedSeasonName)) {
                    selectedSeasonObj = season;
                    break;
                }
            }

            if (selectedSeasonObj != null) {
                Boolean numberValid;
                try {
                    Integer.parseInt(textDurationChapterModify.getText());
                    numberValid = true;
                } catch (Exception e) {
                    numberValid = false;
                }
                if (textNameChapterModify.getText().isBlank() && textDurationChapterModify.getText().isBlank()
                        && textDescriptionChapterModify.getText().isBlank()) {
                    ac.showErrorTimeline(textNameChapterModify, labelWarning,
                            "* All fields must be filled!");
                    ac.showErrorTimeline(textDurationChapterModify, labelWarning, "* All fields must be filled!");
                    ac.showErrorTimeline(textDescriptionChapterModify, labelWarning, "* All fields must be filled!");
                    return; // Salir del método si hay campos vacíos
                } else if (textNameChapterModify.getText().isBlank()
                        || !ac.validateCharacterSpecialAllowNumberSpaceBlank(textNameChapterModify.getText())
                        || !ac.validateName(textNameChapterModify.getText())) {
                    ac.showErrorTimeline(textNameChapterModify, labelWarning,
                            "Invalid name. Max 2 characters, no special characters.");
                    return;

                } else if (textDurationChapterModify.getText().isBlank()
                        || !numberValid) {
                    ac.showErrorTimeline(textDurationChapterModify, labelWarning,
                            "* Duration format is invalid !");
                    return;
                } else if (textDescriptionChapterModify.getText().isBlank()
                        || !ac.validateDescription(textDescriptionChapterModify.getText())) {
                    ac.showErrorTimeline(textDescriptionChapterModify, labelWarning,
                            "Invalid description. Max 5 characters.");
                    return;
                }
                ac.modifyChaptersName(textDescriptionChapterModify.getText(), textNameChapterModify.getText(),
                        Integer.parseInt(textDurationChapterModify.getText()), ac.getCurrentSerie().getId(),
                        selectedSeason, selectedChapter);

                cambiarAEscena1();

                multimediaContentList.clear();
                for (MultimediaContent chapter : selectedSeasonObj.getchapters()) {
                    multimediaContentList.add(chapter.getName());
                }

            } else {

            }

            textNameChapterModify.clear();
            textDurationChapterModify.clear();
            textDescriptionChapterModify.clear();
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(150);
        GridPane.setConstraints(cancelButton, 1, 5);
        cancelButton.setTranslateY(100);
        GridPane.setHalignment(cancelButton, HPos.RIGHT);

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
        GridPane.setConstraints(labelCategory, 0, 3);

        GridPane.setConstraints(textModify1, 1, 0);
        GridPane.setConstraints(textModify2, 1, 1);
        GridPane.setConstraints(textModify3, 1, 2);
        GridPane.setConstraints(choiceBox, 1, 3);
        GridPane.setConstraints(labelWarning, 1, 4);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, textModify1, labelDirector, textModify2, labelDescription, textModify3,
                labelCategory,
                choiceBox, labelWarning);
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

            if (textModify1.getText().isBlank() && textModify2.getText().isBlank() && textModify3.getText().isBlank()
                    && choiceBox.getValue() == null) {
                ac.showErrorTimeline(textModify1, labelWarning,
                        "* All fields must be filled!");
                ac.showErrorTimeline(textModify2, labelWarning, "* All fields must be filled!");
                ac.showErrorTimeline(textModify3, labelWarning, "* All fields must be filled!");
                ac.showErrorTimelineChoiceBox(choiceBox, labelWarning, "* All fields must be filled!");
                return; // Salir del método si hay campos vacíos
            } else if (textModify1.getText().isBlank() || !ac.validateName(textModify1.getText())
                    || !ac.validateCharacterSpecialAllowNumberSpaceBlank(textModify1.getText())) {
                ac.showErrorTimeline(textModify1, labelWarning,
                        "Invalid name. Max 2 characters, no special characters.");
                return;
            } else if (textModify2.getText().isBlank() || !ac.validateName(textModify2.getText())
                    || !ac.validarSinCharacterSpecial(textModify2.getText())) {
                ac.showErrorTimeline(textModify2, labelWarning,
                        "Invalid Director. Max 2 characters, no special characters.");
                return;
            } else if (textModify3.getText().isBlank() || !ac.validateDescription(textModify3.getText())) {
                ac.showErrorTimeline(textModify3, labelWarning,
                        "Invalid description. Max 5 characters.");
                return;
            } else if (choiceBox.getValue() == null) {
                ac.showErrorTimelineChoiceBox(choiceBox, labelWarning, "Please select a category.");
                return;
            }

            ac.modifySeries(textModify3.getText(), textModify1.getText(), textModify2.getText(), choiceBox.getValue(),
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
        Season selectedSeason = ac.getCurrentSerie().getSeasons()
                .get(ac.seasonNameFound(selectedSeasonName, ac.getCurrentSerie().getId()));

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

    public Scene modifySeries2(Serie serie) {
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
        GridPane.setConstraints(labelCategory, 0, 3);

        GridPane.setConstraints(textModify1, 1, 0);
        GridPane.setConstraints(textModify2, 1, 1);
        GridPane.setConstraints(textModify3, 1, 2);
        GridPane.setConstraints(choiceBox, 1, 3);
        GridPane.setConstraints(labelWarning, 1, 4);

        gridPane.setVgap(20);
        gridPane.setHgap(0);

        gridPane.getChildren().setAll(labelName, textModify1, labelDirector, textModify2, labelDescription, textModify3,
                labelCategory,
                choiceBox, labelWarning);
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

            if (textModify1.getText().isBlank() && textModify2.getText().isBlank() && textModify3.getText().isBlank()
                    && choiceBox.getValue() == null) {
                ac.showErrorTimeline(textModify1, labelWarning,
                        "* All fields must be filled!");
                ac.showErrorTimeline(textModify2, labelWarning, "* All fields must be filled!");
                ac.showErrorTimeline(textModify3, labelWarning, "* All fields must be filled!");
                ac.showErrorTimelineChoiceBox(choiceBox, labelWarning, "* All fields must be filled!");
                return; // Salir del método si hay campos vacíos
            } else if (textModify1.getText().isBlank() || !ac.validateName(textModify1.getText())
                    || !ac.validateCharacterSpecialAllowNumberSpaceBlank(textModify1.getText())) {
                ac.showErrorTimeline(textModify1, labelWarning,
                        "Invalid name. Max 2 characters, no special characters.");
                return;
            } else if (textModify2.getText().isBlank() || !ac.validateName(textModify2.getText())
                    || !ac.validarSinCharacterSpecial(textModify2.getText())) {
                ac.showErrorTimeline(textModify2, labelWarning,
                        "Invalid Director. Max 2 characters, no special characters.");
                return;
            } else if (textModify3.getText().isBlank() || !ac.validateDescription(textModify3.getText())) {
                ac.showErrorTimeline(textModify3, labelWarning,
                        "Invalid description. Max 5 characters.");
                return;
            } else if (choiceBox.getValue() == null) {
                ac.showErrorTimelineChoiceBox(choiceBox, labelWarning, "Please select a category.");
                return;
            }

            ac.modifySeries(textModify3.getText(), textModify1.getText(), textModify2.getText(), choiceBox.getValue(),
                    serie.getId());

            // Cambiar a la escena anterior
            formularySeason2(serie);
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
        modifySerie2 = new Scene(root2, screenWidth, screenHeight);
        // aplicar CSS
        modifySerie2.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());
        cancelButton.setId("button");
        acceptButton.setId("button");

        return modifySerie2;

    }

    private void formularySeason2(Serie serie) {
        // Crear el formulario
        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("formPane");
        formGrid.setStyle("-fx-background-color: #191919;");
        formGrid.setHgap(20); // Espacio horizontal entre las columnas
        formGrid.setVgap(20); // Espacio vertical entre las filas
        formGrid.setPadding(new Insets(20)); // Margen alrededor del GridPane

        // Botón de retorno
        ImageView iconoReturn = new ImageView(new Image("file:" + "src\\prograIconos\\volver.png"));
        iconoReturn.setFitWidth(24);
        iconoReturn.setFitHeight(24);

        Button returnButton = new Button();
        returnButton.setGraphic(iconoReturn);
        returnButton.setId("button");
        returnButton.setOnAction(event -> {
            modifySeries2(serie);
        });
        returnButton.setPrefWidth(100);

        // Crear un BorderPane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #191919;");

        // HBox para los botones

        // Botones "Guardar" y "Cancelar"
        Button acceptButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        acceptButton.setOnAction(event -> {
            String missingChapterSeason = ac.validateHaveChapter(serie.getId());

            if (!ac.validateHaveSeason(serie.getId())) {
                ac.showErrorTimeline(text1, labelWarning, " must have at least one season.");
                return;
            } else if (missingChapterSeason != null) {
                ac.showErrorTimeline(text1, labelWarning, "The selected season '" + missingChapterSeason
                        + "' must have at least one chapter.");
                return;
            }

            // Si la validación pasa, continuar con la lógica para guardar la serie y
            // mostrar la ventana emergente
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("The series has been successfully created.");

            // cambio de texto predet. en Alert
            Button buttonOK = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Button buttonCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            buttonOK.setText("Accept");
            buttonCancel.setText("Return");
            //

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
                    ac.deleteSerie(serie.getId());
                    llamarEntryWindowSerie();
                }
            });
        });

        cancelButton.setId("button");
        acceptButton.setId("button");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(30);
        acceptButton.setPrefWidth(150);
        acceptButton.setPrefHeight(30);

        Insets returnButtonMargin = new Insets(15); // Ajusta el tamaño del margen según tus preferencias
        BorderPane.setMargin(returnButton, returnButtonMargin);

        BorderPane.setAlignment(returnButton, Pos.TOP_LEFT);
        root.setTop(returnButton);

        // Crear la escena
        formularySeason2 = new Scene(root, screenWidth, screenHeight);
        formularySeason2.getStylesheets().add(new File("src\\main\\java\\co\\styles\\serie.css").toURI().toString());

        // Establecer la escena en la ventana
        primaryStage.setScene(formularySeason2);
        primaryStage.setTitle("New Movie Scene");
        primaryStage.show();

        // Crear secciones izquierda y derecha del formulario
        VBox leftForm = createLeftFormSection2(serie);
        leftForm.getStyleClass().add("leftForm"); // Aplicar clase CSS para el color
        VBox rightForm = createRightFormSection2(serie);
        rightForm.getStyleClass().add("rightForm"); // Aplicar clase CSS para el color

        // Añadir secciones al GridPane
        formGrid.add(leftForm, 0, 0);
        formGrid.add(rightForm, 1, 0);
        formGrid.add(acceptButton, 0, 1); // Agrega el buttonPane en la columna 0 y la fila 1 del GridPane
        formGrid.add(cancelButton, 1, 1); // Agrega el buttonPane en la columna 0 y la fila 1 del GridPane
        GridPane.setHalignment(acceptButton, HPos.RIGHT);

        // Ajustar alineación del GridPane
        formGrid.setAlignment(Pos.CENTER);

        // Colocar el formulario en el centro del BorderPane
        root.setCenter(formGrid);
    }

    private VBox createLeftFormSection2(Serie serie) {
        VBox leftFormSection = new VBox();
        leftFormSection.setAlignment(Pos.CENTER);
        leftFormSection.setSpacing(25);
        leftFormSection.setPadding(new Insets(20));

        leftFormSection.setMaxWidth(500); // Establecer un ancho máximo
        leftFormSection.setMaxHeight(350); // Establecer una altura máxima
        leftFormSection.setMinWidth(500); // Establecer un ancho mínimo
        leftFormSection.setMinHeight(350); // Establecer una altura mínima

        // HBox para el campo de texto adicional y el botón "Agregar"
        HBox seasonBox = new HBox();
        seasonBox.setAlignment(Pos.CENTER);
        seasonBox.setSpacing(10);

        for (Season season : serie.getSeasons()) {
            additionalOptions.getItems().add(season.getSeasonName());
        }

        Button addButton = new Button("Add Season");
        additionalOptions.setPrefWidth(260);
        addButton.setPrefWidth(380);
        addButton.setOnAction(event -> {
            String seasonName = seasonField.getText();
            if (seasonField.getText().isBlank()
                    || !ac.validateCharacterSpecialAllowNumberSpaceBlank(seasonField.getText())
                    || !ac.validateName(seasonField.getText())) {
                ac.showErrorTimeline(seasonField, labelWarning,
                        "Invalid name. Max 2 characters, no special characters.");
                return;

            } else if (!ac.validateNameSeason(seasonName, serie.getId())) {
                ac.showErrorTimeline(seasonField, labelWarning,
                        "Invalid name,the name of this season already exists. Please enter another name.");
                return;
            }

            ac.addSeason(serie.getId(), seasonName, null);
            seasonsList.add(seasonName);
            seasonField.clear();
        });

        Button buttonDeleteSeason = new Button();

        ImageView iconoDeleteSeason = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        ImageView iconoModifySeason = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));

        iconoDeleteSeason.setFitWidth(16);
        iconoDeleteSeason.setFitHeight(16);

        iconoModifySeason.setFitWidth(16);
        iconoModifySeason.setFitHeight(16);

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
                        ac.deleteSeasonName(selectedSeason, serie.getId());
                        additionalOptions.getItems().remove(selectedSeason);
                        seasonsList.remove(selectedSeason);

                        // Después de eliminar, selecciona la primera temporada (o cualquier otra
                        // lógica)
                        if (!additionalOptions.getItems().isEmpty()) {
                            additionalOptions.getSelectionModel().selectFirst();
                        }
                    }
                });
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptions, labelWarning, "Select a season");
            }
        });

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
                        ac.modifySeasonName(newName, serie.getId(), selectedSeason);

                        // Actualizar el nombre de la temporada en la lista observable
                        int selectedIndex = additionalOptions.getItems().indexOf(selectedSeason);
                        seasonsList.set(selectedIndex, newName);

                        // Actualizar el ChoiceBox con la lista actualizada
                        additionalOptions.setItems(seasonsList);

                        // Volver a seleccionar la temporada modificada
                        additionalOptions.getSelectionModel().select(newName);
                    }
                });
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptions, labelWarning, "Select a season");

            }
        });

        buttonDeleteSeason.setPrefWidth(50);
        buttonModifySeason.setPrefWidth(50);

        // Añade los elementos a la vista
        seasonBox.getChildren().addAll(new Label("Name season:"), seasonField, additionalOptions);
        seasonField.setPrefWidth(280);

        // Botones adicionales
        HBox additionalButtons = new HBox(10, additionalOptions, buttonDeleteSeason, buttonModifySeason);
        additionalButtons.setAlignment(Pos.CENTER);

        // Agregar los elementos al VBox principal
        leftFormSection.getChildren().addAll(seasonBox, addButton, additionalButtons, labelWarning);

        return leftFormSection;
    }

    private VBox createRightFormSection2(Serie serie) {
        VBox rightFormSection = new VBox();
        rightFormSection.setAlignment(Pos.CENTER);
        rightFormSection.setSpacing(25);
        rightFormSection.setPadding(new Insets(20));

        rightFormSection.setMaxWidth(500); // Establecer un ancho máximo
        rightFormSection.setMaxHeight(350); // Establecer una altura máxima
        rightFormSection.setMinWidth(500); // Establecer un ancho mínimo
        rightFormSection.setMinHeight(350); // Establecer una altura mínima

        ImageView iconoDeleteChapter = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
        ImageView iconoModifyChapter = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));

        iconoDeleteChapter.setFitWidth(16);
        iconoDeleteChapter.setFitHeight(16);

        iconoModifyChapter.setFitWidth(16);
        iconoModifyChapter.setFitHeight(16);
        // Crear un Label
        Label labelSelect = new Label("Select a Season:");

        // Configurar el ChoiceBox
        additionalOptionsChooseChapters.setPrefWidth(250);

        // Crear el HBox con el Label y el ChoiceBox
        HBox chooseOption = new HBox(10, labelSelect, additionalOptionsChooseChapters);
        chooseOption.setAlignment(Pos.CENTER);
        chooseOption.setPrefWidth(400); // Ajusta el ancho según tus necesidades
        additionalOptionsMultimediaContent.setPrefWidth(250);
        additionalOptionsChooseChapters.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Obtener la serie actual
                        Serie currentSerie = serie;

                        // Buscar la temporada seleccionada
                        Season selectedSeason = currentSerie.getSeasons().stream()
                                .filter(season -> season.getSeasonName().equals(newValue))
                                .findFirst()
                                .orElse(null);

                        // Verificar si la temporada seleccionada no es nula y tiene capítulos
                        if (selectedSeason != null && selectedSeason.getchapters() != null) {
                            multimediaContentList.clear();
                            // Agregar los capítulos de la temporada seleccionada si no están ya presentes
                            // en multimediaContentList
                            for (MultimediaContent chapter : selectedSeason.getchapters()) {
                                if (!multimediaContentList.contains(chapter.getName())) {
                                    multimediaContentList.add(chapter.getName());
                                }
                            }
                        } else {
                            multimediaContentList.clear();
                        }
                    }
                });

        Button addChapterButton = new Button("Add Chapter");
        addChapterButton.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            if (selectedSeasonName != null) {
                formularyChapter();
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptionsChooseChapters, labelWarning, "Select a season");
            }

        });
        addChapterButton.setPrefWidth(370);

        // VBox para el botón "Add Chapter"
        VBox addChapterBox = new VBox(addChapterButton);
        addChapterBox.setAlignment(Pos.CENTER);

        Button viewChapterButton = new Button("View Chapters");

        viewChapterButton.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            if (selectedSeasonName != null) {
                tableChapters(selectedSeasonName);
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptionsChooseChapters, labelWarning, "Select a season");
            }
        });

        viewChapterButton.setPrefWidth(370);
        // VBox para el botón "Add Chapter"
        VBox viewChapterBox = new VBox(viewChapterButton);
        viewChapterBox.setAlignment(Pos.CENTER);

        Button buttonDeleteChapter = new Button();
        buttonDeleteChapter.setGraphic(iconoDeleteChapter);
        buttonDeleteChapter.getStyleClass().add("boton-delete");

        buttonDeleteChapter.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            String selectedChapterName = additionalOptionsMultimediaContent.getValue();
            if (selectedSeasonName != null && selectedChapterName != null) {
                // Mostrar un cuadro de diálogo de confirmación
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Delete Chapter");
                confirmationDialog.setContentText("Are you sure you want to delete this chapter?");

                // cambio de texto predet. en Alert
                Button buttonOK = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.OK);
                Button buttonCancel = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                buttonOK.setText("Accept");
                buttonCancel.setText("Return");
                //
                // Obtener la respuesta del usuario
                confirmationDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Eliminar el capítulo de la temporada seleccionada
                        ac.deleteChapterName(selectedSeasonName, serie.getId(), selectedChapterName);

                        // Actualizar la lista observable de capítulos
                        additionalOptionsMultimediaContent.getItems().remove(selectedChapterName);

                        // Después de eliminar, selecciona la primera temporada (o cualquier otra

                    }
                });
            } else {
                ac.showErrorTimelineChoiceBox(additionalOptionsMultimediaContent, labelWarning, "Select a chapter");

            }
        });

        Button buttonModifyChapter = new Button();
        buttonModifyChapter.setGraphic(iconoModifyChapter);
        buttonModifyChapter.getStyleClass().add("boton-modify");

        buttonModifyChapter.setOnAction(event -> {
            String selectedSeasonName = additionalOptionsChooseChapters.getValue();
            String selectedChapterName = additionalOptionsMultimediaContent.getValue();
            if (selectedChapterName == null) {
                ac.showErrorTimelineChoiceBox(additionalOptionsMultimediaContent, labelWarning, "Select a chapter");
                return;
            }
            modifyChapter(selectedSeasonName, selectedChapterName);

        });

        buttonDeleteChapter.setPrefWidth(50);
        buttonModifyChapter.setPrefWidth(50);

        // HBox para los botones adicionales
        HBox additionalButtonsChapters = new HBox(10, additionalOptionsMultimediaContent, buttonDeleteChapter,
                buttonModifyChapter);
        additionalButtonsChapters.setAlignment(Pos.CENTER);

        // Agregar todos los elementos al VBox principal
        rightFormSection.getChildren().addAll(chooseOption, addChapterBox,
                additionalButtonsChapters,
                viewChapterBox, labelWarning);

        return rightFormSection;
    }

}
