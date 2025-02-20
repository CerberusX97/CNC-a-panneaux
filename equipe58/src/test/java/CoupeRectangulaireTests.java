import Utils.Point3D;
import ca.ulaval.equipe58.domaine.*;
import org.junit.jupiter.api.Test;
import ca.ulaval.equipe58.domaine.Outil;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CoupeRectangulaireTests {

    /*@Test
    public void testCalculateDepartArrive() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(100, 100, 0), null);
        Outil outil = new Outil("TestOutil", 10);

        CoupeRectangulaire coupe = new CoupeRectangulaire(
            UUID.randomUUID(),
            null,
            null,
            ReferenceType.EDGE,
            ReferenceType.EDGE,
            10,
            10,
            40,
            60,
            outil,
            panneau,
            EdgeType.BOTTOM
        );

        coupe.calculateDepartArrive();

        assertNotNull(coupe.getDepart(), "Le point de départ doit être calculé.");
        assertNotNull(coupe.getArrive(), "Le point d'arrivée doit être calculé.");
    }*/

    /*@Test
    public void testInvalidDValues() {
        CoupeRectangulaire coupe = new CoupeRectangulaire(
            UUID.randomUUID(),
            null,
            null,
            null,
            null,
            0,
            0,
            -10,
            -20,
            null,
            null,
            EdgeType.BOTTOM
        );
        assertTrue(coupe.getD3() > 0 && coupe.getD4() > 0, 
            "D3 et D4 doivent rester positifs.");
    }*/
    
}
