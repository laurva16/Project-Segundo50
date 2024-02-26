package co.edu.uptc.controller;

import java.util.ArrayList;

import com.google.gson.JsonElement;

import co.edu.uptc.model.Admin;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;
import co.edu.uptc.utilities.FileManagement;

public class AdminController {

    private Admin admin;
    private Serie currentSerie; // Creo que hay que eliminar esto
    private ArrayList<Movie> listMovies;
    private ArrayList<Serie> listSeries;
    FileManagement fm = new FileManagement();

    public AdminController() {
        admin = new Admin();
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
            fm.reWriteFile("series", listSeries);
            return true;
        }
        return false;
    }

    public void addMovie(String name, String author, String description, int duration, String nameCategorytegory) {
        Movie newMovie = new Movie(assignid(), name, author, description, duration, nameCategorytegory);
        listMovies.add(newMovie);
        // admin.setMovies(listMovies);
        fm.writeFile("movies", newMovie);
    }

    public void addSerie(String name, String author, String description, ArrayList<Season> seasons,
            String nameCategory) {
        Serie newSerie = new Serie(assignidSerie(), name, author, description, seasons, nameCategory);
        listSeries.add(newSerie);
        // admin.setSeries(listSeries);
        fm.writeFile("series", newSerie);
    }

    public boolean addSeason(int idSerie, String nameSeason, ArrayList<MultimediaContent> listChapters) {
        if (serieFound(idSerie) != -1) {
            listSeries.get(serieFound(idSerie)).getSeasons().add(new Season(nameSeason, listChapters));
            fm.reWriteFile("series", listSeries);
            return true;
        }
        return false;
    }

    public void addChapter(String name, String description, int duration, int idSerie,
            String nameSeason) {
        int serieIndex = serieFound(idSerie);
        int seasonIndex = seasonFound(nameSeason, idSerie);

        if (serieIndex != -1 && seasonIndex != -1) {

            listSeries.get(serieIndex).getSeasons().get(seasonIndex)
                    .getchapters()
                    .add(new MultimediaContent(assignidChapter(serieIndex, seasonIndex),
                            duration, name,
                            description));
            fm.reWriteFile("series", listSeries);
        }
    }

    public ArrayList<Movie> getMovies() {
        return listMovies;
    }

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

    public int seasonFound(String nameSeason, int idSerie) {
        for (int i = 0; i < listSeries.get(serieFound(idSerie)).getSeasons().size(); i++) {
            if (listSeries.get(serieFound(idSerie)).getSeasons().get(i).getSeasonName().equals(nameSeason)) {
                return i;
            }
        }
        return -1;
    }

    public int chapterFound(String nameSeason, int idSerie, String nameChapter) {
        for (int i = 0; i < listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(nameSeason, idSerie))
                .getchapters().size(); i++) {
            if (listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(nameSeason, idSerie))
                    .getchapters().get(i).getName().equals(nameChapter)) {
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

    public ArrayList<Season> createSeasons(String name, ArrayList<MultimediaContent> seasonMultimediaContent) {

        ArrayList<Season> listSeasons = new ArrayList<Season>();

        listSeasons.add(new Season(name, seasonMultimediaContent));

        return listSeasons;

    }

    public ArrayList<MultimediaContent> createChapter(String name, String description, int duration) {
        ArrayList<MultimediaContent> listchapters = new ArrayList<MultimediaContent>();

        listchapters.add(new MultimediaContent(assignidCreateChapter(listchapters), duration, name, description));

        return listchapters;
    }

    public void deleteSeason(String nameSeason, int idSerie) {

        listSeries.get(serieFound(idSerie)).getSeasons()
                .remove(listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(nameSeason, idSerie)));

    }

    public void deleteChapter(String nameSeason, int idSerie, String nameChapter) {

        listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(nameSeason, idSerie))
                .getchapters()
                .remove(listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(nameSeason, idSerie))
                        .getchapters().get(chapterFound(nameSeason, idSerie, nameChapter)));
        fm.reWriteFile("season", listSeries);

    }

    public ArrayList<String> ShowListSeasonNames(int idSerie) {
        ArrayList<String> seasonNames = new ArrayList<>();
        for (Season season : listSeries.get(serieFound(idSerie)).getSeasons()) {
            seasonNames.add(season.getSeasonName());
        }
        return seasonNames;
    }

    public ArrayList<String> showListChapterNames(int idSerie, String nameSeason) {
        ArrayList<String> chaptersNames = new ArrayList<>();
        for (MultimediaContent multimediaContent : listSeries.get(serieFound(idSerie)).getSeasons()
                .get(seasonFound(nameSeason, idSerie)).getchapters()) {
            chaptersNames.add(multimediaContent.getName());
        }
        return chaptersNames;
    }

    public String showSeason(String nameSeason, int idSerie) {

        return listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(nameSeason, idSerie)).toString();

    }

    public String showChapters(String nameSeason, int nameSerie, String nameChapter) {
        return listSeries.get(serieFound(nameSerie)).getSeasons().get(seasonFound(nameSeason, nameSerie))
                .getchapters().get(chapterFound(nameSeason, nameSerie, nameChapter)).toString();
    }

    public boolean modifySeries(String description, String name, String author, int Selected) {
        int aux = serieFound(Selected);

        if (aux != -1) {
            listSeries.get(aux).setAuthor(author);

            listSeries.get(aux).setDescription(description);

            listSeries.get(aux).setName(name);

            admin.setSeries(listSeries);
            return true;
        }
        return false;
    }

    public boolean modifyChapters(String description, String name, int duration, int Selected, String SelectedSeason,
            String SelectedChapter) {
        int aux = serieFound(Selected);
        int auxSeason = seasonFound(SelectedSeason, Selected);
        int auxChapter = chapterFound(SelectedSeason, Selected, SelectedChapter);

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

    public boolean modifySeason(String nameSeasonNew, int Selected, String SelectedSeason) {
        int aux = serieFound(Selected);
        int auxSeason = seasonFound(SelectedSeason, Selected);

        if (aux != -1) {
            listSeries.get(aux).getSeasons().get(auxSeason).setSeasonName(nameSeasonNew);

            return true;
        }
        return false;
    }

    public String showSeries(int nameSerie) {
        return listSeries.get(serieFound(nameSerie)).toString();
    }

    public int assignid() {
        return listMovies.isEmpty() ? 1 : listMovies.size() + 1;
    }

    public int assignidSerie() {
        return listSeries.isEmpty() ? 1 : listSeries.size() + 1;
    }

    public int assignidChapter(int serieIndex, int seasonIndex) {
        ArrayList<MultimediaContent> chapters = listSeries.get(serieIndex).getSeasons().get(seasonIndex)
                .getchapters();
        return chapters.isEmpty() ? 1 : chapters.size() + 1;
    }

    public int assignidCreateChapter(ArrayList<MultimediaContent> chapters) {
        return chapters.isEmpty() ? 1 : chapters.size() + 1;
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

    public boolean validateName(String aux) {
        return (aux.length() > 2);
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
        if (nameSeason.length() < 2) {
            return false;
        }
        return true;
    }

    public boolean deletefirst(int idSerie) {
        if (listSeries.get(serieFound(idSerie)).getSeasons().size() == 1) {
            return true;
        }
        return false;
    }

    public boolean deletefirstchapter(String nameSeason, int idSerie) {
        if (listSeries.get(serieFound(idSerie)).getSeasons().get(seasonFound(nameSeason, idSerie))
                .getchapters().size() == 1) {
            return true;
        }
        return false;
    }
}