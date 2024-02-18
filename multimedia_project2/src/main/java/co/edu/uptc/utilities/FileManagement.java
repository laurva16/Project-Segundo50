package co.edu.uptc.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class FileManagement {

    private File file;
    private PrintWriter pw;
    private final String filePath = "src\\main\\java\\co\\edu\\uptc\\persistence\\";
    private final String fileExtension = ".json";
    private Gson gson;
    private JsonArray fileArray;

    public FileManagement() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        fileArray= new JsonArray();
    }

    public boolean createFile(String fileName) {
        file = new File(filePath + fileName + fileExtension);
        try {
            if (file.exists()) {
                return false;
            } else {
                pw = new PrintWriter(new FileWriter(file, false));
                pw.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //metodo usado para ingresar un nuevo objeto (movie-serie-usuario) a su correspondiente JsonArray -> .json
    public boolean writeFile(String fileName, Object input) {
        file = new File(fileName); 
         // validacion archivo vacio
        if(readJsonFile(fileName) != null){
            fileArray = readJsonFile(fileName);
        }
        //
        JsonElement jElement = JsonParser.parseString(gson.toJson(input));
        fileArray.add(jElement);
        try {  
            pw = new PrintWriter(new FileWriter(filePath + fileName + fileExtension, false));
            // pw.println(gson.toJson(fileArray)) es usado para utilizar el setPrettyPrinting()
            // pw.println(fileArray) todo en una linea
            pw.println(gson.toJson(fileArray));
            pw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JsonArray readJsonFile(String fileName) {
        file = new File(fileName);
        fileArray = new JsonArray();
        try {
            FileReader reader = new FileReader(filePath + file + fileExtension);
            JsonElement jElement = JsonParser.parseReader(reader);
            // validacion archivo vacio
            if (jElement.isJsonNull()) {
                return null;
            } else {
                fileArray = jElement.getAsJsonArray();
                return fileArray;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /*
     * public boolean reWriteFile(String fileName, Object input) {
        file = new File(fileName);
        try {
            pw = new PrintWriter(new FileWriter(filePath + fileName + fileExtension, false));
            pw.println(gson.toJson(input));
            pw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
     */

    public boolean deleteFile(String fileName) {
        file = new File(filePath + fileName + fileExtension);
        if (file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    public Gson getGson() {
        return gson;
    }

}
