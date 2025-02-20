/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

/**
 *
 * @author ADMO-PC
 */
import Utils.Point3D;
import static ca.ulaval.equipe58.domaine.EdgeType.BOTTOM;
import static ca.ulaval.equipe58.domaine.EdgeType.LEFT;
import static ca.ulaval.equipe58.domaine.EdgeType.RIGHT;
import static ca.ulaval.equipe58.domaine.EdgeType.TOP;
import static ca.ulaval.equipe58.domaine.TypeDeCoupe.HORIZONTALE;
import static ca.ulaval.equipe58.domaine.TypeDeCoupe.VERTICALE;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Panneau implements Subject {
    public static final double threshold = 10;
    private Point3D depart;
    private Point3D arrive;
    private Point3D profondeur;
    private List<ZoneInterdite> zoneInterdites;
    private List<Coupe> listeCoupe;
    private CoupeFactory coupeFactory;
    private List<Observer> observers = new ArrayList<>();
    
    public Panneau(){
    }
    public Panneau(Point3D departPanneau, Point3D arrivePanneau, Point3D profondeur) {
        this.depart = departPanneau;
        this.profondeur = profondeur;
        this.arrive = arrivePanneau;
        this.zoneInterdites = new ArrayList<>();
        this.listeCoupe = new ArrayList<>();
        coupeFactory = new CoupeFactory();
    }
    public void setDepart(Point3D depart) {
            this.depart = depart;
    }
    public void setArrive(Point3D arrive) {
        this.arrive = arrive;
    }
    public void setProfondeur(Point3D profondeur) {
        this.profondeur = profondeur;
    }
    public Point3D getProfondeurPanneau(){
        return this.profondeur;
    }
    public Point3D getDepartPanneau(){
        return this.depart;
    }
    
    public Point3D getArrivePanneau(){
        return this.arrive;
    }

    public List<ZoneInterdite> getZoneInterdites() {
        return zoneInterdites;
    }

    public List<Coupe> getCoupes() {
        return listeCoupe;
    }
    public Coupe getCoupe(String id) {
        for (Coupe coupe : listeCoupe) {
            if (coupe.getId().toString().equals(id)) { 
                return coupe;
            }
        }
        return null;  
    }
    
    public void redimensionner(double nouvelleLargeur, double nouvelleHauteur, double nouvelleProfondeur) {
        this.arrive.setX(this.depart.getX() + nouvelleLargeur);
        this.arrive.setY(this.depart.getY() + nouvelleHauteur);
        this.arrive.setZ(this.depart.getZ() + nouvelleProfondeur);
        notifyObservers();
        }
    public void ajouterZoneInterdite(Point3D departZI, Point3D arriveZI) {
        UUID idZI = UUID.randomUUID();
        ZoneInterdite zoneInterdite = new ZoneInterdite(idZI, departZI, arriveZI);
        zoneInterdites.add(zoneInterdite);
    }

    public void supprimerZoneInterdite(UUID zoneId) {
        zoneInterdites.removeIf(ZoneInterdite->ZoneInterdite.getId().equals(zoneId));
    }

    public void modifierZoneInterdite(UUID zoneId, ZoneInterdite nouvelleZone) {
        supprimerZoneInterdite(zoneId);
        ajouterZoneInterdite(nouvelleZone.getDepartZI(), nouvelleZone.getArriveZI());
        
    }
            Coupe switchSelectionStatus2(double x, double y) {
        for (Coupe item : this.listeCoupe) {
            TypeDeCoupe typeDeCoupe = item.getType();
            if(typeDeCoupe == TypeDeCoupe.RECTANGULAIRE){
                CoupeRectangulaire cr = (CoupeRectangulaire) item;
                if(cr.getCoupeVerticaleDepart().containsLigne(x, y, TypeDeCoupe.VERTICALE)
                        || cr.getCoupeVerticaleArrive().containsLigne(x, y, TypeDeCoupe.VERTICALE)
                        || cr.getCoupeHorizontaleDepart().containsLigne(x, y, TypeDeCoupe.HORIZONTALE)
                        || cr.getCoupeHorizontaleArrive().containsLigne(x, y, TypeDeCoupe.HORIZONTALE)
                        ){
                    return item;
                }
            } else if(typeDeCoupe == TypeDeCoupe.EN_L){
                CoupeEnL cl=  (CoupeEnL) item;
                if(cl.getCoupeVerticaleArrive().containsLigne(x, y, TypeDeCoupe.VERTICALE)
                        || cl.getCoupeHorizontaleArrive().containsLigne(x, y, TypeDeCoupe.HORIZONTALE)
                        ){
                    return item;
                }
            }else if (item.containsLigne(x, y, typeDeCoupe)) {
                return item;
            }
        }
        return null;
    }
    public Coupe getCoupeAtPoint(Point3D Point3D, TypeDeCoupe typeDeCoupe) {
        for (Coupe coupe : listeCoupe) {
            switch (coupe.getType()){
                case RECTANGULAIRE -> {
                    CoupeRectangulaire cr = (CoupeRectangulaire) coupe;
                    if(cr.getCoupeVerticaleDepart().getType() == typeDeCoupe && cr.getCoupeVerticaleDepart().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)){
                        return cr.getCoupeVerticaleDepart();
                    } else if(cr.getCoupeVerticaleArrive().getType() == typeDeCoupe && cr.getCoupeVerticaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)){
                        return cr.getCoupeVerticaleArrive();
                    } else if (cr.getCoupeHorizontaleDepart().getType() == typeDeCoupe && cr.getCoupeHorizontaleDepart().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)){
                        return cr.getCoupeHorizontaleDepart();
                    } else if (cr.getCoupeHorizontaleArrive().getType() == typeDeCoupe && cr.getCoupeHorizontaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)){
                        return cr.getCoupeHorizontaleArrive();
                    }
                }
                case EN_L -> {
                    CoupeEnL cl = (CoupeEnL) coupe;
                    if(cl.getCoupeVerticaleArrive().getType() == typeDeCoupe && cl.getCoupeVerticaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)){
                        return cl.getCoupeVerticaleArrive();
                    } else if (cl.getCoupeHorizontaleArrive().getType() == typeDeCoupe && cl.getCoupeHorizontaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)){
                        return cl.getCoupeHorizontaleArrive();
                    }
                }
                default -> {
                    if (coupe.getType() == typeDeCoupe && coupe.containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)) {
                        return coupe;
                    }
                }
            }
        }
        return null;
    }
    
    public EdgeType getEdgeAtPoint(Point3D Point3D, TypeDeCoupe typeDeCoupe) {
        if (typeDeCoupe == TypeDeCoupe.VERTICALE) {
            if (isNearEdge(Point3D, EdgeType.LEFT)) {
                return EdgeType.LEFT;
            } else if (isNearEdge(Point3D, EdgeType.RIGHT)) {
                return EdgeType.RIGHT;
            }
        } else if (typeDeCoupe == TypeDeCoupe.HORIZONTALE) {
            if (isNearEdge(Point3D, EdgeType.TOP)) {
                return EdgeType.TOP;
            } else if (isNearEdge(Point3D, EdgeType.BOTTOM)) {
                return EdgeType.BOTTOM;
            }
        }
        return null;
    }
    
    public Coupe getIrregulierCoupeAtPoint(Point3D point, TypeDeCoupe typeDeCoupe){
        for(Coupe coupe : listeCoupe){
            if(coupe.getType() == TypeDeCoupe.RECTANGULAIRE){
                CoupeRectangulaire coupeRec = (CoupeRectangulaire) coupe;
                Point3D departP = coupe.getDepart();
                Point3D arriveP = coupe.getArrive();
                Point3D departAdjacent = new Point3D(departP.getX(), arriveP.getY());
                Point3D arriveAdjacent = new Point3D(arriveP.getX(), departP.getY());
                
                switch (typeDeCoupe){
                    case VERTICALE -> {
                        if(isNearPoint(departP, point)|| isNearPoint(departAdjacent, point) ){
                            return coupeRec.getCoupeVerticaleDepart();
                        } else if(isNearPoint(arriveP, point)|| isNearPoint(arriveAdjacent, point) ){
                            return coupeRec.getCoupeVerticaleArrive();
                        }
                    }
                    case HORIZONTALE -> {
                        if(isNearPoint(departP, point)|| isNearPoint(arriveAdjacent, point) ){
                            return coupeRec.getCoupeHorizontaleArrive();
                        } else if(isNearPoint(departAdjacent, point)|| isNearPoint(arriveP, point) ){
                            return coupeRec.getCoupeHorizontaleDepart();
                        }
                    }
                }
            } else if(coupe.getType() == TypeDeCoupe.EN_L){
                CoupeEnL coupeEnL = (CoupeEnL) coupe;
                Point3D departP = coupe.getDepart();
                Point3D arriveP = coupe.getArrive();
                Point3D departAdjacent = new Point3D(departP.getX(), arriveP.getY());
                Point3D arriveAdjacent = new Point3D(arriveP.getX(), departP.getY());
                
                switch (typeDeCoupe){
                    case VERTICALE -> {
                        if(isNearPoint(arriveP, point)|| isNearPoint(arriveAdjacent, point) ){
                            return coupeEnL.getCoupeVerticaleArrive();
                        }
                    }
                    case HORIZONTALE -> {
                        if(isNearPoint(departAdjacent, point)|| isNearPoint(arriveP, point) ){
                            return coupeEnL.getCoupeHorizontaleArrive();
                        }
                    }
                }
            }
            
        }
        
        return null;
    }
    
    public void ajouterCoupe(Coupe coupe) {
        listeCoupe.add(coupe);
    }

    public void supprimerCoupeSelectionnee() {
        List<Coupe> coupesToRemove = new ArrayList<>();
        for (Coupe coupe : new ArrayList<>(listeCoupe)) {
            if (coupe.isSelected()) {
                coupe.delete(coupesToRemove);
            }
        }
        listeCoupe.removeAll(coupesToRemove);
    }

    public void modifierCoupe(UUID coupeId, Coupe nouvelleCoupe) {
        supprimerCoupeSelectionnee();
        ajouterCoupe(nouvelleCoupe);
    }
    
    void switchSelectionStatus(double x, double y) {
        for (Coupe item : this.listeCoupe) {
            TypeDeCoupe typeDeCoupe = item.getType();
            if(typeDeCoupe == TypeDeCoupe.RECTANGULAIRE){
                CoupeRectangulaire cr = (CoupeRectangulaire) item;
                if(cr.getCoupeVerticaleDepart().containsLigne(x, y, TypeDeCoupe.VERTICALE)
                        || cr.getCoupeVerticaleArrive().containsLigne(x, y, TypeDeCoupe.VERTICALE)
                        || cr.getCoupeHorizontaleDepart().containsLigne(x, y, TypeDeCoupe.HORIZONTALE)
                        || cr.getCoupeHorizontaleArrive().containsLigne(x, y, TypeDeCoupe.HORIZONTALE)
                        ){
                    item.switchSelection();
                }
            } else if(typeDeCoupe == TypeDeCoupe.EN_L){
                CoupeEnL cl=  (CoupeEnL) item;
                if(cl.getCoupeVerticaleArrive().containsLigne(x, y, TypeDeCoupe.VERTICALE)
                        || cl.getCoupeHorizontaleArrive().containsLigne(x, y, TypeDeCoupe.HORIZONTALE)
                        ){
                    item.switchSelection();
                }
            }else if (item.containsLigne(x, y, typeDeCoupe)) {
                item.switchSelection();
            }
        }    
    }
    
    void updateSelectedItemsPosition(double delta) {
        for (Coupe item : this.listeCoupe) {
                if (item.isSelected()) {
                        item.deplacer(delta);
                        if(item instanceof CoupeRectangulaire irreg){
                            irreg.ajusterDimensionsCoupesInternes();
                        }
                }
        }    
    }
    // Vérifier que je n'ai rien cassé (transformé Point3D 2d->3d)
    public boolean panneauContainsPoint3D(Point3D Point3D) {
       return (this.depart.getX() <= Point3D.getX() && Point3D.getX() <= this.arrive.getX()) &&
              (this.depart.getY() <= Point3D.getY() && Point3D.getY() <= this.arrive.getY()) &&
              (this.depart.getZ() <= Point3D.getZ() && Point3D.getZ() <= this.arrive.getZ());
   }

    public boolean estDansZoneInterdite(Point3D Point3D) {
        boolean estDeDans = false;
        for(ZoneInterdite zoneInterdite : this.zoneInterdites){
            estDeDans = zoneInterdite.zoneInterditeContainsPoint3D(Point3D);
        }
        return estDeDans;
    }
    
    public boolean isReferencePoint3DValid(Point3D Point3D, String typeDeCoupeActuel) {
        TypeDeCoupe typeDeCoupe = TypeDeCoupe.valueOf(typeDeCoupeActuel);
        return isNearValidReference(Point3D, typeDeCoupe) || isNearExistingCut (Point3D, typeDeCoupe);
    }

    private boolean isNearValidReference(Point3D Point3D, TypeDeCoupe typeDeCoupe) {
        if (typeDeCoupe == TypeDeCoupe.VERTICALE) {
            return isNearEdge(Point3D, EdgeType.LEFT) || isNearEdge(Point3D, EdgeType.RIGHT);
        } else if (typeDeCoupe == TypeDeCoupe.HORIZONTALE) {
            return isNearEdge(Point3D, EdgeType.TOP) || isNearEdge(Point3D, EdgeType.BOTTOM);
        }
        return false;
    }

    private boolean isNearEdge(Point3D Point3D, EdgeType edge) {
        return switch (edge) {
            case LEFT -> Math.abs(Point3D.getX() - depart.getX()) <= threshold;
            case RIGHT -> Math.abs(Point3D.getX() - arrive.getX()) <= threshold;
            case TOP -> Math.abs(Point3D.getY() - depart.getY()) <= threshold;
            case BOTTOM -> Math.abs(Point3D.getY() - arrive.getY()) <= threshold;
            default -> false;
        };
    }

    private boolean isNearExistingCut(Point3D Point3D, TypeDeCoupe typeDeCoupe) {
        for (Coupe coupe : listeCoupe) {
            if (coupe.getType() == typeDeCoupe && coupe.containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)) {
                return true;
            } else if(coupe.getType() == TypeDeCoupe.RECTANGULAIRE){
                CoupeRectangulaire cr = (CoupeRectangulaire) coupe;
                if(cr.getCoupeVerticaleDepart().getType() == typeDeCoupe && cr.getCoupeVerticaleDepart().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)
                        || cr.getCoupeVerticaleArrive().getType() == typeDeCoupe && cr.getCoupeVerticaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)
                        || cr.getCoupeHorizontaleDepart().getType() == typeDeCoupe && cr.getCoupeHorizontaleDepart().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)
                        || cr.getCoupeHorizontaleArrive().getType() == typeDeCoupe && cr.getCoupeHorizontaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)
                        ){
                    return true;
                }
            } else if(coupe.getType() == TypeDeCoupe.EN_L){
                CoupeEnL cl = (CoupeEnL) coupe;
                if(cl.getCoupeVerticaleArrive().getType() == typeDeCoupe && cl.getCoupeVerticaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)
                        || cl.getCoupeHorizontaleArrive().getType() == typeDeCoupe && cl.getCoupeHorizontaleArrive().containsLigne(Point3D.getX(), Point3D.getY(), typeDeCoupe)
                        ){
                    return true;
                }
            }
        }
        return false;
    }
    
    public double calculateDistanceFromReference(Point3D Point3DReference, Point3D Point3DDeCoupe, TypeDeCoupe typeDeCoupe) {
        if (typeDeCoupe == TypeDeCoupe.VERTICALE) {
            return Point3DDeCoupe.getX() - Point3DReference.getX();
        } else if (typeDeCoupe == TypeDeCoupe.HORIZONTALE) {
            return Point3DDeCoupe.getY() - Point3DReference.getY();
        }
        return 0;
    }
    
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
    public boolean isIntersectionPoint(Point3D Point3D) {
        boolean isIntersection = false;
        // Vérification des intersections avec coupes régulières
        for (Coupe coupe1 : listeCoupe) {
            if (coupe1.getType() == TypeDeCoupe.VERTICALE) {
                for (Coupe coupe2 : listeCoupe) {
                    if (coupe2.getType() == TypeDeCoupe.HORIZONTALE) {
                        if (Math.abs(coupe1.getDepart().getX() - Point3D.getX()) <= threshold &&
                        Math.abs(coupe2.getDepart().getY() - Point3D.getY()) <= threshold){
                            isIntersection = true;
                        }
                    }
                }
            }
            // Vérification des intersections avec coupes irrégulières
            if (isIntersectionWithIrregularCuts(coupe1, Point3D)) {
                return true;
            }
        }
        
        return isIntersection;
    }
    
    private boolean isIntersectionWithIrregularCuts(Coupe coupe, Point3D Point3D) {
        if (coupe instanceof CoupeRectangulaire || coupe instanceof CoupeEnL) {
            List<Coupe> coupesInternes1 = getCoupesInternes(coupe);

            for (Coupe coupeInterne1 : coupesInternes1) {
                for (Coupe coupe2 : listeCoupe) {
                    if (coupe2 instanceof CoupeRectangulaire || coupe2 instanceof CoupeEnL) {
                        List<Coupe> coupesInternes2 = getCoupesInternes(coupe2);

                        for (Coupe coupeInterne2 : coupesInternes2) {
                            if (isIntersectionBetweenTwoCuts(coupeInterne1, coupeInterne2, Point3D)) {
                                return true; 
                            }
                        }
                    } else if (isIntersectionBetweenTwoCuts(coupeInterne1, coupe2, Point3D)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isIntersectionBetweenTwoCuts(Coupe coupe1, Coupe coupe2, Point3D Point3D) {
        return (coupe1.getType() == TypeDeCoupe.VERTICALE && coupe2.getType() == TypeDeCoupe.HORIZONTALE &&
                Math.abs(coupe1.getDepart().getX() - Point3D.getX()) <= threshold &&
                Math.abs(coupe2.getDepart().getY() - Point3D.getY()) <= threshold) ||
               (coupe1.getType() == TypeDeCoupe.HORIZONTALE && coupe2.getType() == TypeDeCoupe.VERTICALE &&
                Math.abs(coupe1.getDepart().getY() - Point3D.getY()) <= threshold &&
                Math.abs(coupe2.getDepart().getX() - Point3D.getX()) <= threshold);
    }
    
    private List<Coupe> getCoupesInternes(Coupe coupe) {
        List<Coupe> coupesInternes = new ArrayList<>();
        switch (coupe) {
            case CoupeRectangulaire rect -> {
                coupesInternes.add(rect.getCoupeVerticaleDepart());
                coupesInternes.add(rect.getCoupeVerticaleArrive());
                coupesInternes.add(rect.getCoupeHorizontaleDepart());
                coupesInternes.add(rect.getCoupeHorizontaleArrive());
            }
            case CoupeEnL enL -> {
                coupesInternes.add(enL.getCoupeVerticaleArrive());
                coupesInternes.add(enL.getCoupeHorizontaleArrive());
            }
            default -> {
            }
        }
        return coupesInternes;
    }
    
    public boolean isNearPoint(Point3D Point3D1, Point3D Point3D2){
        boolean xMatch = Math.abs(Point3D1.getX() - Point3D2.getX()) <= threshold;
        boolean yMatch = Math.abs(Point3D1.getY() - Point3D2.getY()) <= threshold;
        return xMatch && yMatch;
    }
    
public void creerCoupe(TypeDeCoupe typeDeCoupe, Object bordure, ReferenceType typeDeReference, 
                        double distanceFromReference, Outil outil, Point3D referencePoint3D, 
                        Point3D cutPoint3D, EdgeType edgeReference) {
    Coupe coupe = coupeFactory.creerCoupe(typeDeCoupe, bordure, typeDeReference, 
                                           distanceFromReference, outil, this, referencePoint3D, 
                                           cutPoint3D, edgeReference);
    ajouterCoupe(coupe);
}

    
    public boolean isReferencePointValidForIrregularCut(Point3D point3D) {
        return isCornerPoint(point3D) || isIntersectionPoint(point3D) || isIntersectionCutEdge(point3D) || isIntersectionOnACut(point3D);
    }
    
    public boolean isIntersectionCutEdge(Point3D Point3D){
        boolean isIntersection = false;
        for(Coupe coupe : listeCoupe){
            TypeDeCoupe typeDeCoupe = coupe.getType();
            if(typeDeCoupe == TypeDeCoupe.HORIZONTALE || typeDeCoupe == TypeDeCoupe.VERTICALE){
                if(isNearPoint(coupe.getDepart(), Point3D) || isNearPoint(coupe.getArrive(), Point3D)){
                    isIntersection = true;
                }
            }
        }
        
        return isIntersection;
    }
    
    public boolean isIntersectionOnACut(Point3D point){
        boolean near = false;
        for(Coupe coupe : listeCoupe){
            if(coupe.getType() == TypeDeCoupe.RECTANGULAIRE || coupe.getType() == TypeDeCoupe.EN_L){
                Point3D departP = coupe.getDepart();
                Point3D arriveP = coupe.getArrive();
                Point3D departAdjacent = new Point3D(departP.getX(), arriveP.getY());
                Point3D arriveAdjacent = new Point3D(arriveP.getX(), departP.getY());
                
                if(isNearPoint(departP, point) || isNearPoint(arriveP, point)
                        || isNearPoint(departAdjacent, point) || isNearPoint(arriveAdjacent, point)
                        ){
                    near = true;
                }
            }
        }
        
        return near;
    }

    private boolean isCornerPoint(Point3D Point3D) {
        boolean nearTopLeft = Math.abs(Point3D.getX() - depart.getX()) <= threshold && Math.abs(Point3D.getY() - arrive.getY()) <= threshold;
        boolean nearTopRight = Math.abs(Point3D.getX() - arrive.getX()) <= threshold && Math.abs(Point3D.getY() - arrive.getY()) <= threshold;
        boolean nearBottomLeft = Math.abs(Point3D.getX() - depart.getX()) <= threshold && Math.abs(Point3D.getY() - depart.getY()) <= threshold;
        boolean nearBottomRight = Math.abs(Point3D.getX() - arrive.getX()) <= threshold && Math.abs(Point3D.getY() - depart.getY()) <= threshold;
        return nearTopLeft || nearTopRight || nearBottomLeft || nearBottomRight;
    }

    public void creerCoupeBordure( double Hauteur, double Largeur , Outil outil) {
        for(Coupe coupe : listeCoupe){
            if (coupe.getType() == TypeDeCoupe.BORDURE){
                return;
            }
        }
        Coupe coupe;
        coupe = new CoupeBordure(UUID.randomUUID(), outil, this, Hauteur, Largeur);
        ajouterCoupe(coupe);
    }
    public void suppCoupeBordure() {
        List<Coupe> coupesToRemove = new ArrayList<>();
        for (Coupe coupe : new ArrayList<>(listeCoupe)) {
            if (coupe.getType() == TypeDeCoupe.BORDURE) {
                coupe.delete(coupesToRemove);
            }
        }
        listeCoupe.removeAll(coupesToRemove);
    }
    public void modifierCoupeBordure( double Hauteur, double Largeur , Outil outil) {
        
        //ajouterCoupe(coupe);
    }

    private Object getEdgeOrIntersectionAtPoint(Point3D Point3D) {
        if (isCornerPoint(Point3D)) {
            return getEdgeAtCorner(Point3D);
        } else if (isIntersectionPoint(Point3D)) {
            return getCoupeAtPoint(Point3D, TypeDeCoupe.RECTANGULAIRE);
        }
        return null;
    }

    private ReferenceType determineReferenceType(Object bordure) {
        if (bordure instanceof EdgeType) {
            return ReferenceType.EDGE;
        } else if (bordure instanceof Coupe) {
            return ReferenceType.COUPE;
        }
        return null;
    }

    private EdgeType getEdgeAtCorner(Point3D Point3D) {
        if (Math.abs(Point3D.getX() - depart.getX()) <= threshold && Math.abs(Point3D.getY() - depart.getY()) <= threshold) {
            return EdgeType.TOP_LEFT;
        }
        if (Math.abs(Point3D.getX() - arrive.getX()) <= threshold && Math.abs(Point3D.getY() - depart.getY()) <= threshold) {
            return EdgeType.TOP_RIGHT;
        }
        if (Math.abs(Point3D.getX() - depart.getX()) <= threshold && Math.abs(Point3D.getY() - arrive.getY()) <= threshold) {
            return EdgeType.BOTTOM_LEFT;
        }
        if (Math.abs(Point3D.getX() - arrive.getX()) <= threshold && Math.abs(Point3D.getY() - arrive.getY()) <= threshold) {
            return EdgeType.BOTTOM_RIGHT;
        }
        return null;
    }
    
    public void creerCoupeRectangulaire(Point3D Point3DReference, Point3D firstPoint3DDeCoupe, Point3D secondPoint3DDeCoupe, Outil outil) {
        Object b1 = this.getEdgeAtPoint(Point3DReference, TypeDeCoupe.VERTICALE);
        ReferenceType ref1 = ReferenceType.EDGE;
        
        if(b1 == null){
            b1 = this.getCoupeAtPoint(Point3DReference, TypeDeCoupe.VERTICALE);
            ref1 = ReferenceType.COUPE;
            
            if(b1 == null){
                b1 = this.getIrregulierCoupeAtPoint(Point3DReference, TypeDeCoupe.VERTICALE);
            }
        }
        
        
        double d1 = this.calculateDistanceFromReference(Point3DReference, firstPoint3DDeCoupe, TypeDeCoupe.VERTICALE);
        double d3 = this.calculateDistanceFromReference(Point3DReference, secondPoint3DDeCoupe, TypeDeCoupe.VERTICALE);

        Object b2 = this.getEdgeAtPoint(Point3DReference, TypeDeCoupe.HORIZONTALE);
        ReferenceType ref2 = ReferenceType.EDGE;
        
        if(b2 == null){
            b2 = this.getCoupeAtPoint(Point3DReference, TypeDeCoupe.HORIZONTALE);
            ref2 = ReferenceType.COUPE;
            
            if(b2 == null){
                b2 = this.getIrregulierCoupeAtPoint(Point3DReference, TypeDeCoupe.HORIZONTALE);
            }
        }
        
        double d2 = this.calculateDistanceFromReference(Point3DReference, firstPoint3DDeCoupe, TypeDeCoupe.HORIZONTALE);
        double d4 = this.calculateDistanceFromReference(Point3DReference, secondPoint3DDeCoupe, TypeDeCoupe.HORIZONTALE);
        EdgeType edgeType = EdgeType.LEFT;
        EdgeType edgeType2 = EdgeType.TOP;
        CoupeRectangulaire coupeRect = new CoupeRectangulaire(
            UUID.randomUUID(),
            b1,
            b2,
            ref1,
            ref2,
            d1,
            d2,
            d3,
            d4,
            outil,
            this,
            edgeType
        );

        ajouterCoupe(coupeRect);
    }
    
public void modifierOutil(String nomOutil, double nouveauDiametre, Outil outilAModifier) {  
    if (nouveauDiametre <= 0) {
        throw new IllegalArgumentException("Le diamètre doit être supérieur à 0.");
    }


    outilAModifier.setLargeur(nouveauDiametre);

    for (Coupe coupe : listeCoupe) {
        coupe.calculateDepartArrive(); 
    }

    System.out.println("Outil " + nomOutil + " mis à jour avec un diamètre de " + nouveauDiametre);
}


    public void creerCoupeEnL(Point3D Point3DReference, Point3D secondPoint3DDeCoupe, Outil outil) {
        Object b1 = this.getEdgeAtPoint(Point3DReference, TypeDeCoupe.VERTICALE);
        ReferenceType ref1 = ReferenceType.EDGE;
        
        if(b1 == null){
            b1 = this.getCoupeAtPoint(Point3DReference, TypeDeCoupe.VERTICALE);
            ref1 = ReferenceType.COUPE;
            
            if(b1 == null){
                b1 = this.getIrregulierCoupeAtPoint(Point3DReference, TypeDeCoupe.VERTICALE);
            }
        }
        
        double d1 = 0;
        double d3 = this.calculateDistanceFromReference(Point3DReference, secondPoint3DDeCoupe, TypeDeCoupe.VERTICALE);

        Object b2 = this.getEdgeAtPoint(Point3DReference, TypeDeCoupe.HORIZONTALE);
        ReferenceType ref2 = ReferenceType.EDGE;
        
        if(b2 == null){
            b2 = this.getCoupeAtPoint(Point3DReference, TypeDeCoupe.HORIZONTALE);
            ref2 = ReferenceType.COUPE;
            
            if(b2 == null){
                b2 = this.getIrregulierCoupeAtPoint(Point3DReference, TypeDeCoupe.HORIZONTALE);
            }
        }
        
        double d2 = 0;
        double d4 = this.calculateDistanceFromReference(Point3DReference, secondPoint3DDeCoupe, TypeDeCoupe.HORIZONTALE);
        EdgeType edgeType = EdgeType.LEFT;
        CoupeEnL CoupeEnL = new CoupeEnL(
            UUID.randomUUID(),
            b1,
            b2,
            ref1,
            ref2,
            d1,
            d2,
            d3,
            d4,
            outil,
            this,
            edgeType  
        );
        ajouterCoupe(CoupeEnL);
    }
    
    public void ExportGcode(double vitesse, double spin){
        Gcode gcode = new Gcode(this.listeCoupe, vitesse, spin, this);
        gcode.Generate();
    } 
    
    public void creerNouveauPanneau(double width, double height, double depth) {
      if (width <= 0 || height <= 0 || depth <= 0) {
          throw new IllegalArgumentException("Les dimensions doivent être positive");
      }

      this.depart = new Point3D(0, 0, 0);
      this.arrive = new Point3D(width, height, 0);
      this.profondeur = new Point3D(0, 0, depth);

      if (this.listeCoupe == null) {
          this.listeCoupe = new ArrayList<>();
      } else {
          this.listeCoupe.clear();
      }

      if (this.zoneInterdites == null) {
          this.zoneInterdites = new ArrayList<>();
      } else {
          this.zoneInterdites.clear();
      }

      notifyObservers();
    }
    
    public void setListeCoupe(List<Coupe> coupes) {
    this.listeCoupe = new ArrayList<>(coupes);
    notifyObservers();
    }
    public List<Coupe> getListeCoupe() {
    return this.listeCoupe;
    }



}
