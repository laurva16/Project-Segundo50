package co.edu.uptc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonElement;

import co.edu.uptc.model.Admin;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.PlayList;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;
import co.edu.uptc.model.UserRegistered;
import co.edu.uptc.utilities.FileManagement;

public class UserRegisteredController {

    private ArrayList<UserRegistered> listUsers = new ArrayList<>();
    private UserRegistered currentUser = new UserRegistered();
    private Admin admin;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Serie> series = new ArrayList<>();
    private UserRegistered userCreated;
    public FileManagement fm = new FileManagement();

    public UserRegisteredController() {
        readUserFile();
    }

    public void readUserFile() {
        listUsers = new ArrayList<>();
        FileManagement fm = new FileManagement();
        for (JsonElement je : fm.readJsonFile("users")) {
            UserRegistered ur = fm.getGson().fromJson(je, UserRegistered.class);
            listUsers.add(ur);
        }
    }

    public ArrayList<UserRegistered> getListUsers() {
        return listUsers;
    }

    public boolean couldLogIn(String userName, String password) {
        for (UserRegistered user : listUsers) {
            if (user.getUser().equals(userName) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    // encargado de la actualización de alguna de las variables de currentUser
    public boolean updateUserInformation( ){
        for(UserRegistered user : listUsers){
            if(user.getId() == currentUser.getId()){
                return fm.reWriteFile("users", listUsers);
            }
        }
        return false;
    }

    /*
     *  public boolean addMovieToPlayList(Movie object, String playlistName){
        for(PlayList list: currentUser.getplayList()){
            if(list.getName().equals(playlistName)){
                list.getMovies().add(object);
                return true;
            }
        }
        return true;
    }
     */
   

    public boolean idFound(int id) {
        for (UserRegistered i : listUsers) {
            if (i.getId() == id) {
                currentUser = i;
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserRegisteredController [listUsers=" + listUsers + ", currentUser=" + currentUser + "]";
    }

    public boolean userFound(String user) {
        for (UserRegistered i : listUsers) {
            if (i.getUser().equals(user)) {
                currentUser = i;
                return true;
            }
        }
        return false;
    }

    public boolean addUser(String firstName, String lastName, int id, String password) {
        UserRegistered r = new UserRegistered(firstName, lastName, id, firstName + id + "@uptc.edu.co", password);
        userCreated = r;
        listUsers.add(r);
        fm.reWriteFile("users", listUsers);
        return true;
    }

    public UserRegistered getUserCreated() {
        return userCreated;
    }

    public UserRegistered getCurrentuser() {
        return currentUser;
    }

    public boolean verifyPasswordAux(String password, String regex) {
        boolean rta = false;
        char[] passwordChar = password.toCharArray();
        for (int i = 0; i < passwordChar.length && true; i++) {
            if (String.valueOf(passwordChar[i]).matches(regex)) {
                rta = true;
                break;
            } else if (i == passwordChar.length - 1) {
                return false;
            }
        }
        return rta;
    }

    public boolean validatePassword(String password) {
        boolean rta = false;
        // al menos un número, una mayúscula, una minúscula y un carácter especial.
        if (verifyPasswordAux(password, "[0-9]")) {
            if (verifyPasswordAux(password, "[a-z]")) {
                if (verifyPasswordAux(password, "[A-Z]")) {
                    if (verifyPasswordAux(password, "[!@#$%^&*(),.?\":{}|<>]")) {
                        if (!verifyPasswordAux(password, "(?=\\\\\\\\\\\\\\\\S+$)")) {
                            if (password.matches(".{8,}$")) {
                                rta = true;
                            }
                        }
                    }
                }
            }
        }
        return rta;
    }

    public void playMovie(int duracionMilisegundos, String movieName) {
        System.out.println("Reproduciendo la película " + movieName + "...");
        try {
            Thread.sleep(duracionMilisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Película Finalizada");

    }

    public void playSerie(int duracionMilisegundos, String serieName) {
        System.out.println("Reproduciendo la serie " + serieName + "...");
        try {
            Thread.sleep(duracionMilisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Serie Finalizada");

    }

    // Buscar película
    public String[] getMovieNames() {
        String[] options = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            options[i] = movies.get(i).getName() + "-" + movies.get(i).getId();
        }
        return options;
    }

    // Buscar serie
    public String[] getSerieNames() {
        String[] options = new String[series.size()];
        for (int i = 0; i < series.size(); i++) {
            options[i] = series.get(i).getName() + "-" + series.get(i).getId();
        }
        return options;
    }

    // Mostrar película
    public Movie getMovie(int movieId) {
        for (Movie movie : movies) {
            if (movie.getId() == movieId) {
                return movie;
            }
        }
        return null;
    }

    // Mostrar serie
    public Serie getSerie(int serieId) {
        for (Serie serie : series) {
            if (serie.getId() == serieId) {
                return serie;
            }
        }
        return null;
    }

    public String[] serieSeasonsNames(int serieId) {
        Serie serie = getSerie(serieId);
        if (serie != null) {
            List<Season> seasons = serie.getSeasons();
            String[] names = new String[seasons.size()];

            for (int i = 0; i < seasons.size(); i++) {
                names[i] = seasons.get(i).getSeasonName();
            }

            return names;
        } else {
            return null;
        }
    }

    public String[] serieChapterNames(int serieId, String seasonName) {
        Serie serie = getSerie(serieId);

        if (serie != null) {
            Season targetSeason = null;

            // Buscar el capítulo específica
            for (Season season : serie.getSeasons()) {
                if (season.getSeasonName().equals(seasonName)) {
                    targetSeason = season;
                    break;
                }
            }

            if (targetSeason != null) {
                List<MultimediaContent> chapters = targetSeason.getchapters();
                String[] names = new String[chapters.size()];

                for (int i = 0; i < chapters.size(); i++) {
                    names[i] = chapters.get(i).getName();
                }

                return names;
            }
        } else {
            return null;
        }

        return null;
    }

    public MultimediaContent getSerieChapter(int serieId, String seasonName, String chapterName) {
        Serie serie = getSerie(serieId);

        if (serie != null) {
            Season targetSeason = null;

            // Buscar la temporada específica
            for (Season season : serie.getSeasons()) {
                if (season.getSeasonName().equals(seasonName)) {
                    targetSeason = season;
                    break;
                }
            }

            if (targetSeason != null) {
                // Buscar el capítulo específico
                for (MultimediaContent chapter : targetSeason.getchapters()) {
                    if (chapter.getName().equals(chapterName)) {
                        return chapter;
                    }
                }

                return null;
            }
        }

        return null;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<Serie> getSeries() {
        return series;
    }

    public UserRegistered currentUser(String currentUser) {
        for (UserRegistered i : listUsers) {
            if (i.getUser().equals(currentUser)) {
                return i;
            }
        }
        return null;
    }

    public void setCurrentUser(int currentUser) {
        for (UserRegistered i : listUsers) {
            if (i.getId() == currentUser) {
                this.currentUser = i;
            }
        }
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setSeries(ArrayList<Serie> series) {
        this.series = series;
    }

    public UserRegistered getCurrentUser() {
        return currentUser;
    }

    public boolean userValidation(String user) {
        String[] userArray = user.split("@");
        if (userArray.length == 2) {
            String domain = userArray[1];
            if (domain.equals("uptc.edu.co") || domain.equals("gmail.com") || domain.equals("outlook.com")
                    || domain.equals("yahoo.com")) {
                return true;
            }
        }
        return false;
    }

    public int generateId() {
        Random random = new Random();
        int newId;
        if (listUsers.isEmpty()) {
            return random.nextInt(9999) + 1;
        } else {
            newId = random.nextInt(9999) + 1;
            if (!idFound(newId)) {
                return newId;
            } else {
                generateId();
            }
        }
        return 0;
    }

    public boolean addUser(String firstName, String lastName, String user, String password) {
        if (!userFound(user)) {
            UserRegistered r = new UserRegistered(firstName, lastName, generateId(),
                    user, password);
            userCreated = r;
            listUsers.add(r);
            return true;
        }
        return false;
    }
}