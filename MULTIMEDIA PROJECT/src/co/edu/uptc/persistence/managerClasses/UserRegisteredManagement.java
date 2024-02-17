package co.edu.uptc.persistence.managerClasses;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.edu.uptc.model.Movie;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.PlayList;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;
import co.edu.uptc.model.UserRegistered;
import co.edu.uptc.model.UserSubscription;

public class UserRegisteredManagement {

    @SuppressWarnings("unchecked")
    public void addUser(UserRegistered user) {

        JSONObject atributes = new JSONObject();

        atributes.put("id", user.getId());
        atributes.put("firstName", user.getFirstName());
        atributes.put("lastName", user.getLastName());
        atributes.put("user", user.getUser());
        atributes.put("password", user.getPassword());
        atributes.put("subscription", user.getSub());

        JSONArray playLists = new JSONArray();

        atributes.put("playLists", playLists);

        JSONObject userObject = new JSONObject();
        userObject.put("user", atributes);

        // Reads if JSON exists
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\userFile\\users.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray userList = (JSONArray) currentJSON.get("users");

            userList.add(userObject);

            currentJSON.put("users", userList);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\userFile\\users.json")) {
                file.write(currentJSON.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserRegistered> getUsers() {
        ArrayList<UserRegistered> userArray = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\userFile\\users.json")) {
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            System.out.println("Sumn went wrong");
        }

        JSONArray users = (JSONArray) jsonObject.get("users");
        for (Object user : users) {
            JSONObject u = (JSONObject) user;

            JSONObject uo = (JSONObject) u.get("user");
            long id = (long) uo.get("id");
            String firstName = (String) uo.get("firstName");
            String lastName = (String) uo.get("lastName");
            String userString = (String) uo.get("user");
            String password = (String) uo.get("password");

            

            JSONArray playLists = (JSONArray) uo.get("playLists");
            // PLayList--Sumn coulda went wrong
            ArrayList<PlayList> playListArray = new ArrayList<>();
            for (Object playList : playLists) {
                JSONObject playListObj = (JSONObject) playList;

                JSONObject playListObjAux = (JSONObject) playListObj.get("playList");

                JSONArray movies = (JSONArray) playListObjAux.get("movies");
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

                // Series--Sum coulda went wrong
                ArrayList<Serie> seriesArray = new ArrayList<>();
                JSONArray series = (JSONArray) playListObjAux.get("series");
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

                PlayList p = new PlayList((String) playListObjAux.get("name"));
                p.setMovies(moviesArray);
                p.setSeries(seriesArray);
                playListArray.add(p);
            }

            
            UserRegistered ur = new UserRegistered(firstName, lastName, (int) id, userString, password);
            JSONObject sub = (JSONObject) uo.get("subscription");
            if(sub != null){
                long durationSub = (long) sub.get("duration");
                String nameSub = (String) sub.get("name");
                String descriptionSub = (String) sub.get("description");
                double priceSub = (double) sub.get("price");
                long startTimeSub = (long) sub.get("startTime");
                long endTimeSub = (long) sub.get("endTime");

                UserSubscription userSub = new UserSubscription(nameSub, (int) durationSub, descriptionSub, priceSub, startTimeSub, endTimeSub);
                ur.setSub(userSub);
            }
            
            ur.setplayList(playListArray);
            userArray.add(ur);
        }
        return userArray;
    }

    @SuppressWarnings("unchecked")
    public void updateUsers(ArrayList<UserRegistered> allusers) {
        JSONArray users = new JSONArray();

        for (UserRegistered user : allusers) {
            JSONObject atributes = new JSONObject();

            atributes.put("id", user.getId());
            atributes.put("firstName", user.getFirstName());
            atributes.put("lastName", user.getLastName());
            atributes.put("user", user.getUser());
            atributes.put("password", user.getPassword());

            
            JSONObject subAtributes = new JSONObject();
            if(user.getSub() != null){
                subAtributes.put("name", user.getSub().getName());
                subAtributes.put("description", user.getSub().getDescription());
                subAtributes.put("duration", (long) user.getSub().getDuration());
                subAtributes.put("price", (double) user.getSub().getPrice());
                subAtributes.put("startTime", (long) user.getSub().getStartTime());
                subAtributes.put("endTime", (long) user.getSub().getEndTime());

                atributes.put("subscription", subAtributes);
            }else{
                atributes.put("subscription", null);
            }
            
            


            JSONArray playLists = new JSONArray();

            for (PlayList playList : user.getplayList()) {
                JSONObject playListsAtributes = new JSONObject();
                JSONArray movies = new JSONArray();

                for (Movie movieAux : playList.getMovies()) {
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

                for (Serie serieAux : playList.getSeries()) {
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

                playListsAtributes.put("movies", movies);
                playListsAtributes.put("series", series);
                playListsAtributes.put("name", playList.getName());

                JSONObject playListObj = new JSONObject();
                playListObj.put("playList", playListsAtributes);
                playLists.add(playListObj);
            }
            atributes.put("playLists", playLists);

            JSONObject userObj = new JSONObject();
            userObj.put("user", atributes);
            users.add(userObj);
        }

        // Reads if JSON exists
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\userFile\\users.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            currentJSON.put("users", users);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\userFile\\users.json")) {
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