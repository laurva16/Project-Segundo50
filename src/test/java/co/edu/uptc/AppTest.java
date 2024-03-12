package co.edu.uptc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Test;
import co.edu.uptc.controller.AdminController;
import co.edu.uptc.controller.UserRegisteredController;
import co.edu.uptc.model.Payment;
import co.edu.uptc.model.Season;
import co.edu.uptc.model.UserRegistered;
import co.edu.uptc.utilities.FileManagement;

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
        ac.deleteMovie(0);

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
       // ac.addSerie("Serie1", "Su papa", "asdfasdf", new ArrayList<Season>(), "Jum");
        ac.addSeason(459, "Season1", null);
        ac.addSeason(459, "Season2", null);
        ac.addChapter("Chapter1", "Holquehace", 23, 459, 1);
        ac.addChapter("Chapter2", "Holquehace", 23, 459, 1);

     //   ac.addSerie("Serie2", "sumama", "asdfasdf", new ArrayList<Season>(), "Jum");
        ac.addSeason(460, "Season1", null);
        ac.addSeason(460, "Season2", null);
        ac.addChapter("Chapter1", "Holquehace", 23, 460, 1);
        ac.addChapter("Chapter2", "Holquehace", 23, 460, 1);

     //   ac.addSerie("Serie3", "Sutio", "asdfasdf", new ArrayList<Season>(), "Jum");
        ac.addSeason(461, "Season1", null);
        ac.addSeason(461, "Season2", null);
        ac.addChapter("Chapter1", "Holquehace", 23, 461, 1);
        ac.addChapter("Chapter1", "Holquehace", 23, 461, 2);
    }

    @Test
    public void addMultimediaValidation() {
        AdminController ac = new AdminController();

        // ac.addMovie("Name1", "Author1", null, 0, null);
        // ac.addMovie("Name2", "Author1", null, 0, null);
        // ac.addMovie("Name1", "Author2", null, 0, null);
        // ac.addMovie("Name1", "Author1", null, 0, null);

    //    ac.addSerie("Name1", "Author1", null, new ArrayList<Season>(), null);
    //    ac.addSerie("Name2", "Author1", null, new ArrayList<Season>(), null);
    //    ac.addSerie("Name1", "Author2", null, new ArrayList<Season>(), null);
//ac.addSerie("Name1", "Author1", null, new ArrayList<Season>(), null);
    }

    @Test
    public void UsersPersistence() {
        UserRegisteredController uc = new UserRegisteredController();

        uc.addUser("yovani ", "basquez", 1234, "jusad");
        uc.getListUsers();
    }

    @Test
    public void MultimediaListInUsersLogin() {
        UserRegisteredController uc = new UserRegisteredController();
        AdminController ac = new AdminController();
        if (uc.couldLogIn("yovani1234@uptc.edu.co", "jusad")) {

        }

    } 
    
    @Test
    public void pdf(){
        FileManagement fm = new FileManagement();
        UserRegistered u = new UserRegistered();
        u.setPayment(new Payment("juan casas", 123213, "seug", 2020, "fe"));
        u.setUser("casas@gmail.com");
        u.setId(12312);
        fm.generatePaymentPdf(u);
    
    }


}
