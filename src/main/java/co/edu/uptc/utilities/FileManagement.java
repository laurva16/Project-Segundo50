package co.edu.uptc.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.awt.Desktop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.edu.uptc.model.UserRegistered;
import javafx.scene.control.ChoiceBox;

public class FileManagement {

    private File file;
    private PrintWriter pw;
    private final String filePath = "src\\main\\java\\co\\edu\\uptc\\persistence\\";
    private final String fileExtension = ".json";
    private Gson gson;
    private JsonArray fileArray;

    public FileManagement() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        fileArray = new JsonArray();
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

    // metodo usado para ingresar un nuevo objeto (movie-serie-usuario) a su
    // correspondiente JsonArray -> .json
    public boolean writeFile(String fileName, Object input) {
        file = new File(fileName);
        // validacion archivo vacio
        if (readJsonFile(fileName) != null) {
            fileArray = readJsonFile(fileName);
        }
        //
        JsonElement jElement = JsonParser.parseString(gson.toJson(input));
        fileArray.add(jElement);
        try {
            pw = new PrintWriter(new FileWriter(filePath + fileName + fileExtension, false));
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

    public boolean reWriteFile(String fileName, Object input) {
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

    public ChoiceBox<String> getFileMoviesNames() {
        ChoiceBox<String> listNames = new ChoiceBox<>();
        File folder = new File("src/multimediaVideos/Movies");

        File[] listFiles = folder.listFiles();

        for (File file : listFiles) {
            listNames.getItems().add(file.getName());
        }
        return listNames;
    }

    public ChoiceBox<String> getFileSeriesNames() {
        ChoiceBox<String> listNames = new ChoiceBox<>();
        File folder = new File("src/multimediaVideos/Series");

        File[] listFiles = folder.listFiles();

        for (File file : listFiles) {
            listNames.getItems().add(file.getName());
        }
        return listNames;
    }

    public void generatePaymentPdf(UserRegistered user) {
        Document document = new Document();
        String filePath = "src\\Payments\\payment_";
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        int factureID = new Random().nextInt(90000) + 10000;
        int nameFile = user.getId();
        try {

            PdfWriter.getInstance(document, new FileOutputStream(filePath + nameFile + ".pdf"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String time = fechaHoraActual.format(formatter);
            //
            document.open();

            document.add(new Paragraph("PAYMENT"));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Payment ID: " + factureID));
            document.add(new Paragraph("Date: " + time));
            document.add(new Paragraph("Client: " + user.getPayment().getNameCard().toString()));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Descripción                       Cantidad     Precio Unitario"));
            document.add(new Paragraph(user.getUser()));
            document.add(new Paragraph("Producto 2                        1                  $20.00"));
            document.add(new Paragraph("\n"));

            PdfPTable tabla = new PdfPTable(3);

            // Añadimos celdas a la tabla
            tabla.addCell(new Paragraph("ID"));
            tabla.addCell(new Paragraph("Nombre"));
            tabla.addCell(new Paragraph("Edad"));

            // Añadimos filas a la tabla
            tabla.addCell("1");
            tabla.addCell("Juan");
            tabla.addCell("25");
            tabla.addCell("2");
            tabla.addCell("María");
            tabla.addCell("30");

            // Añadimos la tabla al documento
            document.add(tabla);
            document.close();

            //ABRE EL PDF
            File pdfFile = new File(filePath + nameFile + ".pdf");
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(pdfFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException de) {
            de.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}