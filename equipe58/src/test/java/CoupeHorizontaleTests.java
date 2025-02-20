import Utils.Point3D;
import ca.ulaval.equipe58.domaine.*;
import org.junit.jupiter.api.Test;
import ca.ulaval.equipe58.domaine.Outil;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

public class CoupeHorizontaleTests {

    @Test
    public void testCalculateDepartArriveAvecEdgeReference() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(500, 500, 0), null);
        Outil outil = new Outil("TestOutil", 5);
        CoupeHorizontale coupe = new CoupeHorizontale(UUID.randomUUID(), null, ReferenceType.EDGE, 20, outil, panneau, EdgeType.BOTTOM);

        coupe.calculateDepartArrive();

        assertNotNull(coupe.getDepart());
        assertNotNull(coupe.getArrive());
    }

   /* @Test
    public void testEdgeReferenceFail() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(500, 500, 0), null);
        Outil outil = new Outil("TestOutil", 5);

        CoupeHorizontale coupe = new CoupeHorizontale(UUID.randomUUID(), null, null, -100, outil, panneau, EdgeType.TOP);
        coupe.calculateDepartArrive();

        assertEquals(0, coupe.getDepart().getY());
    }*/
    
}

