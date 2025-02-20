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
public class CoupeDTO {
    private UUID id;                
    private TypeDeCoupe type;   
    private Point3D depart;
    private Point3D arrive;     
    
    
    public CoupeDTO(UUID id, TypeDeCoupe type, Point3D depart, Point3D arrive){
        this.id = id;
        this.type = type;
        this.depart = depart;
        this.arrive = arrive;
    }
    
    public static CoupeDTO fromCoupe(Coupe coupe) {
        return new CoupeDTO(coupe.getId(), coupe.getType(), coupe.getDepart(), coupe.getArrive());
    }
    
    public Point3D getDepart(){
        return this.depart;
    }
    
    public Point3D getArrive(){
        return this.arrive;
    }
}