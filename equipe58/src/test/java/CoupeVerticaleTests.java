import Utils.Point3D;
import ca.ulaval.equipe58.domaine.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import ca.ulaval.equipe58.domaine.Outil;

public class CoupeVerticaleTests {

    /*@Test
    public void testPointsDepartEtArriveVerticaux() {
        Panneau panneau = new Panneau(
            new Point3D(0, 0, 0), 
            new Point3D(2000, 1500, 0), 
            new Point3D(0, 0, 50) // Troisième paramètre ajouté
        );
        Outil outil = new Outil("outil-test", 10);
        CoupeVerticale coupe = new CoupeVerticale(
            UUID.randomUUID(),
            null, // bordure est null
            ReferenceType.EDGE,
            200.0,
            outil,
            panneau,
            EdgeType.LEFT
        );

        Point3D depart = coupe.getDepart();
        Point3D arrive = coupe.getArrive();

        assertEquals(200, depart.getX(), "La coordonnée X du point de départ doit être correcte.");
        assertEquals(0, depart.getY(), "La coordonnée Y doit être le bord supérieur du panneau.");
        assertEquals(200, arrive.getX(), "La coordonnée X doit correspondre à la même verticale.");
        assertEquals(1500, arrive.getY(), "La coordonnée Y doit être le bord inférieur du panneau.");
    }*/

    @Test
    public void testChangerDistanceReference() {
        Panneau panneau = new Panneau(
            new Point3D(0, 0, 0), 
            new Point3D(2000, 1500, 0), 
            new Point3D(0, 0, 50) // Troisième paramètre ajouté
        );
        CoupeVerticale coupe = new CoupeVerticale(
            UUID.randomUUID(),
            null, // bordure est null
            ReferenceType.EDGE,
            0,
            new Outil("outil", 15),
            panneau,
            EdgeType.LEFT
        );
        coupe.setDistanceFromReference(300);

        assertEquals(300, coupe.getDistanceFromReference(), 
            "La distance par rapport à la référence doit être mise à jour.");
    }
}
