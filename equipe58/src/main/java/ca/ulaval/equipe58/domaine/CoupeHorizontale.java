/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.util.UUID;

/**
 *
 * @author mamad
 */
public class CoupeHorizontale extends Coupe {
    private EdgeType edgeReference; 

    public EdgeType getEdgeReference() {
        return edgeReference;
    }
@Override
public CoupeHorizontale clone() {
    CoupeHorizontale cloned = (CoupeHorizontale) super.clone();
    cloned.edgeReference = this.edgeReference; // Copier l'attribut spécifique
    return cloned;
}
    public void setEdgeReference(EdgeType edgeReference) {
        this.edgeReference = edgeReference;
    }
    
    public CoupeHorizontale(UUID id, Object bordure, ReferenceType typeDeReference, double distanceFromReference, Outil outil, Panneau panneau, EdgeType edgeReference) {
        super(id, TypeDeCoupe.HORIZONTALE, bordure, typeDeReference, distanceFromReference, outil, panneau);
        this.edgeReference = edgeReference;  
        System.out.println("CoupeHorizontale- "+ edgeReference);

        calculateDepartArrive();
    }

    @Override
    public ReferenceType getTypeDeReference() {
        return super.getTypeDeReference();
    }

    @Override
    public double getDistanceFromReference() {
        return super.getDistanceFromReference(); 
    }
    
    @Override
    public void calculateDepartArrive() {
        double referenceY = 0;
        ReferenceType typeDeReference = this.getTypeDeReference();
        Panneau panneau = this.getPanneau();
        
        if (typeDeReference == ReferenceType.EDGE) {
            // Utilisation de edgeReference pour déterminer la position Y
            switch (this.edgeReference) {
                case TOP -> referenceY = panneau.getDepartPanneau().getY();
                case BOTTOM -> referenceY = panneau.getArrivePanneau().getY();
                case LEFT -> referenceY = panneau.getDepartPanneau().getY();
                case RIGHT -> referenceY = panneau.getArrivePanneau().getY();
            }
        } else if (typeDeReference == ReferenceType.COUPE) {
            Coupe coupeReference = (Coupe) this.getBordure();
            referenceY = coupeReference.getDepart().getY();
        }
        
        double distance = this.getDistanceFromReference();
        
        double y;
        if (distance < 0) {
            y = referenceY + this.getDistanceFromReference() - (int) this.getOutil().getLargeur()/2;
        } else {
            y = referenceY + this.getDistanceFromReference() + (int) this.getOutil().getLargeur()/2;
        }
        
        Point3D depart = new Point3D(panneau.getDepartPanneau().getX(), y);
        Point3D arrive = new Point3D(panneau.getArrivePanneau().getX(), y);
        this.setDepart(depart);
        this.setArrive(arrive);
    }
}

