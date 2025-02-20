/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.util.UUID;

/**
 *
 * @author ADMO-PC
 */
public class ZoneInterdite {
    private UUID id;
    private Point3D departZI;
    private Point3D arriveZI;
    
    public ZoneInterdite(UUID id, Point3D departZI, Point3D arriveZI){
    this.id = id;
    this.departZI = departZI;
    this.arriveZI = arriveZI;
    }

    public UUID getId() {
        return id;
    }
    
    public Point3D getDepartZI(){
        return this.departZI;
    }
    
    public Point3D getArriveZI(){
        return this.arriveZI;
    }
    
    public double getLongueur()
    {
        double deltaX = arriveZI.getX() - departZI.getX();
        return deltaX;
    }
    
    public double getHauteur()
    {
        double deltaY = arriveZI.getY() - departZI.getY();
        return deltaY;
    } 

    public boolean zoneInterditeContainsPoint3D(Point3D Point3D){
        return (this.departZI.getX() <= Point3D.getX() && Point3D.getX() <= this.arriveZI.getX()) && 
                (this.departZI.getY() <= Point3D.getY() && Point3D.getY() <= this.arriveZI.getY());
    }
}
