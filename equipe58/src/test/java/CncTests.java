/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author chahid
 */
import Utils.Point3D;
import ca.ulaval.equipe58.domaine.Cnc;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class CncTests {

    @Test
    public void testInitialOutilsListVide() {
        Cnc cnc = new Cnc();
        assertTrue(cnc.getOutils().isEmpty());
    }

    @Test
    public void testAjoutOutil() {
        Cnc cnc = new Cnc();
        cnc.ajouterOutil("outil1", 5.0);
        assertEquals(1, cnc.getOutils().size());
        assertEquals("outil1", cnc.getOutils().get(0).getNom());
    }

    @Test
    public void testRedimensionnerPanneau() {
        Cnc cnc = new Cnc();
        cnc.redimensionnerPanneau(2000, 3000, 50);
        assertEquals(2000, cnc.getPanneau().getArrivePanneau().getX());
        assertEquals(3000, cnc.getPanneau().getArrivePanneau().getY());
    }}

    //@Test
    //public void testCreerCoupeRectangulaire() {
      //  Cnc cnc = new Cnc();
       // Point3D ref = new Point3D(0, 0, 0);
        //Point3D firstPoint = new Point3D(1000, 0, 0);
        //Point3D secondPoint = new Point3D(1000, 500, 0);
        //cnc.creerCoupeRectangulaire(ref, firstPoint, secondPoint, null);  // null outil juste pour simplifier
        //assertEquals(1, cnc.getCoupes().size());
    //}