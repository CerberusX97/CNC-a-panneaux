/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

import java.util.UUID;
import Utils.Point3D;

/**
 *
 * @author mamad
 */
public class CoupeBordure extends Coupe {
    private double hauteur;
    private double largeur;
    private Outil outil;

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public Outil getOutil() {
        return outil;
    }

    public void setOutil(Outil outil) {
        this.outil = outil;
    }
        @Override
public CoupeBordure clone() {
    CoupeBordure cloned = (CoupeBordure) super.clone();
    cloned.hauteur = this.hauteur;
    cloned.largeur = this.largeur;
    return cloned;
}
    public CoupeBordure(UUID id, Outil outil, Panneau panneau, double Hauteur, double Largeur){
        super(id, TypeDeCoupe.BORDURE, null, null, 0, outil, panneau);
        this.hauteur = Hauteur;
        this.largeur = Largeur;
        this.outil = outil;
        calculateDepartArrive();
    }
    
    @Override
    public void calculateDepartArrive(){
        Panneau panneau = this.getPanneau();
        double centreX = (panneau.getArrivePanneau().getX() - panneau.getDepartPanneau().getX())/2;
        double centreY = (panneau.getArrivePanneau().getY() - panneau.getDepartPanneau().getY())/2;
                
        Point3D depart = new Point3D(centreX - largeur/2, centreY - hauteur/2);
        Point3D arrive = new Point3D(centreX + largeur/2, centreY + hauteur/2);
        this.setDepart(depart);
        this.setArrive(arrive);
    }
    
    
}