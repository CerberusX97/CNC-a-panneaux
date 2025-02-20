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
public class CoupeEnL extends Coupe{
    private Object b2;
    private double d2;
    private double d3;
    private double d4;
    private ReferenceType ref2;
    private EdgeType edgeReference1;
     @Override
    public ReferenceType getTypeDeReference(){
        return super.getTypeDeReference();
    }
    @Override
    public double getDistanceFromReference() {
        return super.getDistanceFromReference(); 
    }

    public ReferenceType getRef2() {
        return ref2;
    }

    public void setRef2(ReferenceType ref2) {
        this.ref2 = ref2;
    }
    
    private Coupe coupeVerticaleArrive;
    private Coupe coupeHorizontaleArrive;

    public Object getB2() {
        return b2;
    }

    public void setB2(Object b2) {
        this.b2 = b2;
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
@Override
public CoupeEnL clone() {
    CoupeEnL cloned = (CoupeEnL) super.clone();
    cloned.b2 = this.b2;
    cloned.d2 = this.d2;
    cloned.d3 = this.d3;
    cloned.d4 = this.d4;
    cloned.coupeVerticaleArrive = this.coupeVerticaleArrive.clone();
    cloned.coupeHorizontaleArrive = this.coupeHorizontaleArrive.clone();
    return cloned;
}
    public double getD4() {
        return d4;
    }

    public void setD4(double d4) {
        this.d4 = d4;
    }
    private EdgeType edgeType;

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) {
        this.edgeType = edgeType;
    }
    public CoupeEnL(UUID id, Object b1, Object b2,  ReferenceType ref1, ReferenceType ref2, double d1, double d2, double d3, double d4, Outil outil, Panneau panneau, EdgeType edgeType) {
        super(id, TypeDeCoupe.EN_L, b1, ref1, d1, outil, panneau);
        
        this.coupeVerticaleArrive = new CoupeVerticale(id, b1, ref1, d3, outil, panneau, edgeType);
        this.coupeHorizontaleArrive = new CoupeHorizontale(id, b2, ref2, d4, outil, panneau, edgeType);
        
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

    public Coupe getCoupeVerticaleArrive() {
        return coupeVerticaleArrive;
    }

    public Coupe getCoupeHorizontaleArrive() {
        return coupeHorizontaleArrive;
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

        System.out.println("x1 : " + x1 + "y1 : " + y1 + "x2 : " + x2 + "y2 : " + y2);
        setDepart(new Point3D(x1, y1));
        setArrive(new Point3D(x2, y2));
    }
    
}
