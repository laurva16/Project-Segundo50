package co.edu.uptc.persistence.managerClasses;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.edu.uptc.model.Movie;

public class MoviesManagement {

    // "Type safety: The method put(Object, Object) belongs to the raw type HashMap.
    // References to generic type HashMap<K,V> should be parameterizedJava"
    @SuppressWarnings("unchecked")
    public void addMovie(Movie movie) {

        JSONObject atributes = new JSONObject();

        atributes.put("id", movie.getId());
        atributes.put("name", movie.getName());
        atributes.put("author", movie.getAuthor());
        atributes.put("description", movie.getDescription());
        atributes.put("duration", movie.getDuration());

        JSONObject movieObject = new JSONObject();
        movieObject.put("movie", atributes);

        // Reads if JSON exists
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\movies.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray moviesList = (JSONArray) currentJSON.get("movies");

            moviesList.add(movieObject);

            currentJSON.put("movies", moviesList);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\movies.json")) {
                file.write(currentJSON.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> moviesArray = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\movies.json")) {
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            System.out.println("Sumn went wrong");
        }

        JSONArray movies = (JSONArray) jsonObject.get("movies");
        for (Object movie : movies) {
            JSONObject m = (JSONObject) movie;

            JSONObject ma = (JSONObject) m.get("movie");
            long id = (long) ma.get("id");
            String name = (String) ma.get("name");
            String author = (String) ma.get("author");
            String description = (String) ma.get("description");
            long duration = (long) ma.get("duration");

            Movie mo = new Movie((int) id, name, author, description, (int) duration);
            moviesArray.add(mo);
        }
        return moviesArray;
    }

    @SuppressWarnings("unchecked")
    public void updateMovie(Movie movieToUpdate, Movie movieUpdated) {
        JSONObject atributesMovieToUpdate = new JSONObject();

        atributesMovieToUpdate.put("id", (long) movieToUpdate.getId());
        atributesMovieToUpdate.put("name", movieToUpdate.getName());
        atributesMovieToUpdate.put("author", movieToUpdate.getAuthor());
        atributesMovieToUpdate.put("description", movieToUpdate.getDescription());
        atributesMovieToUpdate.put("duration", (long) movieToUpdate.getDuration());

        JSONObject movieToUpdateObject = new JSONObject();
        movieToUpdateObject.put("movie", atributesMovieToUpdate);

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\movies.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray moviesList = (JSONArray) currentJSON.get("movies");
            moviesList.remove(movieToUpdateObject);

            JSONObject movieUpdatedAtributes = new JSONObject();

            movieUpdatedAtributes.put("id", movieUpdated.getId());
            movieUpdatedAtributes.put("name", movieUpdated.getName());
            movieUpdatedAtributes.put("author", movieUpdated.getAuthor());
            movieUpdatedAtributes.put("description", movieUpdated.getDescription());
            movieUpdatedAtributes.put("duration", movieUpdated.getDuration());
            currentJSON.put("movies", moviesList);

            JSONObject movieUpdatedObject = new JSONObject();
            movieUpdatedObject.put("movie", movieUpdatedAtributes);

            moviesList.add(movieUpdatedObject);
            currentJSON.put("movies", moviesList);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\movies.json")) {
                file.write(currentJSON.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void removeMovie(Movie movieToRemove) {
        JSONObject atributesMovieToRemove = new JSONObject();

        atributesMovieToRemove.put("id", (long) movieToRemove.getId());
        atributesMovieToRemove.put("name", movieToRemove.getName());
        atributesMovieToRemove.put("author", movieToRemove.getAuthor());
        atributesMovieToRemove.put("description", movieToRemove.getDescription());
        atributesMovieToRemove.put("duration", (long) movieToRemove.getDuration());

        JSONObject movieToRemoveObject = new JSONObject();
        movieToRemoveObject.put("movie", atributesMovieToRemove);

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\movies.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray moviesList = (JSONArray) currentJSON.get("movies");
            moviesList.remove(movieToRemoveObject);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\movies.json")) {
                file.write(currentJSON.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
