package co.edu.uptc.controller;

import java.util.ArrayList;
import com.google.gson.JsonElement;
import co.edu.uptc.model.Admin;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;
import co.edu.uptc.utilities.FileManagement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class AdminController {

    private Admin admin;
    private Serie currentSerie; // Creo que hay que eliminar esto
    private ArrayList<Movie> listMovies;
    private ArrayList<Serie> listSeries;
    FileManagement fm = new FileManagement();

    public AdminController() {
        // admin = new Admin("Admin", "", 1, "Admin1@uptc.edu.co", "Admin.1");
        admin = new Admin("Admin", "", 1, "1", "1");
        loadMoviesFile();
        loadSerieFile();
    }

    public void loadMoviesFile() {
        listMovies = new ArrayList<>();
        for (JsonElement je : fm.readJsonFile("movies")) {
            Movie movie = fm.getGson().fromJson(je, Movie.class);
            listMovies.add(movie);
        }
    }

    public void loadSerieFile() {
        listSeries = new ArrayList<>();
        for (JsonElement je : fm.readJsonFile("series")) {
            Serie serie = fm.getGson().fromJson(je, Serie.class);
            listSeries.add(serie);
        }
    }

    public boolean deleteMovie(int idMovie) {
        if (movieFound(idMovie) != -1) {
            // admin.setMovies(listMovies);
            listMovies.remove(listMovies.get(movieFound(idMovie)));
            fm.reWriteFile("movies", listMovies);
            return true;
        }
        return false;
    }

    public boolean deleteSerie(int idSerie) {
        if (serieFound(idSerie) != -1) {
            // admin.setSeries(listSeries);
            listSeries.remove(listSeries.get(serieFound(idSerie)));
            return fm.reWriteFile("series", listSeries);
        }
        return false;
    }

    public boolean addMovie(String name, String author, String description, int duration, String nameCategory,
            String fileVideo, String coverImage) {
        if (addMultimediaValidation(name, author, 1)) {
            Movie newMovie = new Movie(assignid(), name, author, description, duration, nameCategory);
            newMovie.setFileVideo(fileVideo);
            newMovie.setCoverImage(coverImage);
            listMovies.add(newMovie);
            // admin.setMovies(listMovies);
            return fm.writeFile("movies", newMovie);
        }
        return false;
    }

    public boolean addSerie(String name, String author, String description, ArrayList<Season> seasons,
            String nameCategory) {
        if (addMultimediaValidation(name, author, 2)) {
            Serie newSerie = new Serie(assignidSerie(), name, author, description, seasons, nameCategory);
            listSeries.add(newSerie);
            currentSerie = newSerie;
            // admin.setSeries(listSeries);
            return fm.writeFile("series", newSerie);
        }
        return false;
    }

    public boolean addSeason(int idSerie, String nameSeason,
            ArrayList<MultimediaContent> listChapters) {
        int posSerie = serieFound(idSerie);
        if (posSerie != -1) {
            listSeries.get(posSerie).getSeasons()
                    .add(new Season(assignidSeason(idSerie), nameSeason, listChapters));
            fm.reWriteFile("series", listSeries);
            return true;
        }
        return false;
    }

    public void addChapter(String name, String description, int duration, int idSerie,
            int idSeason) {
        int serieIndex = serieFound(idSerie);
        int seasonIndex = seasonFound(idSeason, idSerie);

        if (serieIndex != -1 && seasonIndex != -1) {
            if (listSeries.get(serieIndex).getSeasons().get(seasonIndex).getchapters() == null) {
                listSeries.get(serieIndex).getSeasons().get(seasonIndex).setchapters(new ArrayList<>());
                listSeries.get(serieIndex).getSeasons().get(seasonIndex)
                        .addchapters((new MultimediaContent(assignidChapter(serieIndex, seasonIndex),
                                duration, name, description)));
            } else {
                listSeries.get(serieIndex).getSeasons().get(seasonIndex).getchapters().add(
                        (new MultimediaContent(assignidChapter(serieIndex, seasonIndex), duration, name, description)));
            }
            fm.reWriteFile("series", listSeries);
        }
    }

    public void addChapterName(String name, String description, int duration, int idSerie,
            String idSeason, String fileVideo, String coverimage) {
        int serieIndex = serieFound(idSerie);
        int seasonIndex = seasonNameFound(idSeason, idSerie);

        if (serieIndex != -1 && seasonIndex != -1) {
            if (listSeries.get(serieIndex).getSeasons().get(seasonIndex).getchapters() == null) {

                listSeries.get(serieIndex).getSeasons().get(seasonIndex).setchapters(new ArrayList<>());
                listSeries.get(serieIndex).getSeasons().get(seasonIndex)
                        .addchapters((new MultimediaContent(assignidChapter(serieIndex, seasonIndex),
                        name , duration, description, fileVideo, coverimage)));
            } else {
                listSeries.get(serieIndex).getSeasons().get(seasonIndex).getchapters().add(
                        (new MultimediaContent(assignidChapter(serieIndex, seasonIndex), name , duration, description, fileVideo, coverimage)));
            }
            fm.reWriteFile("series", listSeries);
        }
    }

    public boolean updateMovieInformation(Movie updateMovie) {
        for (Movie movie : listMovies) {
            if (movie.getId() == updateMovie.getId()) {
                movie = updateMovie;
            }
        }
        return fm.reWriteFile("movies", listMovies);
    }

    public ArrayList<Movie> getMovies() {
        return listMovies;
    }

    /*
     * public ChoiceBox <String> getDisponibleFileVideoNames(){
     * ChoiceBox <String> choiceBox = new ChoiceBox<>();
     * ArrayList <String> fileNames = fm.getFileVideoNames();
     * ArrayList <String> usedNames = new ArrayList<>();
     * 
     * //movies
     * for(Movie movie: listMovies){
     * usedNames.add(movie.getFileVideo());
     * }
     * // for(Serie serie: listSeries){
     * // usedNames.add(serie.getFileVideo());
     * // }
     * 
     * for(String used: usedNames){
     * fileNames.remove(used);
     * }
     * for(String names: fileNames){
     * choiceBox.getItems().add(names);
     * }
     * return choiceBox;
     * 
     * }
     */

    public int movieFound(int id) {
        for (int i = 0; i < listMovies.size(); i++) {
            if (listMovies.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public int serieFound(int idSerie) {
        for (int i = 0; i < listSeries.size(); i++) {
            if (listSeries.get(i).getId() == idSerie) {
                return i;
            }
        }
        return -1;
    }

    public int seasonFound(int idSeason, int idSerie) {
        for (int i = 0; i < listSeries.get(serieFound(idSerie)).getSeasons().size(); i++) {
            if (listSeries.get(serieFound(idSerie)).getSeasons().get(i).getId() == idSeason) {
                return i;
            }
        }
        return -1;
    }

    public int seasonNameFound(String idSeason, int idSerie) {
        for (int i = 0; i < listSeries.get(serieFound(idSerie)).getSeasons().size(); i++) {
            if (listSeries.get(serieFound(idSerie)).getSeasons().get(i).getSeasonName().equals(idSeason)) {
                return i;
            }
        }
        return -1;
    }

    public int chapterFound(int idSeason, int idSerie, int idChapter) {
        for (int i = 0; i < listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie))
                .getchapters().size(); i++) {
            if (listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie))
                    .getchapters().get(i).getId() == idChapter) {
                return i;
            }
        }
        return -1;
    }

    public int chapterNameFound(String idSeason, int idSerie, String idChapter) {
        for (int i = 0; i < listSeries.get(serieFound(idSerie)).getSeasons().get(seasonNameFound(idSeason, idSerie))
                .getchapters().size(); i++) {
            if (listSeries.get(serieFound(idSerie)).getSeasons().get(seasonNameFound(idSeason, idSerie))
                    .getchapters().get(i).getName().equals(idChapter)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<String> ShowListMovieNames() {
        ArrayList<String> movieNames = new ArrayList<>();
        for (MultimediaContent movie : listMovies) {
            movieNames.add(movie.getName() + "-" + movie.getId());
        }
        return movieNames;
    }

    public ArrayList<String> ShowListSeriesNames() {
        ArrayList<String> serieNames = new ArrayList<>();
        for (Serie serie : listSeries) {
            serieNames.add(serie.getName() + "-" + serie.getId());
        }
        return serieNames;
    }

    public String[] showSeasonsNames(int idSerie) {
        ArrayList<String> aux = new ArrayList<>();
        for (Serie s : listSeries) {
            if (s.getId() == idSerie) {
                for (Season se : s.getSeasons()) {
                    aux.add(se.getSeasonName());
                }
            }
        }
        String[] seasonNames = new String[aux.size()];
        for (int i = 0; i < aux.size(); i++) {
            seasonNames[i] = aux.get(i);
        }
        return seasonNames;
    }

    public String[] chapterNames(int idSerie, String seasonName) {
        ArrayList<String> aux = new ArrayList<>();
        for (Serie s : listSeries) {
            if (s.getId() == idSerie) {
                for (Season se : s.getSeasons()) {
                    if (se.getSeasonName().equals(seasonName)) {
                        for (MultimediaContent chap : se.getchapters()) {
                            aux.add(chap.getName());
                        }
                    }
                }
            }
        }
        String[] chapNames = new String[aux.size()];
        for (int i = 0; i < aux.size(); i++) {
            chapNames[i] = aux.get(i);
        }
        return chapNames;
    }

    public MultimediaContent chapterForAwayMenu(int idSerie, String seasonName, String chapName) {
        for (Serie s : listSeries) {
            if (s.getId() == idSerie) {
                for (Season se : s.getSeasons()) {
                    if (se.getSeasonName().equals(seasonName)) {
                        for (MultimediaContent chap : se.getchapters()) {
                            if (chap.getName().equals(chapName)) {
                                return chap;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String ShowMovies(int movieId) {
        return listMovies.get(movieFound(movieId)).toString();
    }

    public boolean modifyMovies(Movie movieToUpdate, String description, String name, int duration, String author,
            int Selected) {
        int aux = movieFound(Selected);

        if (aux != -1) {
            listMovies.get(aux).setAuthor(author);

            listMovies.get(aux).setDescription(description);

            listMovies.get(aux).setName(name);

            listMovies.get(aux).setDuration(duration);

            admin.setMovies(listMovies);
            return true;
        }
        return false;
    }

    public ArrayList<Season> createSeasons(int idSerie, String name,
            ArrayList<MultimediaContent> seasonMultimediaContent) {
        ArrayList<Season> listSeasons = new ArrayList<Season>();
        listSeasons.add(new Season(assignidSeason(idSerie), name, seasonMultimediaContent));
        return listSeasons;
    }

    public MultimediaContent createChapter(String name, String description, int duration) {
        ArrayList<MultimediaContent> listchapters = new ArrayList<MultimediaContent>();
        MultimediaContent mc = new MultimediaContent(assignidCreateChapter(listchapters), duration, name, description);

        return mc;
    }

    public void deleteSeason(int idSeason, int idSerie) {

        listSeries.get(serieFound(idSerie)).getSeasons()
                .remove(listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie)));

    }

    public void deleteSeasonName(String idSeason, int idSerie) {

        listSeries.get(serieFound(idSerie)).getSeasons()
                .remove(listSeries.get(serieFound(idSerie)).getSeasons().get(seasonNameFound(idSeason, idSerie)));
        fm.reWriteFile("series", listSeries);

    }

    public void deleteChapter(int idSeason, int idSerie, int idChapter) {

        listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie))
                .getchapters()
                .remove(listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie))
                        .getchapters().get(chapterFound(idSeason, idSerie, idChapter)));
        fm.reWriteFile("season", listSeries);

    }

    public void deleteChapterName(String idSeason, int idSerie, String idChapter) {

        listSeries.get(serieFound(idSerie)).getSeasons().get(seasonNameFound(idSeason, idSerie))
                .getchapters()
                .remove(listSeries.get(serieFound(idSerie)).getSeasons().get(seasonNameFound(idSeason, idSerie))
                        .getchapters().get(chapterNameFound(idSeason, idSerie, idChapter)));
        fm.reWriteFile("series", listSeries);

    }

    public ArrayList<String> ShowListSeasonNames(int idSerie) {
        ArrayList<String> seasonNames = new ArrayList<>();
        for (Season season : listSeries.get(serieFound(idSerie)).getSeasons()) {
            seasonNames.add(season.getSeasonName());
        }
        return seasonNames;
    }

    public ArrayList<String> showListChapterNames(int idSerie, int idSeason) {
        ArrayList<String> chaptersNames = new ArrayList<>();
        for (MultimediaContent multimediaContent : listSeries.get(serieFound(idSerie)).getSeasons()
                .get(seasonFound(idSeason, idSerie)).getchapters()) {
            chaptersNames.add(multimediaContent.getName());
        }
        return chaptersNames;
    }

    public String showSeason(int idSeason, int idSerie) {

        return listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie)).toString();

    }

    public String showChapters(int idSeason, int idSerie, int idChapter) {
        return listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie))
                .getchapters().get(chapterFound(idSeason, idSerie, idChapter)).toString();
    }

    public boolean modifySeries(String description, String name, String author, String category, int Selected) {
        int aux = serieFound(Selected);

        if (aux != -1) {
            listSeries.get(aux).setAuthor(author);
            listSeries.get(aux).setDescription(description);
            listSeries.get(aux).setName(name);
            listSeries.get(aux).setCategory(category);
            admin.setSeries(listSeries);
            fm.reWriteFile("series", listSeries);
            currentSerie = new Serie(aux, name, author, description, null, category);
            return true;
        }
        return false;
    }

    public boolean modifyChapters(String description, String name, int duration, int Selected, int idSeason,
            int idChapter) {
        int aux = serieFound(Selected);
        int auxSeason = seasonFound(idSeason, Selected);
        int auxChapter = chapterFound(idSeason, Selected, idChapter);

        if (aux != -1) {
            listSeries.get(aux).getSeasons().get(auxSeason).getchapters().get(auxChapter)
                    .setDescription(description);

            listSeries.get(aux).getSeasons().get(auxSeason).getchapters().get(auxChapter)
                    .setName(name);

            listSeries.get(aux).getSeasons().get(auxSeason).getchapters().get(auxChapter)
                    .setDuration(duration);

            return true;
        }
        return false;
    }

    public boolean modifyChaptersName(String description, String name, int duration, int Selected, String idSeason,
            String idChapter) {
        int aux = serieFound(Selected);
        int auxSeason = seasonNameFound(idSeason, Selected);
        int auxChapter = chapterNameFound(idSeason, Selected, idChapter);

        if (aux != -1) {
            listSeries.get(aux).getSeasons().get(auxSeason).getchapters().get(auxChapter)
                    .setDescription(description);

            listSeries.get(aux).getSeasons().get(auxSeason).getchapters().get(auxChapter)
                    .setName(name);

            listSeries.get(aux).getSeasons().get(auxSeason).getchapters().get(auxChapter)
                    .setDuration(duration);
            fm.reWriteFile("series", listSeries);
            return true;

        }
        return false;
    }

    public boolean modifySeason(String nameSeasonNew, int Selected, int idSeason) {
        int aux = serieFound(Selected);
        int auxSeason = seasonFound(idSeason, Selected);

        if (aux != -1) {
            listSeries.get(aux).getSeasons().get(auxSeason).setSeasonName(nameSeasonNew);
            return true;
        }
        return false;
    }

    public boolean modifySeasonName(String nameSeasonNew, int Selected, String idSeason) {
        int aux = serieFound(Selected);
        int auxSeason = seasonNameFound(idSeason, Selected);

        if (aux != -1) {

            listSeries.get(aux).getSeasons().get(auxSeason).setSeasonName(nameSeasonNew);
            fm.reWriteFile("series", listSeries);

            return true;
        }
        return false;
    }

    public String showSeries(int nameSerie) {
        return listSeries.get(serieFound(nameSerie)).toString();
    }

    public int assignid() {
        return listMovies.isEmpty() ? 1 : listMovies.get(listMovies.size() - 1).getId() + 1;
    }

    public int assignidSerie() {
        return listSeries.isEmpty() ? 1 : listSeries.get(listSeries.size() - 1).getId() + 1;
    }

    public int assignidChapter(int serieIndex, int seasonIndex) {
        ArrayList<MultimediaContent> chapters = listSeries.get(serieIndex).getSeasons().get(seasonIndex)
                .getchapters();
        return chapters.isEmpty() ? 1 : chapters.get(chapters.size() - 1).getId() + 1;
    }

    public int assignidCreateChapter(ArrayList<MultimediaContent> chapters) {
        return chapters.isEmpty() ? 1 : chapters.get(chapters.size() - 1).getId() + 1;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public ArrayList<Serie> getListSeries() {
        return listSeries;
    }

    public Serie getCurrentSerie() {
        return currentSerie;
    }

    public void setCurrentSerie(Serie currentSerie) {
        this.currentSerie = currentSerie;
    }

    public void setListMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
    }

    public void setListSeries(ArrayList<Serie> listSeries) {
        this.listSeries = listSeries;
    }

    public Serie serieFoundRapidly(int id) {
        for (Serie s : listSeries) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public boolean validateNameChapter(String nameSeason, int idSerie, String nameChapter) {
        for (Serie serie : listSeries) {
            if (serie.getId() == idSerie) {
                for (Season season : serie.getSeasons()) {
                    if (season.getSeasonName().equals(nameSeason)) {
                        for (MultimediaContent chapter : season.getchapters())
                            if (chapter.getName().equalsIgnoreCase(nameChapter)) {
                                return false;
                            }
                    }
                }
            }
        }
        if (nameSeason.length() < 3) {
            return false;
        }
        return true;
    }

    public boolean validateNumbers(String aux) {
        int duration = 0;
        try {
            duration = Integer.parseInt(aux);
        } catch (NumberFormatException e) {
            return false;
        }
        if (duration == 0 || duration < 10) {
            return false;
        }
        return true;
    }

    public boolean validateNumbersDigits(String digit) {
        if (digit.length() < 6) {
            return true;
        }
        return false;
    }
    public boolean validateName(String aux) {
        return (aux.length() > 2);
    }

    public void showErrorTimeline(TextField textField, Label errorLabel, String mensaje) {
        // Cambiar el color del texto del error a rojo
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        errorLabel.setText(mensaje);

        // Aplicar un borde rojo al TextField
        textField.setStyle("-fx-border-color: red");

        // Definir la duración y acción para limpiar el mensaje de error después de un
        // tiempo
        Duration duration = Duration.seconds(4);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            errorLabel.setText("");
            // Restaurar el estilo y efecto del TextField
            textField.setStyle("");
            textField.setEffect(null);
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void showErrorTimelineChoiceBox(ChoiceBox<String> choiceBox, Label errorLabel, String mensaje) {
        // Cambiar el color del texto del error a rojo
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        errorLabel.setText(mensaje);

        // Cambiar el estilo del ChoiceBox para resaltar su importancia
        choiceBox.setStyle("-fx-border-color: red;"); // Cambiar el color del borde a rojo

        // Definir la duración y acción para limpiar el mensaje de error después de un
        // tiempo
        Duration duration = Duration.seconds(4);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            errorLabel.setText("");
            // Restaurar el estilo del ChoiceBox
            choiceBox.setStyle("");
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void showErrorTimelineIntComboBox(ComboBox<Integer> intBox, Label errorLabel, String mensaje) {
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        errorLabel.setText(mensaje);    
        intBox.setStyle("-fx-border-color: red;"); 
        Duration duration = Duration.seconds(4);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            errorLabel.setText("");
            intBox.setStyle("");
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void showErrorTimelineFile(Button selected, Label errorLabel, String mensaje) {
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        errorLabel.setText(mensaje);    
        selected.setStyle("-fx-border-color: red;"); 
        Duration duration = Duration.seconds(4);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            errorLabel.setText("");
            selected.setStyle("");
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void showErrorTimelineStringComboBox(ComboBox<String> stringBox, Label errorLabel, String mensaje) {
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        errorLabel.setText(mensaje);    
        stringBox.setStyle("-fx-border-color: red;"); 
        Duration duration = Duration.seconds(4);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            errorLabel.setText("");
            stringBox.setStyle("");
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public boolean containCharacterSpecial(String str) {
        int x = 0;
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).equals("1")
                    || String.valueOf(str.charAt(i)).equals("2")
                    || String.valueOf(str.charAt(i)).equals("3")
                    || String.valueOf(str.charAt(i)).equals("4")
                    || String.valueOf(str.charAt(i)).equals("5")
                    || String.valueOf(str.charAt(i)).equals("6")
                    || String.valueOf(str.charAt(i)).equals("7")
                    || String.valueOf(str.charAt(i)).equals("8")
                    || String.valueOf(str.charAt(i)).equals("9")
                    || String.valueOf(str.charAt(i)).equals("0")) {
                x++;
            }
        }
        return str.matches("^[a-zA-Z0-9\\s]+$") && x == 0;

    }

    public boolean validateDescription(String aux) {
        return (aux.length() > 4);
    }

    public boolean validarSinCharacterSpecial(String input) {
        String patron = "^[a-zA-Z\\s]*$";
        return input.matches(patron);
    }

    public boolean validateCharacterSpecialAllowNumberSpaceBlank(String input) {
        String patron = "^[a-zA-Z0-9\\s]*$";
        return input.matches(patron);
    }

    public boolean validateWithoutSpecialCharacter(String input) {
        String patron = "^[a-zA-Z\\s,.:;]*$";
        return input.matches(patron);
    }

    public boolean validateWithNumber(String input) {
        String patron = "^[a-zA-Z0-9\\s]*$";
        return input.matches(patron);
    }

    
    public boolean validateNameSeason(String nameSeason, int idSerie) {
        for (Serie serie : listSeries) {
            if (serie.getId() == idSerie) {
                for (Season season : serie.getSeasons()) {
                    if (season.getSeasonName().equalsIgnoreCase(nameSeason)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deletefirst(int idSerie) {
        if (listSeries.get(serieFound(idSerie)).getSeasons().size() == 1) {
            return true;
        }
        return false;
    }

    public boolean deletefirstchapter(int idSeason, int idSerie) {
        if (listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(idSeason, idSerie))
                .getchapters().size() == 1) {
            return true;
        }
        return false;
    }

    public String validateHaveChapter(int idSerie) {
        // Obtener la serie correspondiente al ID proporcionado
        Serie serie = listSeries.get(serieFound(idSerie));

        // Recorrer todas las temporadas de la serie
        for (Season season : serie.getSeasons()) {
            // Verificar si la temporada no tiene capítulos
            if (season.getchapters() == null || season.getchapters().isEmpty()) {
                return season.getSeasonName();
            }
        }

        return null;
    }

    public boolean validateHaveSeason(int idSerie) {
        Serie serie = listSeries.get(serieFound(idSerie));

        // Verificar si la serie no tiene temporadas
        if (serie.getSeasons().isEmpty()) {
            return false; // No hay temporadas en la serie
        }

        return true; // La serie tiene al menos una temporada
    }

    public int assignidSeason(int idSerie) {
        for (int i = 0; i < listSeries.size(); i++) {
            if (listSeries.get(i).getId() == idSerie) {
                int size = listSeries.get(i).getSeasons().size();
                return listSeries.get(i).getSeasons().isEmpty() ? 1
                        : listSeries.get(i).getSeasons().get(size - 1).getId() + 1;
            }
        }
        return 0;
    }

    // Num 1 to movies, num 2 to series
    public boolean addMultimediaValidation(String name, String author, int num) {
        switch (num) {
            case 1:
                for (Movie movie : listMovies) {
                    if (movie.getName().equals(name) && movie.getAuthor().equals(author)) {
                        return false;
                    }
                }
                return true;
            case 2:
                for (Serie serie : listSeries) {
                    if (serie.getName().equals(name) && serie.getAuthor().equals(author)) {
                        return false;
                    }
                }
                return true;
        }
        return false;
    }
}