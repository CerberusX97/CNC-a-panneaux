import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ca.ulaval.equipe58.UI.Afficheur;

public class AfficheurTests {

    //@Test
    //public void testModeInitialParDefaut() {
        //Afficheur afficheur = new Afficheur();
        //assertEquals(Afficheur.ApplicationMode.SELECT, afficheur.actualMode,
            //"Le mode initial doit être SELECT.");
    //}

    @Test
    public void testChangeApplicationMode() {
        Afficheur afficheur = new Afficheur();
        afficheur.actualMode = Afficheur.ApplicationMode.COUPER;
        assertEquals(Afficheur.ApplicationMode.COUPER, afficheur.actualMode, 
            "Le mode doit changer correctement à COUPER.");
    }}

    //@Test
    //public void testUpdateMouseCoordinates() {
        //Afficheur afficheur = new Afficheur();
        ////
        //assertEquals(100.5, afficheur.actualMousePoint.getX(),
          //  "La coordonnée X de la souris doit être mise à jour correctement.");
        //assertEquals(200.25, afficheur.actualMousePoint.getY(),
          //  "La coordonnée Y de la souris doit être mise à jour correctement.");
    //}

