/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ADMO-PC
 */
public abstract class Coupe implements Subject, Observer, Cloneable {
    private UUID id;                
    private TypeDeCoupe type;  
    protected double radius;
    private String reference;
    private String savedHash;
    protected EdgeType edgeReference;

    private Outil outil;
    private boolean selectionStatus;
    private Point3D depart;
    private Point3D arrive;
    private boolean isUpdating = false;
    private Panneau panneau;   
    public void setReference(String reference) {
        this.reference = reference;
        if (reference != null && reference.contains("@")) {
            this.savedHash = reference.substring(reference.lastIndexOf('@') + 1);
        }
    }
     public void setEdgeReference(EdgeType edgeReference) {
        this.edgeReference = edgeReference;
    }
    
    public EdgeType getEdgeReference() {
        return this.edgeReference;
    }
    @Override
    public String toString() {
        if (savedHash != null) {
            return this.getClass().getName() + "@" + savedHash;
        }
        return this.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(this));
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setSelectionStatus(boolean selectionStatus) {
        this.selectionStatus = selectionStatus;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public void setPanneau(Panneau panneau) {
        this.panneau = panneau;
    }

    public void setTypeDeReference(ReferenceType typeDeReference) {
        this.typeDeReference = typeDeReference;
    }

    public void setDistanceFromReference(double distanceFromReference) {
        this.distanceFromReference = distanceFromReference;
    }
   @Override
    public Coupe clone() {
        try {
            Coupe cloned = (Coupe) super.clone();
            cloned.depart = new Point3D(this.depart.getX(), this.depart.getY(), this.depart.getZ());
            cloned.arrive = new Point3D(this.arrive.getX(), this.arrive.getY(), this.arrive.getZ());
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clonage non supporté", e);
        }
    }


    public void setBordure(Object bordure) {
        this.bordure = bordure;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }
    private ReferenceType typeDeReference;
    private double distanceFromReference;
    private Object bordure;
    private List<Observer> observers = new ArrayList<>();
    
    public ReferenceType getTypeDeReference(){
        return typeDeReference;
    }
    public Coupe(UUID id) {
        this.id = id;
        String defaultRef = this.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(this));
        this.reference = defaultRef;
        this.savedHash = Integer.toHexString(System.identityHashCode(this));
    }
    public Coupe(UUID id, TypeDeCoupe type, Object bordure, ReferenceType typeDeReference, double distanceFromReference, Outil outil, Panneau panneau){
        this.id = id;
        this.type = type;
        this.bordure = bordure;
        this.typeDeReference = typeDeReference;
        this.distanceFromReference = distanceFromReference;
        this.outil = outil;
        this.panneau = panneau;
        this.selectionStatus = false;
        this.radius = 10;
        this.reference = this.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(this));

        if (typeDeReference == ReferenceType.COUPE) {
            Coupe referenceCoupe = (Coupe) bordure;
            referenceCoupe.addObserver(this); 
        } else if (typeDeReference == ReferenceType.EDGE) {
            panneau.addObserver(this);
        }
    }

    protected abstract void calculateDepartArrive();
    public String getReference() {
            return reference;
        }
    public UUID getId() {
        return id;
    }

    public TypeDeCoupe getType() {
        return type;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
     public Outil getOutil() {
        return outil;  
    }
    public void setOutil(Outil nouvelOutil) {
        if (this.outil != null) {
            this.outil.removeObserver(this);
        }
        this.outil = nouvelOutil;
        if (nouvelOutil != null) {
            nouvelOutil.addObserver(this);
        }
    }
    
    
    public void setType(TypeDeCoupe type) {
        this.type = type;
    }
    
    void switchSelection() {
            this.selectionStatus = !this.selectionStatus;
    }

    public double getRadius() {
        return this.radius;
    }

    public boolean isSelected() {
        return this.selectionStatus;
    }
    
    public Point3D getDepart(){
        return this.depart;
    }
    
    public Point3D getArrive(){
        return this.arrive;
    }
    
    public void setDepart(Point3D depart){
        this.depart = depart;
    }
    
    public void setArrive(Point3D arrive){
        this.arrive = arrive;
    }
    
    public double getLongueur()
    {
        double deltaX = arrive.getX() - depart.getX();
        return deltaX;
    }
    
    public double getHauteur()
    {
        double deltaY = arrive.getY() - depart.getY();
        return deltaY;
    }
    
    public void deplacer(double delta){
        double distance = this.getDistanceFromReference();
        if(distance < 0) {
            this.distanceFromReference = - delta;
        }else {
         this.distanceFromReference = delta;
        }

        this.calculateDepartArrive();
        notifyObservers();
    } 
    
    
    
    public Panneau getPanneau(){
        return this.panneau;
    }
    
    
    public Object getBordure(){
        return this.bordure;
    }

    public double getDistanceFromReference() {
        return distanceFromReference;
    }
    
    public boolean containsLigne(double x, double y, TypeDeCoupe typeDeCoupe) {
        return switch (typeDeCoupe) {
            case VERTICALE -> xIsNearCutLine(x);
            case HORIZONTALE -> yIsNearCutLine(y);
            default -> false;
        };    
    }

    private boolean xIsNearCutLine(double x) {
        return Math.abs(x - this.arrive.getX()) <= radius;
    }
    
    public void onOutilUpdated(Outil outil) {
        if (isUpdating) return;
        if (this.outil == outil) {
            calculateDepartArrive();
            notifyObservers();
        }
        isUpdating = false;       
    }
    
    private boolean yIsNearCutLine(double y) {
        return Math.abs(y - this.arrive.getY()) <= radius;
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
        for(Observer o : observers) {
            o.update();
        }
    }
    
    @Override
    public void update() {
        calculateDepartArrive();
        notifyObservers();
    }
    
    public void delete(List<Coupe> coupesToRemove) {
        List<Observer> observersCopy = new ArrayList<>(observers);

        for (Observer observer : observersCopy) {
            if (observer instanceof Coupe coupeObserver) {
                coupeObserver.delete(coupesToRemove);
            }
        }
        if (typeDeReference == ReferenceType.COUPE) {
            Coupe referenceCoupe = (Coupe) bordure;
            referenceCoupe.removeObserver(this);
        } else if (typeDeReference == ReferenceType.EDGE) {
            panneau.removeObserver(this);
        }
        observers.clear();
        coupesToRemove.add(this);
    }

}
