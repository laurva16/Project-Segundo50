package co.edu.uptc.persistence.managerClasses;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.edu.uptc.model.Category;
import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;

public class CategoryManagement {
    @SuppressWarnings("unchecked")
    public void addCategory(Category category) {

        JSONObject atributes = new JSONObject();

        atributes.put("name", category.getName());

        JSONArray movies = new JSONArray();
        JSONArray series = new JSONArray();

        atributes.put("movies", movies);
        atributes.put("series", series);

        JSONObject categoryObj = new JSONObject();
        categoryObj.put("category", atributes);

        // Reads if JSON exists
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\categories.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray categories = (JSONArray) currentJSON.get("categories");

            categories.add(categoryObj);

            currentJSON.put("categories", categories);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\categories.json")) {
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
    public void updateCategories(ArrayList<Category> categoriesArray) {
        JSONArray categories = new JSONArray();

        for (Category c : categoriesArray) {
            JSONObject atributes = new JSONObject();

            atributes.put("name", c.getName());

            JSONArray movies = new JSONArray();

            for (Movie movieAux : c.getMovies()) {
                JSONObject movieAtributes = new JSONObject();
                movieAtributes.put("id", (long) movieAux.getId());
                movieAtributes.put("duration", (long) movieAux.getDuration());
                movieAtributes.put("name", movieAux.getName());
                movieAtributes.put("description", movieAux.getDescription());
                movieAtributes.put("author", movieAux.getAuthor());

                JSONObject movieObj = new JSONObject();
                movieObj.put("movie", movieAtributes);
                movies.add(movieObj);
            }

            JSONArray series = new JSONArray();

            for (Serie serieAux : c.getSeries()) {
                JSONObject serieAtributes = new JSONObject();

                serieAtributes.put("id", serieAux.getId());
                serieAtributes.put("name", serieAux.getName());
                serieAtributes.put("author", serieAux.getAuthor());
                serieAtributes.put("description", serieAux.getDescription());

                JSONArray seasonsList = new JSONArray();

                for (Season i : serieAux.getSeasons()) {
                    JSONArray chapterList = new JSONArray();

                    JSONObject seasonAtributes = new JSONObject();
                    seasonAtributes.put("seasonName", i.getSeasonName());

                    for (MultimediaContent m : i.getSeasonMultimediaContent()) {
                        JSONObject chapterAtributes = new JSONObject();
                        chapterAtributes.put("duration", (long) m.getDuration());
                        chapterAtributes.put("name", m.getName());
                        chapterAtributes.put("description", m.getDescription());

                        JSONObject chapterObject = new JSONObject();
                        chapterObject.put("chapter", chapterAtributes);
                        chapterList.add(chapterObject);
                    }

                    seasonAtributes.put("chapters", chapterList);
                    JSONObject seasonObject = new JSONObject();
                    seasonObject.put("season", seasonAtributes);
                    seasonsList.add(seasonObject);
                }

                serieAtributes.put("seasons", seasonsList);
                JSONObject serieObj = new JSONObject();
                serieObj.put("serie", serieAtributes);
                series.add(serieObj);
            }

            atributes.put("movies", movies);
            atributes.put("series", series);

            JSONObject categoryObj = new JSONObject();
            categoryObj.put("category", atributes);
            categories.add(categoryObj);

        }

        // Reads if JSON exists
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\categories.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            currentJSON.put("categories", categories);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\categories.json")) {
                file.write(currentJSON.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categoriesArray = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\categories.json")) {
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            System.out.println("Sumn went wrong");
        }

        JSONArray categories = (JSONArray) jsonObject.get("categories");
        for (Object c : categories) {
            JSONObject category = (JSONObject) c;

            JSONObject catObj = (JSONObject) category.get("category");

            String name = (String) catObj.get("name");

            JSONArray movies = (JSONArray) catObj.get("movies");

            ArrayList<Movie> moviesArray = new ArrayList<>();

            // Movies
            for (Object movie : movies) {
                JSONObject movieObj = (JSONObject) movie;

                JSONObject movieObjAux = (JSONObject) movieObj.get("movie");

                long idMovie = (long) movieObjAux.get("id");
                long durationMovie = (long) movieObjAux.get("duration");
                String nameMovie = (String) movieObjAux.get("name");
                String authorMovie = (String) movieObjAux.get("author");
                String descriptionMovie = (String) movieObjAux.get("description");
                Movie m = new Movie((int) idMovie, nameMovie, authorMovie, descriptionMovie, (int) durationMovie);
                moviesArray.add(m);
            }

            ArrayList<Serie> seriesArray = new ArrayList<>();
            JSONArray series = (JSONArray) catObj.get("series");
            for (Object serie : series) {
                JSONObject serieObject = (JSONObject) serie;

                JSONObject serieObjectAux = (JSONObject) serieObject.get("serie");

                JSONArray seasonsList = (JSONArray) serieObjectAux.get("seasons");
                ArrayList<Season> seasons = new ArrayList<>();

                for (Object season : seasonsList) {
                    JSONObject seasonObject = (JSONObject) season;

                    JSONObject seasonObjectAux = (JSONObject) seasonObject.get("season");

                    JSONArray chaptersList = (JSONArray) seasonObjectAux.get("chapters");
                    ArrayList<MultimediaContent> chapters = new ArrayList<>();

                    for (Object chapter : chaptersList) {
                        JSONObject chapterObject = (JSONObject) chapter;

                        JSONObject chapterObjectAux = (JSONObject) chapterObject.get("chapter");

                        long durationChap = (long) chapterObjectAux.get("duration");
                        String nameChap = (String) chapterObjectAux.get("name");
                        String descriptionChap = (String) chapterObjectAux.get("description");

                        MultimediaContent newChap = new MultimediaContent((int) durationChap, nameChap,
                                descriptionChap);
                        chapters.add(newChap);
                    }

                    String nameSea = (String) seasonObjectAux.get("seasonName");

                    Season newSeason = new Season(nameSea, chapters);

                    seasons.add(newSeason);
                }

                long idSerie = (long) serieObjectAux.get("id");
                String nameSerie = (String) serieObjectAux.get("name");
                String authorSerie = (String) serieObjectAux.get("author");
                String descriptionSerie = (String) serieObjectAux.get("description");

                Serie newSerie = new Serie((int) idSerie, nameSerie, authorSerie, descriptionSerie, seasons);
                seriesArray.add(newSerie);

            }

            Category newCategory = new Category(name);
            newCategory.setMovies(moviesArray);
            newCategory.setSeries(seriesArray);
            categoriesArray.add(newCategory);

        }
        return categoriesArray;
    }
}

