package co.edu.uptc;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.MultimediaContent;
import co.edu.uptc.model.Season;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void MoviesPersistence() {
        AdminController ac = new AdminController();
        // ac.addMovie("prueba", "prueba", "prueba", 122);
        System.out.println("delete");
        ac.deleteMovie(0);
        ac.getMovies().forEach(System.out::println);
    }

    @Test
    public void SeriesPersistence() {
        AdminController ac = new AdminController();
        // ac.addSerie("prueba", "prueba", "prueba", new ArrayList<Season>());
        // ac.deleteSerie(44324);
        // ac.addSeason(784, "temp1", new ArrayList<MultimediaContent>());
        // ac.addChapter("cap2", "des", 60, 784, "temp1");
        // ac.deleteChapter("temp1", 783, "cap2");
        // ac.addSerie("prueba2", "prueba", "prueba", new ArrayList<Season>());
        // ac.getMovies().forEach(System.out::println);
        ac.addSerie("Serie1", "Su papa", "asdfasdf", new ArrayList<Season>(), "Jum");
    }

    @Test
    public void UsersPersistence() {
        UserRegisteredController uc = new UserRegisteredController();

        uc.addUser("yovani ", "basquez", 1234, "jusad");
        uc.getListUsers();
    }
}
