package co.edu.uptc.persistence.managerClasses;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.edu.uptc.model.Subscription;

public class SubscriptionsManagement {

    @SuppressWarnings("unchecked")
    public void addSubscription(Subscription sub) {

        JSONObject atributes = new JSONObject();

        atributes.put("name", sub.getName());
        atributes.put("description", sub.getDescription());
        atributes.put("price",(double) sub.getPrice());
        atributes.put("duration", sub.getDuration());

        JSONObject subObject = new JSONObject();
        subObject.put("subscription", atributes);

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir") + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\subscriptions.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray subscriptionsList = (JSONArray) currentJSON.get("subscriptions");

            subscriptionsList.add(subObject);

            currentJSON.put("subscriptions", subscriptionsList);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir") + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\subscriptions.json")) {
                file.write(currentJSON.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Subscription> getSubscriptions() {
        ArrayList<Subscription> subsArray = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try (FileReader reader = new FileReader(System.getProperty("user.dir")+ "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\subscriptions.json")) {
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            System.out.println("Sumn went wrong");
        }

        JSONArray subs = (JSONArray) jsonObject.get("subscriptions");
        for (Object sub : subs) {
            JSONObject s = (JSONObject) sub;

            JSONObject so = (JSONObject) s.get("subscription");
            long duration = (long) so.get("duration");
            String name = (String) so.get("name");
            String description = (String) so.get("description");
            double price = (double) so.get("price");

            Subscription newSub = new Subscription(name, (int) duration, description, price);
            subsArray.add(newSub);
        }
        return subsArray;
    }

    @SuppressWarnings("unchecked")
    public void updateSubscription(Subscription subToUpdate, Subscription subUpdated) {
        JSONObject atributessubToUpdate = new JSONObject();

        atributessubToUpdate.put("duration", (long) subToUpdate.getDuration());
        atributessubToUpdate.put("name", subToUpdate.getName());
        atributessubToUpdate.put("description", subToUpdate.getDescription());
        atributessubToUpdate.put("price", (double) subToUpdate.getPrice());

        JSONObject subToUpdateObject = new JSONObject();
        subToUpdateObject.put("subscription", atributessubToUpdate);

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir") + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\subscriptions.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray subsList = (JSONArray) currentJSON.get("subscriptions");
            subsList.remove(subToUpdateObject);

            JSONObject subUpdatedAtributes = new JSONObject();

            subUpdatedAtributes.put("price",(double) subUpdated.getPrice());
            subUpdatedAtributes.put("name", subUpdated.getName());
            subUpdatedAtributes.put("description", subUpdated.getDescription());
            subUpdatedAtributes.put("duration", subUpdated.getDuration());
            currentJSON.put("subscriptions", subsList);

            JSONObject subUpdatedObject = new JSONObject();
            subUpdatedObject.put("subscription", subUpdatedAtributes);

            subsList.add(subUpdatedObject);
            currentJSON.put("subscriptions", subsList);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\subscriptions.json")) {
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
    public void removeSubscription(Subscription subToRemove){
        JSONObject atributessubToRemove = new JSONObject();

        atributessubToRemove.put("duration", (long) subToRemove.getDuration());
        atributessubToRemove.put("name", subToRemove.getName());
        atributessubToRemove.put("description", subToRemove.getDescription());
        atributessubToRemove.put("price", (double) subToRemove.getPrice());

        JSONObject subToRemoveObject = new JSONObject();
        subToRemoveObject.put("subscription", atributessubToRemove);

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(System.getProperty("user.dir") + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\subscriptions.json")) {
            Object objAux = jsonParser.parse(reader);
            JSONObject currentJSON = (JSONObject) objAux;

            JSONArray subsList = (JSONArray) currentJSON.get("subscriptions");
            subsList.remove(subToRemoveObject);

            currentJSON.put("subscriptions", subsList);

            try (FileWriter file = new FileWriter(System.getProperty("user.dir")
                    + "\\MULTIMEDIA PROJECT\\src\\co\\edu\\uptc\\persistence\\files\\administratorFile\\subscriptions.json")) {
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
