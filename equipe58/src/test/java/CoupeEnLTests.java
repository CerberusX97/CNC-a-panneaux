import Utils.Point3D;
import ca.ulaval.equipe58.domaine.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

public class CoupeEnLTests {

    /*@Test
    public void testSetEtGetD3() {
        CoupeEnL coupe = new CoupeEnL(UUID.randomUUID(), null, null, null, null, 0, 0, 50, 100, null, null);
        coupe.setD3(75);
        assertEquals(75, coupe.getD3());
    }*/

   /* @Test
    public void testDepartArriveAvecD3() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1000, 1000, 0), new Point3D(0, 0, 50));
        CoupeEnL coupe = new CoupeEnL(UUID.randomUUID(), EdgeType.BOTTOM, EdgeType.RIGHT, null, null, 100, 150, 300, 400, null, panneau);

        coupe.calculateDepartArrive();

        assertNotNull(coupe.getDepart());
        assertNotNull(coupe.getArrive());

        assertEquals(100, coupe.getDepart().getX());
        assertEquals(150, coupe.getDepart().getY());
    }*/

    /*@Test
    public void testInvalidD3Value() {
        CoupeEnL coupe = new CoupeEnL(UUID.randomUUID(), null, null, null, null, 0, 0, 50, 100, null, null);
        coupe.setD3(-10); // Une valeur négative invalide
        assertTrue(coupe.getD3() >= 0, "D3 devrait être une valeur positive.");
    }*/
    
}

