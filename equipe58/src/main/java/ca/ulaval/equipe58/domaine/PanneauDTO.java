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
import java.util.List;
import java.util.stream.Collectors;

public class PanneauDTO {
    private Point3D depart;
    private Point3D arrive;
    private Point3D profondeur;
    private List<ZoneInterditeDTO> zoneInterdites;
    private List<CoupeDTO> coupes;

    public PanneauDTO(Point3D depart, Point3D arrive, Point3D profondeur, List<ZoneInterditeDTO> zoneInterdites, List<CoupeDTO> coupes) {
        this.depart = depart;
        this.arrive = arrive;
        this.profondeur = profondeur;
        this.zoneInterdites = zoneInterdites;
        this.coupes = coupes;
    }

    public Point3D getDepart(){
        return this.depart;
    }
    
    public Point3D getArrive(){
        return this.arrive;
    }
  public Point3D profondeur(){
        return this.profondeur;
    }
    public List<ZoneInterditeDTO> getZoneInterdites() {
        return zoneInterdites;
    }

    public List<CoupeDTO> getCoupes() {
        return coupes;
    }

public static PanneauDTO fromPanneau(Panneau panneau) {
    Point3D depart = panneau.getDepartPanneau();
    Point3D arrive = panneau.getArrivePanneau();
    Point3D profondeur = panneau.getProfondeurPanneau();
    
    //System.out.println("depart old: " + depart);
    //System.out.println("arrive old: " + arrive);
    
    //inversement
    double tempX = depart.getX();
    depart = new Point3D(depart.getX(), depart.getY(), 0); 
    arrive = new Point3D(arrive.getX(), arrive.getY(), 0); 


    List<ZoneInterditeDTO> zoneInterditeDtos = panneau.getZoneInterdites().stream()
        .map(ZoneInterditeDTO::fromZoneInterdite)
        .collect(Collectors.toList());

    List<CoupeDTO> coupeDtos = panneau.getCoupes().stream()
        .map(CoupeDTO::fromCoupe)
        .collect(Collectors.toList());

    return new PanneauDTO(depart, arrive, profondeur, zoneInterditeDtos, coupeDtos);
}
}