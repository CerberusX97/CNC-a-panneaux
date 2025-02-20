import Utils.Point3D;
import ca.ulaval.equipe58.domaine.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import ca.ulaval.equipe58.domaine.Outil;

public class CoupeBordureTests {

    @Test
    public void testCalculateDepartArrive() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(100, 100, 0), new Point3D(0, 0, 50));
        Outil outil = new Outil("TestOutil", 10);
        CoupeBordure coupe = new CoupeBordure(UUID.randomUUID(), outil, panneau, 50, 20);

        coupe.calculateDepartArrive();

        assertNotNull(coupe.getDepart(), "Le départ ne doit pas être null.");
        assertNotNull(coupe.getArrive(), "L'arrivée ne doit pas être null.");
    }
}
//

