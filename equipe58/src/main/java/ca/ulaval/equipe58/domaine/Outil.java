/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMO-PC
 */
public class Outil {
    private String nom;
    private boolean isUpdating = false;
    private double largeur;  
    private List<Coupe> observers;
    
    public Outil(String nom, double largeur){
        this.nom = nom;
        this.largeur = largeur;
        this.observers = new ArrayList<>();
    }

    public double getLargeur() {
        notifyObservers();
        return largeur;
    }

    public String getNom() {
        return nom;
    }
    
    private void notifyObservers() {

        if (isUpdating) return;
        isUpdating = true;
        for (Coupe coupe : observers) {
            System.out.println("qqch ce passe : ");
            coupe.onOutilUpdated(this);
        }
        isUpdating = false;
    }
    
    public void addObserver(Coupe coupe){
        observers.add(coupe);
    }
        
    public void removeObserver(Coupe coupe){
        observers.remove(coupe);
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
        notifyObservers(); 
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void modifier(double nouveauDiametre) {
    if (nouveauDiametre <= 0) {
        throw new IllegalArgumentException("Le diamètre doit être supérieur à 0.");
    }
    this.setLargeur(nouveauDiametre);
    System.out.println("Diamètre de l'outil modifié : " + nouveauDiametre);
}
    
}
