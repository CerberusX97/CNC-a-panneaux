package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import static ca.ulaval.equipe58.domaine.EdgeType.BOTTOM;
import static ca.ulaval.equipe58.domaine.EdgeType.LEFT;
import static ca.ulaval.equipe58.domaine.EdgeType.RIGHT;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoupeRectangulaire extends Coupe {private Object b2;
    private ReferenceType ref2;
    private double d2;
    private double d3;
    private double d4;
    
    private EdgeType edgeType;

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) {
        this.edgeType = edgeType;
    }
    
    
    public Object getB2() {
        return b2;
    }

    public void setB2(Object b2) {
        this.b2 = b2;
    }

    public ReferenceType getRef2() {
        return ref2;
    }
    public double getLargeur() {
    return Math.abs(d3);
}
@Override
public CoupeRectangulaire clone() {
    CoupeRectangulaire cloned = (CoupeRectangulaire) super.clone();
    cloned.b2 = this.b2;
    cloned.ref2 = this.ref2;
    cloned.d2 = this.d2;
    cloned.d3 = this.d3;
    cloned.d4 = this.d4;
    cloned.coupeVerticaleDepart = this.coupeVerticaleDepart.clone();
    cloned.coupeVerticaleArrive = this.coupeVerticaleArrive.clone();
    cloned.coupeHorizontaleDepart = this.coupeHorizontaleDepart.clone();
    cloned.coupeHorizontaleArrive = this.coupeHorizontaleArrive.clone();
    return cloned;
}
public double getHauteur() {
    return Math.abs(d4);
}

    public void setRef2(ReferenceType ref2) {
        this.ref2 = ref2;
    }

    public double getD2() {
        return d2;
    }

    public void setD2(double d2) {
        this.d2 = d2;
    }

    public double getD3() {
        return d3;
    }

    public void setD3(double d3) {
        this.d3 = d3;
    }

    public double getD4() {
        return d4;
    }

    public void setD4(double d4) {
        this.d4 = d4;
    }

    private String coupeVerticaleStartRef;
    private String coupeVerticaleEndRef;
    private String coupeHorizontaleStartRef;
    private String coupeHorizontaleEndRef;
    private Coupe coupeVerticaleDepart;
    private Coupe coupeVerticaleArrive;
    private Coupe coupeHorizontaleDepart;
    private Coupe coupeHorizontaleArrive;

     public CoupeRectangulaire(UUID id, CoupeVerticale vertStart, CoupeVerticale vertEnd, 
                             CoupeHorizontale horizStart, CoupeHorizontale horizEnd,
                             Outil outil, Panneau panneau, EdgeType edgeType) {
        super(id, TypeDeCoupe.RECTANGULAIRE, vertStart, ReferenceType.COUPE, 0.0, outil, panneau);
        this.coupeVerticaleDepart = vertStart;
        this.coupeVerticaleArrive = vertEnd;
        this.coupeHorizontaleDepart = horizStart;
        this.coupeHorizontaleArrive = horizEnd;
        this.edgeType = edgeType;
        
        calculateDepartArrive();
    }
    public CoupeRectangulaire(UUID id, Object b1, Object b2, ReferenceType ref1, ReferenceType ref2, 
                              double d1, double d2, double d3, double d4, Outil outil, Panneau panneau, EdgeType edgeType) {
        super(id, TypeDeCoupe.RECTANGULAIRE, b1, ref1, d1, outil, panneau);
        
        this.coupeVerticaleDepart = new CoupeVerticale(id, b1, ref1, d1, outil, panneau, edgeType);
        this.coupeVerticaleArrive = new CoupeVerticale(id, b1, ref1, d3, outil, panneau, edgeType);
        this.coupeHorizontaleDepart = new CoupeHorizontale(id, b2, ref2, d2, outil, panneau, edgeType);
        this.coupeHorizontaleArrive = new CoupeHorizontale(id, b2, ref2, d4, outil, panneau, edgeType);
        
        this.coupeVerticaleStartRef = coupeVerticaleDepart.getReference();
        this.coupeVerticaleEndRef = coupeVerticaleArrive.getReference();
        this.coupeHorizontaleStartRef = coupeHorizontaleDepart.getReference();
        this.coupeHorizontaleEndRef = coupeHorizontaleArrive.getReference();
        
        this.b2 = b2;
        this.d2 = d2;
        this.d3 = d3;
        this.d4 = d4;
        if (ref2 == ReferenceType.COUPE) {
            Coupe ref2Coupe = (Coupe) b2;
            ref2Coupe.addObserver(this); 
        } else if (ref2 == ReferenceType.EDGE) {
            panneau.addObserver(this);
        }
        
        calculateDepartArrive();
    }
    
    
    @Override
    public void calculateDepartArrive() {
        Object b1 = this.getBordure();
        double d1 = this.getDistanceFromReference();

        Panneau panneau = this.getPanneau();

        double b1PosX;
        double b2PosY;

        if (b1 instanceof EdgeType) {
            EdgeType edge = (EdgeType) b1;
            switch (edge) {
                case LEFT:
                    b1PosX = panneau.getDepartPanneau().getX();
                    break;
                case RIGHT:
                    b1PosX = panneau.getArrivePanneau().getX();
                    break;
                default:
                    throw new IllegalArgumentException("b1 doit être une bordure verticale (LEFT ou RIGHT)");
            }
        } else if (b1 instanceof Coupe) {
            Coupe coupeB1 = (Coupe) b1;
            if (coupeB1.getType() == TypeDeCoupe.VERTICALE) {
                b1PosX = coupeB1.getDepart().getX();
            } else {
                throw new IllegalArgumentException("b1 doit être une coupe verticale");
            }
        } else {
            throw new IllegalArgumentException("b1 doit être soit un EdgeType, soit une Coupe");
        }

        if (b2 instanceof EdgeType) {
            EdgeType edge = (EdgeType) b2;
            switch (edge) {
                case TOP:
                    b2PosY = panneau.getDepartPanneau().getY();
                    break;
                case BOTTOM:
                    b2PosY = panneau.getArrivePanneau().getY();
                    break;
                default:
                    throw new IllegalArgumentException("b2 doit être une bordure horizontale (TOP ou BOTTOM)");
            }
        } else if (b2 instanceof Coupe) {
            Coupe coupeB2 = (Coupe) b2;
            if (coupeB2.getType() == TypeDeCoupe.HORIZONTALE) {
                b2PosY = coupeB2.getDepart().getY();
            } else {
                throw new IllegalArgumentException("b2 doit être une coupe horizontale");
            }
        } else {
            throw new IllegalArgumentException("b2 doit être soit un EdgeType, soit une Coupe");
        }

        double x1 = b1PosX + d1;
        double y1 = b2PosY + d2;
        double x2 = b1PosX + d3;
        double y2 = b2PosY + d4;

        setDepart(new Point3D(Math.min(x1, x2), Math.min(y1, y2)));
        setArrive(new Point3D(Math.max(x1, x2), Math.max(y1, y2)));
        
        ajusterDimensionsCoupesInternes();
    }
    
    public void ajusterDimensionsCoupesInternes() {
        Point3D departRect = this.getDepart();
        Point3D arriveRect = this.getArrive();

        this.coupeVerticaleDepart.setDepart(new Point3D(departRect.getX(), departRect.getY()));
        this.coupeVerticaleDepart.setArrive(new Point3D(departRect.getX(), arriveRect.getY()));

        this.coupeVerticaleArrive.setDepart(new Point3D(arriveRect.getX(), departRect.getY()));
        this.coupeVerticaleArrive.setArrive(new Point3D(arriveRect.getX(), arriveRect.getY()));

        this.coupeHorizontaleDepart.setDepart(new Point3D(departRect.getX(), departRect.getY()));
        this.coupeHorizontaleDepart.setArrive(new Point3D(arriveRect.getX(), departRect.getY()));

        this.coupeHorizontaleArrive.setDepart(new Point3D(departRect.getX(), arriveRect.getY()));
        this.coupeHorizontaleArrive.setArrive(new Point3D(arriveRect.getX(), arriveRect.getY()));
    }
    
    public List<Coupe> getCoupesInternes() {
        List<Coupe> coupes = new ArrayList<>();
        coupes.add(coupeVerticaleDepart);
        coupes.add(coupeVerticaleArrive);
        coupes.add(coupeHorizontaleDepart);
        coupes.add(coupeHorizontaleArrive);
        return coupes;
    }
    
    @Override
    public void deplacer(double delta) {
        double d1 = this.getDistanceFromReference();
        
        this.d3 = (d3 - d1) + delta;
        this.d4 = (d4 - d2) + delta;
        this.setDistanceFromReference(delta);
        this.d2 = delta;

        // Recalculer les points de départ et d'arrivée du rectangle
        calculateDepartArrive();
    }
    
    // Méthodes pour accéder aux coupes individuelles
    public Coupe getCoupeVerticaleDepart() {
        return coupeVerticaleDepart;
    }

    public Coupe getCoupeVerticaleArrive() {
        return coupeVerticaleArrive;
    }

    public Coupe getCoupeHorizontaleDepart() {
        return coupeHorizontaleDepart;
    }

    public Coupe getCoupeHorizontaleArrive() {
        return coupeHorizontaleArrive;
    }

    
        @Override
    public void update() {
        // Recalculer les points départ et arrivée
        calculateDepartArrive();
        notifyObservers();
    }

}