package co.edu.uptc.run;

import java.io.File;
import java.util.Optional;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.CategoryController;
import co.edu.uptc.model.Category;
import co.edu.uptc.model.Movie;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class EntryWindow extends Application {
    private TableView<Movie> tabla;
    private Stage primaryStage = new Stage();
    private Scene movieScene;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    Label labelName = new Label("Movie name:");
    Label labelDirector = new Label("Director name:");
    Label labelDescription = new Label("Description:");
    Label labelDuration = new Label("Duration:");
    Label labelCategory = new Label("Category:");    
    Label labelWarning;

    ObservableList<Movie> grupos;
    // controllers
    AdminController adminC;
    CategoryController categoryC;

    public EntryWindow() {
        adminC = new AdminController();
        categoryC = new CategoryController();
        categoryC.getCategories().forEach(
                category -> choiceBox.getItems().add(category.getName()));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showMovieScene();
    }

    public void showMovieScene(){
        
        BorderPane root = new BorderPane();
        //importante tener esta instancia aqui
        tabla = new TableView<>();
        //
        MenuBar menuBar = new MenuBar();
        Menu MovieMenu = new Menu("Movie");
        Menu SerieMenu = new Menu("Serie");

        menuBar.getMenus().add(MovieMenu);
        menuBar.getMenus().add(SerieMenu);

        menuBar.getStyleClass().add("menubar");

        root.setTop(menuBar);

        // gc.setGroupList(gc.leerArchivoJson("src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json"));
        grupos = FXCollections.observableArrayList(adminC.getMovies());
        
        TableColumn<Movie, String> IdColumn = new TableColumn<>("Id");
        TableColumn<Movie, String> facultyColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> nombreGrupoColumn = new TableColumn<>("Director");

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nombreGrupoColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        tabla.getColumns().addAll(IdColumn, facultyColumn, nombreGrupoColumn);

        // Establecer ancho máximo para la tabla
        tabla.setMaxWidth(600);

        // Agregar columna de botones
        TableColumn<Movie, Void> accionesColumna = new TableColumn<>("Actions");
        accionesColumna.setCellFactory(param -> new BotonCelda());
        tabla.getColumns().add(accionesColumna);

        tabla.setItems(grupos);

        StackPane stackPane = new StackPane(tabla);
        stackPane.setAlignment(Pos.CENTER); // Centrar la tabla en el StackPane

        // Crear un botón flotante
        ImageView iconoAgregar = new ImageView(new Image("file:" + "src\\prograIconos\\anadir.png"));
        iconoAgregar.setFitWidth(22);
        iconoAgregar.setFitHeight(22);
        Button addNewButton = new Button();
        addNewButton.setTranslateY(-20);
        addNewButton.getStyleClass().add("boton-flotante");
        addNewButton.setGraphic(iconoAgregar);

        // Agregar el botón flotante en la esquina inferior derecha
        BorderPane.setAlignment(addNewButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(addNewButton, new Insets(15));
        root.setBottom(addNewButton);

        // Agregar el StackPane que contiene la tabla al centro del BorderPane
        root.setCenter(stackPane);

        // Obtener dimensiones de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        movieScene = new Scene(root, screenWidth, screenHeight);

        // Configurar la escena y mostrarla
        movieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        primaryStage.setScene(movieScene);
        primaryStage.setTitle("JavaFX MenuBar with CSS");
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Add new Movie scene
        addNewButton.setOnAction(event -> switchNewMovieScene());
    }

    public static void main(String[] args) {
        launch(args);
    }

    void switchNewMovieScene(){
        NewMovieScreen nms = new NewMovieScreen(this, adminC);
        nms.switchScene();
        primaryStage.setScene(nms.getNewMovieScene());
        primaryStage.setMaximized(true);
        primaryStage.setTitle("New Movie Scene");
        primaryStage.show();
    }
    
    public Scene getMovieScene() {
        return movieScene;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public class BotonCelda extends TableCell<Movie, Void> {
        private final Button btnEliminar = new Button();
        private final Button btnModificar = new Button();
        private final Button seeButton = new Button();

        public BotonCelda() {
            // Configura los íconos para los botones

            ImageView iconoEliminar = new ImageView(new Image("file:" + "src\\prograIconos\\eliminar.png"));
            ImageView iconoModificar = new ImageView(new Image("file:" + "src\\prograIconos\\editarB.png"));
            ImageView iconover = new ImageView(new Image("file:" + "src\\prograIconos\\ver.png"));

            iconoEliminar.setFitWidth(16);
            iconoEliminar.setFitHeight(16);

            iconoModificar.setFitWidth(16);
            iconoModificar.setFitHeight(16);

            iconover.setFitWidth(16);
            iconover.setFitHeight(16);

            btnEliminar.setGraphic(iconoEliminar);
            btnModificar.setGraphic(iconoModificar);
            seeButton.setGraphic(iconover);

            btnEliminar.getStyleClass().add("boton-eliminar");
            btnModificar.getStyleClass().add("boton-modificar");
            seeButton.getStyleClass().add("seeButton");

            btnEliminar.setOnAction(event -> {
                Movie grupo = getTableView().getItems().get(getIndex());
                // Mostrar una ventana emergente de confirmación
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this movie?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    adminC.deleteMovie(grupo.getId());

                    tabla.getItems().remove(grupo);
                    // gc.creararchivoJson(gc.getGroupList(),
                    // "src\\main\\java\\co\\edu\\uptc\\persistence\\Base.json");

                }
            });

            seeButton.setOnAction(event -> {
                seeMovieScreen(getTableView().getItems().get(getIndex()));        
            });

            btnModificar.setOnAction(event -> {
                //editMovieScreen(getTableView().getItems().get(getIndex()));       
            });

            // Configura el contenido de las celdas para mostrar los botones
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        
        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox botonesContainer = new HBox(seeButton, btnEliminar, btnModificar);
                botonesContainer.setSpacing(5);
                setGraphic(botonesContainer);
            }
        }
    }

    void seeMovieScreen(Movie movie){

        Stage secundaryStage = new Stage();
        secundaryStage.initModality(Modality.APPLICATION_MODAL);
        secundaryStage.setTitle("Movie Information");

        GridPane gridPane = new GridPane();
        gridPane.setId("root2");
        gridPane.setMaxWidth(600);
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20);
        gridPane.setHgap(50);
        
        Label name = new Label(movie.getName());
        Label director = new Label(movie.getAuthor());
        Label description = new Label(movie.getDescription());
        Label duration = new Label(String.valueOf(movie.getDuration()));
        Label category = new Label(movie.getCategory());
        
        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelDirector, 0, 1);
        GridPane.setConstraints(labelDescription, 0, 2);
        GridPane.setConstraints(labelDuration, 0, 3);
        GridPane.setConstraints(labelCategory, 0, 4);
        
        GridPane.setConstraints(name, 1, 0);
        GridPane.setConstraints(director, 1, 1);
        GridPane.setConstraints(description, 1, 2);
        GridPane.setConstraints(duration, 1, 3);
        GridPane.setConstraints(category, 1, 4);
   
        Button closeButton = new Button();
        //closeButton.setTranslateX(-100);
        closeButton.setText("Close");
        closeButton.setPrefWidth(100);
        GridPane.setConstraints(closeButton, 1,5);
        closeButton.setOnAction(event -> secundaryStage.close());
        closeButton.setId("button");
        gridPane.getChildren().setAll(labelName,labelDirector,labelDescription,labelDuration,labelCategory,name,director,description,duration,category,closeButton);
        
        // Configurar tamano description
        description.setMaxWidth(200);
        description.setWrapText(true);
        
        Scene seeMovieScene = new Scene(gridPane, 500, 550);
        seeMovieScene.getStylesheets().add(new File("src\\main\\java\\co\\styles\\principal.css").toURI().toString());
        secundaryStage.setScene(seeMovieScene);
        secundaryStage.showAndWait();
    }

    
}

