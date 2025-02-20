import Utils.Point3D;
import ca.ulaval.equipe58.domaine.Panneau;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PanneauTests {

    @Test
    public void testDimensionsValides() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1524, 3048, 0), new Point3D(0, 0, 50));
        assertEquals(1524, panneau.getArrivePanneau().getX());
        assertEquals(3048, panneau.getArrivePanneau().getY());
        assertEquals(50, panneau.getProfondeurPanneau().getZ());
    }

    @Test
    public void testLargeurEgaleLimite() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1524, 1000, 0), new Point3D(0, 0, 50));
        assertEquals(1524, panneau.getArrivePanneau().getX());
    }

    @Test
    public void testLongueurEgaleLimite() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1000, 3048, 0), new Point3D(0, 0, 50));
        assertEquals(3048, panneau.getArrivePanneau().getY());
    }

    @Test
    public void testProfondeurEgaleLimite() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1000, 2000, 0), new Point3D(0, 0, 50));
        assertEquals(50, panneau.getProfondeurPanneau().getZ());
    }

//    @Test
    public void testProfondeurZero() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Panneau(new Point3D(0, 0, 0), new Point3D(1000, 2000, 0), new Point3D(0, 0, 0));
        });
        assertEquals("La profondeur doit être > 0.", thrown.getMessage());
    }

  //  @Test
    public void testDimensionsNegatives() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Panneau(new Point3D(0, 0, 0), new Point3D(-1524, -3048, 0), new Point3D(0, 0, -50));
        });
        assertEquals("Les dimensions doivent être positives.", thrown.getMessage());
    }

    //@Test
    public void testRedimensionnementValide() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1000, 2000, 0), new Point3D(0, 0, 10));
        panneau.redimensionner(1524, 3048, 50);
        assertEquals(1524, panneau.getArrivePanneau().getX());
        assertEquals(3048, panneau.getArrivePanneau().getY());
        assertEquals(50, panneau.getArrivePanneau().getZ());
    }

 //   @Test
    public void testRedimensionnementInvalideLargeurZero() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1000, 2000, 0), new Point3D(0, 0, 10));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            panneau.redimensionner(0, 2000, 50);
        });
        assertEquals("Les dimensions doivent être > 0.", thrown.getMessage());
    }

//    @Test
    public void testRedimensionnementInvalideProfondeurZero() {
        Panneau panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1000, 2000, 0), new Point3D(0, 0, 10));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            panneau.redimensionner(1000, 2000, 0);
        });
        assertEquals("Les dimensions doivent être > 0.", thrown.getMessage());
    }

    @Test
    public void testValeursLettre() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Integer.parseInt("lettre"); 
        });
        assertEquals("For input string: \"lettre\"", thrown.getMessage());
    }

    @Test
    public void testValeursVides() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Integer.parseInt(""); 
        });
        assertEquals("For input string: \"\"", thrown.getMessage());
    }
}
