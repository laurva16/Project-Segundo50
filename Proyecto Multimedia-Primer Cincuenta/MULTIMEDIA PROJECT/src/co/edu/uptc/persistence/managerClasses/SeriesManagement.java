package co.edu.uptc.persistence.managerClasses;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.Serie;

public class SeriesManagement {

    @SuppressWarnings("unchecked")
    public void addSerie(Serie serie) {

        JSONObject atributes = new JSONObject();

        atributes.put("id", serie.getId());
        atributes.put("name", serie.getName());
        atributes.put("author", serie.getAuthor());
        atributes.put("description", serie.getDescription());

        JSONArray seasonsList = new JSONArray();

        for (Season i : serie.getSeasons()) {
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

        atributes.put("seasons", seasonsList);

        JSONObject serieObject = new JSONObject();
        serieObject.put("serie", atributes);

        // Reads if JSON exists
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\series.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray seriesList = (JSONArray) currentJSON.get("series");

            seriesList.add(serieObject);

            currentJSON.put("series", seriesList);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\series.json")) {
                file.write(currentJSON.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Serie> getSeries() {
        ArrayList<Serie> seriesArray = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\series.json")) {
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            System.out.println("Sumn went wrong");
        }

        JSONArray series = (JSONArray) jsonObject.get("series");
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

                    MultimediaContent newChap = new MultimediaContent((int) durationChap, nameChap, descriptionChap);
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
        return seriesArray;
    }

    @SuppressWarnings("unchecked")
    public void updateSerie(ArrayList<Serie> seriesArray) {
        JSONArray series = new JSONArray();

        for (Serie serieAux : seriesArray) {
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

        // Reads if JSON exists
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\series.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            currentJSON.put("series", series);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\series.json")) {
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
    public void removeSerie(Serie serieToRemove) {
        
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir")
                + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\series.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray seriesList = (JSONArray) currentJSON.get("series");
            Iterator<Object> iterator = seriesList.iterator();

            while (iterator.hasNext()) {
                Object s = iterator.next();
                JSONObject sObj = (JSONObject) s;
                JSONObject sObjAux = (JSONObject) sObj.get("serie");

                if ((long) sObjAux.get("id") == (long) serieToRemove.getId()) {
                    iterator.remove(); 
                    break;
                }
            }

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\series.json")) {
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