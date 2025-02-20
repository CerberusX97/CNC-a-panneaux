/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.util.UUID;

/**
 *
 * @author mamad
 */
public class CoupeFactory {
    public Coupe creerCoupe(TypeDeCoupe typeDeCoupe, Object bordure, ReferenceType typeDeReference,
                            double distanceFromReference, Outil outil, Panneau panneau,
                            Point3D referencePoint3D, Point3D cutPoint3D, EdgeType edgeReference) {
        UUID id = UUID.randomUUID();
        
        return switch (typeDeCoupe) {
            case VERTICALE -> new CoupeVerticale(id, bordure, typeDeReference,
                                                 distanceFromReference, outil, panneau, edgeReference);
            case HORIZONTALE -> new CoupeHorizontale(id, bordure,
                                                     typeDeReference,
                                                     distanceFromReference,
                                                     outil,
                                                     panneau, edgeReference);
            /*case EN_L -> new CoupeEnL(id,
                                      bordure,
                                      typeDeReference,
                                      distanceFromReference,
                                      outil,
                                      panneau); // Ensure the constructor is complete
            /*case RECTANGULAIRE -> new CoupeRectangulaire(id,
                                                         bordure,
                                                         typeDeReference,
                                                         distanceFromReference,
                                                         outil,
                                                         panneau,
                                                         referencePoint3D,
                                                         cutPoint3D);*/
            /*case BORDURE -> new CoupeBordure(id,
                                             bordure,
                                             typeDeReference,
                                             distanceFromReference,
                                             outil,
                                             panneau);*/
            default -> null;
        };
    }
}