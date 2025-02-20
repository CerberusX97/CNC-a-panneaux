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
public class CoupeVerticale extends Coupe {
    private EdgeType edgeReference; 

    public EdgeType getEdgeReference() {
        return edgeReference;
    }
    @Override
    public CoupeVerticale clone() {
        CoupeVerticale cloned = (CoupeVerticale) super.clone();
        cloned.edgeReference = this.edgeReference; // Copier les attributs spécifiques
        return cloned;
    }

    public void setEdgeReference(EdgeType edgeReference) {
        this.edgeReference = edgeReference;
    }
    
    public CoupeVerticale(UUID id, Object bordure, ReferenceType typeDeReference, double distanceFromReference, Outil outil, Panneau panneau, EdgeType edgeReference) {
        super(id, TypeDeCoupe.VERTICALE, bordure, typeDeReference, distanceFromReference, outil, panneau);
        this.edgeReference = edgeReference; 

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
    protected void calculateDepartArrive() {
        double referenceX = 0;
        ReferenceType typeDeReference = this.getTypeDeReference();
        Panneau panneau = this.getPanneau();

        if (typeDeReference == ReferenceType.EDGE) {
            // Utilisation de edgeReference pour déterminer la position X
            switch (this.edgeReference) {
                case LEFT -> referenceX = panneau.getDepartPanneau().getX();
                case RIGHT -> referenceX = panneau.getArrivePanneau().getX();
            }
        } else if (typeDeReference == ReferenceType.COUPE) {
            Coupe coupeReference = (Coupe) this.getBordure();
            referenceX = coupeReference.getDepart().getX();
        }

        double distance = this.getDistanceFromReference();
        double x;
        if (distance < 0) {
            x = referenceX + this.getDistanceFromReference() - (int) this.getOutil().getLargeur()/2;
        } else {
            x = referenceX + this.getDistanceFromReference() + (int) this.getOutil().getLargeur()/2;
        }

        Point3D depart = new Point3D(x, panneau.getDepartPanneau().getY());
        Point3D arrive = new Point3D(x, panneau.getArrivePanneau().getY());
        this.setDepart(depart);
        this.setArrive(arrive);
    }
}


