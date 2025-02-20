/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

/**
 *
 * @author ADMO-PC
 */
public class OutilDTO {
    private String nom;          
    private double largeur;   
    
    public OutilDTO(String nom, double largeur){
        this.nom = nom;
        this.largeur = largeur;
    }
    
    public static OutilDTO fromOutil(Outil outil){
        return new OutilDTO(outil.getNom(), outil.getLargeur());
    }
    
    public String getNom() {
        return nom;
    }
    
    public double getLargeur() {
        return largeur;
    }
    

}

