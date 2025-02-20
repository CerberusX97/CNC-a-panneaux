package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cnc {
public static final double LONGUEUR_EN_PIEDS = 10;
    public static final double LARGEUR_EN_PIEDS = 5;

    private Panneau panneau;
    private List<Outil> outils;
    private Outil outilActuel;

    public void setPanneau(Panneau panneau) {
        this.panneau = panneau;
    }

    public void setOutilActuel(Outil outilActuel) {
        this.outilActuel = outilActuel;
    }

    public Cnc(Panneau panneau, List<Outil> outils) {
        this.panneau = panneau;
        this.outils = outils ;
        this.outilActuel = null;
    }
    public Coupe switchSelectionStatus2(double x, double y) {
        return panneau.switchSelectionStatus2(x, y);
    }
    public Cnc() {
        Point3D departPanneau = new Point3D(0, 0, 0);
        Point3D arrivePanneau = new Point3D(1219, 914, 10);
        Point3D profondeurPanneau = new Point3D(0,0,10);
        this.panneau = new Panneau(departPanneau, arrivePanneau, profondeurPanneau);
        this.outils = new ArrayList<>();
    }
    public void setOutils(List<Outil> outils) {
        this.outils = outils;
    }
    public void redimensionnerPanneau(double x, double y, double z) {
        this.panneau.redimensionner(x, y, z);
    }
    
    public void resetPanneau() {
        this.panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(1219, 914, 0), new Point3D(0,0, 1));
    }

    public boolean isReferencePointValid(Point3D Point3D, String typeDeCoupe) {
        return panneau.isIntersectionPoint(Point3D) || panneau.isReferencePoint3DValid(Point3D, typeDeCoupe);
    }
    public Coupe getSelectedCoupe2(Point3D Point3D, String typeDeCoupe) {
        return panneau.getCoupeAtPoint(Point3D, TypeDeCoupe.valueOf(typeDeCoupe));
    }
    public void creerCoupe(String typeDeCoupeActuel, Point3D Point3DReference, Point3D Point3DDeCoupe, Outil outil) {
        TypeDeCoupe typeDeCoupe = TypeDeCoupe.valueOf(typeDeCoupeActuel);
        Object bordure = panneau.getEdgeAtPoint(Point3DReference, typeDeCoupe);
        ReferenceType typeDeReference = ReferenceType.EDGE;
        
        if(bordure == null){
            bordure = panneau.getCoupeAtPoint(Point3DReference, typeDeCoupe);
            typeDeReference = ReferenceType.COUPE;
        }
        

        EdgeType edgeType = EdgeType.LEFT;
        double distanceFromReference = panneau.calculateDistanceFromReference(Point3DReference, Point3DDeCoupe, typeDeCoupe);
        System.out.println("Point=" + distanceFromReference);
        if (distanceFromReference < 0){
            edgeType = EdgeType.RIGHT;
        }
        else if  (distanceFromReference >= 0){
            edgeType = EdgeType.LEFT;
        }
        panneau.creerCoupe(typeDeCoupe, bordure, typeDeReference, distanceFromReference, outil, Point3DReference, Point3DDeCoupe, edgeType);
    }
    
    public void setOutilActuel(String nomOutil) {
        this.outilActuel = outils.stream()
            .filter(outil -> outil.getNom().equals(nomOutil))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Outil introuvable : " + nomOutil));
            }


    public void supprimerCoupe() {
        panneau.supprimerCoupeSelectionnee();
    }
    public void supprimerCoupeBordure() {
        panneau.suppCoupeBordure();
    }

    public void ajouterOutil(String nom, double largeur) {
        if (outils.size() >= 12) {
            throw new IllegalStateException("Impossible d'ajouter plus de 12 outils.");
        }
        Outil outil = new Outil(nom, largeur);
        this.outils.add(outil);
    }

public void supprimerOutil(String nom) {
    System.out.println("Avant suppression : " + outils);
    Outil outil = trouverOutil(nom);
    if (outil != null) {
        outils.remove(outil);
        System.out.println("Outil supprimé : " + outil.getNom());
    } else {
        System.out.println("Outil à supprimer non trouvé : " + nom);
    }
    System.out.println("Après suppression : " + outils);
}

    public void choisirOutil(String nom) {
        this.outilActuel = trouverOutil(nom);
        
    }

private Outil trouverOutil(String nom) {
    for (Outil outil : outils) {
        System.out.println(outil.getNom());
        if (outil.getNom().equals(nom)) {
            System.out.println("Outil trouvé : " + outil.getNom());
            return outil;
        }
    }
    System.out.println("Outil non trouvé pour : " + nom);
    return null;
}

    public void ajouterZoneInterdite(Point3D departZI, Point3D arriveZI) {
        this.panneau.ajouterZoneInterdite(departZI, arriveZI);
    }

    public void supprimerZoneInterdite(UUID id) {
        this.panneau.supprimerZoneInterdite(id);
    }
    
    public void modifierZoneInterdite(UUID zoneId, ZoneInterdite nouvelleZone) {
        this.panneau.modifierZoneInterdite(zoneId, nouvelleZone);
    }    

    public boolean verifierPoint(Point3D Point3D) {
    return false;
    }

    public void sauvegarderPan(String path) {
    }
    public void sauvegarderCnc(String path) {
    }
    
    public void ExportGcode(double vitesse, double spin){
        panneau.ExportGcode(vitesse, spin);
    }

    public void importerPan(String path) {
    }
    public void importerCnc(String path) {
    }

    public Panneau getPanneau() {
        return panneau;
    }

    public List<Outil> getOutils() {
        return outils;
    }

    public Outil getOutilActuel() {
        return outilActuel;
    }

    public Point3D getDepartPanneau(){
        return this.panneau.getDepartPanneau();
    }
    
    public Point3D getArrivePanneau(){
        return this.panneau.getArrivePanneau();
    }
    
    public List<Coupe> getCoupes(){
        return this.panneau.getCoupes();
    }
    
    public void switchSelectionStatus(double x, double y) {
        panneau.switchSelectionStatus(x, y);
    }
    
    public void updateSelectedItemsPositions(double delta) {
        panneau.updateSelectedItemsPosition(delta);
    }
    
    public boolean isReferencePointValidForIrregularCut(Point3D Point3D) {
        return panneau.isReferencePointValidForIrregularCut(Point3D);
    }

    public void creerCoupeRectangulaire(Point3D Point3DReference, Point3D firstPoint3DDeCoupe, Point3D secondPoint3DDeCoupe, Outil outil) {
        panneau.creerCoupeRectangulaire(Point3DReference, firstPoint3DDeCoupe, secondPoint3DDeCoupe, outil);
    }

    public void creerCoupeEnL(Point3D Point3DReference, Point3D Point3DDeCoupe, Outil outil) {
        panneau.creerCoupeEnL(Point3DReference, Point3DDeCoupe, outil);
    }
    
    public void creerCoupeBordure(double Hauteur, double Largeur, Outil outil) {
        panneau.creerCoupeBordure(Hauteur, Largeur, outil);
    }
    public void modifierCoupeBordure(double Hauteur, double Largeur, Outil outil) {
        panneau.modifierCoupeBordure(Hauteur, Largeur, outil);
    }
    
public void modifierOutil(String nomOutil, double nouveauDiametre) {
        Outil outilAModifier = getOutils().stream()
            .filter(outil -> outil.getNom().equals(nomOutil))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Outil introuvable : " + nomOutil));
    panneau.modifierOutil(nomOutil, nouveauDiametre,  outilAModifier);
}

public void creerNouveauPanneau(double width, double height, double depth) {
    if (panneau == null) {
        panneau = new Panneau(new Point3D(0, 0, 0), new Point3D(width, height, 0), new Point3D(0, 0, depth));
    } else {
        panneau.creerNouveauPanneau(width, height, depth);
    }
}

}