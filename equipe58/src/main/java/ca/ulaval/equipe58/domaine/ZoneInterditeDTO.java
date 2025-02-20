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
public class ZoneInterditeDTO {
    private UUID id;
    private Point3D depart;
    private Point3D arrive;
    
    public ZoneInterditeDTO(UUID id, Point3D depart, Point3D arrive){
    this.id = id;
    this.depart = depart;
    this.arrive = arrive;
    }
    
    public static ZoneInterditeDTO fromZoneInterdite(ZoneInterdite zoneInterdite) {
        return new ZoneInterditeDTO(zoneInterdite.getId(), zoneInterdite.getDepartZI(), zoneInterdite.getArriveZI());
    }
}

