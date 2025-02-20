/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.UI.Drawer;
import Utils.ConvertisseurPxMm;
import ca.ulaval.equipe58.domaine.Controller;
import ca.ulaval.equipe58.domaine.Coupe;
import ca.ulaval.equipe58.domaine.Outil;
import ca.ulaval.equipe58.domaine.PanneauDTO;
import ca.ulaval.equipe58.domaine.TypeDeCoupe;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import Utils.Point3D;
import ca.ulaval.equipe58.domaine.CoupeEnL;
import ca.ulaval.equipe58.domaine.CoupeRectangulaire;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicol
 */
public class Drawer {
    private Dimension dimensionFenetre;
    private Controller controller;
    private boolean afficherGrille = false;
    private double largeurCase = 50; // Largeur par défaut de la case
    private double hauteurCase = 50; 

    
    public Drawer(Controller controller, Dimension dimensionFenetre)
    {
        this.dimensionFenetre = dimensionFenetre;
        this.controller = controller;  
    }
    


    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        dessinerPanneau(g);
        dessinerCoupes(g);
        if (afficherGrille){
            dessinerGrille(g2d);
} 
    }
    
public void dessinerPanneau(Graphics g) {
    PanneauDTO panneauDTO = controller.getPanneauDTO();
    ConvertisseurPxMm convertisseur = new ConvertisseurPxMm();

    if (panneauDTO != null) {
        double largeurPx = convertisseur.convertMmToPx(panneauDTO.getArrive().getX() - panneauDTO.getDepart().getX());
        double longueurPx = convertisseur.convertMmToPx(panneauDTO.getArrive().getY() - panneauDTO.getDepart().getY());
        
        double departX = convertisseur.convertMmToPx(panneauDTO.getDepart().getX());
        double departY = convertisseur.convertMmToPx(panneauDTO.getDepart().getY());

        g.setColor(Color.red);
        g.fillRect((int) departX,(int) departY,(int) largeurPx,(int) longueurPx);
    }
    }


public void setAfficherGrille(boolean afficherGrille,double largeur,double hauteur) {
    this.afficherGrille = afficherGrille;
    this.largeurCase = largeur;
    this.hauteurCase = hauteur;
}

// probleme lorsque pas dans le mode selecte voir pourquoi
   public void dessinerCoupes(Graphics g) {
    List<Coupe> coupes = controller.getCoupes();
    ConvertisseurPxMm convertisseur = new ConvertisseurPxMm();
    Coupe coupeSelectionnee = controller.getSelectedCoupe(); 

    for (Coupe coupe : coupes) {
        Outil outil = coupe.getOutil();
        double largeurMm = outil.getLargeur();
        Point3D departMm = coupe.getDepart();
        Point3D arriveMm = coupe.getArrive();

        // Conversion de mm à px
        double largeurPx = convertisseur.convertMmToPx((int) largeurMm);
        double departXpx = convertisseur.convertMmToPx(departMm.getX());
        double departYpx = convertisseur.convertMmToPx(departMm.getY());
        double arriveXpx = convertisseur.convertMmToPx(arriveMm.getX());
        double arriveYpx = convertisseur.convertMmToPx(arriveMm.getY());
        
        if (coupe.getType() == TypeDeCoupe.RECTANGULAIRE) {
            dessinerCoupesInternes(g, (CoupeRectangulaire) coupe);
        } else  {
            if (coupe.equals(coupeSelectionnee)) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.BLUE);
            }
            dessinerCoupe(
                g,
                coupe.getType(),
                coupe.getDistanceFromReference(), // Pas utilisé pour le dessin direct ici, mais utile ailleurs
                new Point3D(departXpx, departYpx),
                new Point3D(arriveXpx, arriveYpx),
                largeurPx
            );
        }
    }
}
   
   private void dessinerGrille(Graphics2D g2d) {
    PanneauDTO panneauDTO = controller.getPanneauDTO();
    ConvertisseurPxMm convertisseur = new ConvertisseurPxMm();

    g2d.setColor(Color.LIGHT_GRAY);
    double largeurPx = convertisseur.convertMmToPx(panneauDTO.getArrive().getX() - panneauDTO.getDepart().getX());
    double longueurPx = convertisseur.convertMmToPx(panneauDTO.getArrive().getY() - panneauDTO.getDepart().getY());
    largeurCase = convertisseur.convertMmToPx(largeurCase);
    hauteurCase = convertisseur.convertMmToPx(hauteurCase);

    for (int x = 0; x < largeurPx; x += largeurCase) {
        g2d.drawLine(x, 0, x, (int) longueurPx);
    }

    for (int y = 0; y < longueurPx; y += hauteurCase) {
        g2d.drawLine(0, y, (int) largeurPx, y);
    }
}

public void dessinerCoupe(Graphics g, TypeDeCoupe typeDeCoupe, double distanceAvecReference, Point3D depart, Point3D arrive, double largeur) {        
    Graphics2D g2d = (Graphics2D) g;
    double offset = largeur / 2; 
    
    if (typeDeCoupe == TypeDeCoupe.VERTICALE) {
        //System.out.println(distanceAvecReference);
        //System.out.println(offset);
        //System.out.println(largeur);
        
        
        g2d.fillRect((int) depart.getX() - (int)offset,(int) depart.getY(),(int) largeur,(int) arrive.getY() - (int)depart.getY());

    } else if (typeDeCoupe == TypeDeCoupe.HORIZONTALE) {

            g2d.fillRect((int) depart.getX(),(int) depart.getY() -(int) offset,(int) arrive.getX() - (int) depart.getX(),(int) largeur);
            
    }else if (typeDeCoupe == TypeDeCoupe.EN_L) {        
        int xDepart = (int) depart.getX();
        int yDepart = (int) depart.getY();
        int xArrive = (int) arrive.getX();
        int yArrive = (int) arrive.getY();

        g2d.setStroke(new BasicStroke((int) largeur));
        g2d.drawLine(xDepart, yArrive, xArrive, yArrive);
        g2d.drawLine(xArrive, yArrive, xArrive, yDepart);
        g2d.setStroke(new BasicStroke(1)); 
    }else if (typeDeCoupe == TypeDeCoupe.BORDURE) {
        double x = Math.min(depart.getX(), arrive.getX());
        double y = Math.min(depart.getY(), arrive.getY());
        double width = Math.abs(arrive.getX() - depart.getX());
        double height = Math.abs(arrive.getY() - depart.getY());
        
        double adjustX = x - offset;
        double adjustY = y - offset;
        
        double adjustWidth = width + largeur;
        double adjustHeight = height + largeur;

        g2d.setStroke(new BasicStroke((int) largeur));
        g2d.drawRect((int) adjustX,(int) adjustY,(int) adjustWidth,(int) adjustHeight);
        g2d.setStroke(new BasicStroke(1)); 
        
    } else {
        double x = Math.min(depart.getX(), arrive.getX());
        double y = Math.min(depart.getY(), arrive.getY());
        double width = Math.abs(arrive.getX() - depart.getX());
        double height = Math.abs(arrive.getY() - depart.getY());
        System.out.println("Probleme ");
        g.fillRect((int) x -(int) offset,(int) y -(int) offset,(int) width + 2 *200,(int) height + 2 *(int) offset);
    }
}

private void dessinerCoupesInternes(Graphics g, CoupeRectangulaire rectangle) {
    ConvertisseurPxMm convertisseur = new ConvertisseurPxMm();
    for (Coupe sousCoupe : rectangle.getCoupesInternes()) {
        Outil outil = sousCoupe.getOutil();
        double largeurPx = convertisseur.convertMmToPx(outil.getLargeur());
        Point3D departPx = new Point3D(
                convertisseur.convertMmToPx(sousCoupe.getDepart().getX()),
                convertisseur.convertMmToPx(sousCoupe.getDepart().getY()));
        Point3D arrivePx = new Point3D(
                convertisseur.convertMmToPx(sousCoupe.getArrive().getX()),
                convertisseur.convertMmToPx(sousCoupe.getArrive().getY()));

        if (sousCoupe.equals(controller.getSelectedCoupe())) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.BLUE);
            }
        dessinerCoupe(g, sousCoupe.getType(), sousCoupe.getDistanceFromReference(), departPx, arrivePx, largeurPx);
    }
}


}
