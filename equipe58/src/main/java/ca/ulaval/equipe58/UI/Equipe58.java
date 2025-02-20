/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ca.ulaval.equipe58.UI;
import ca.ulaval.equipe58.domaine.Controller;
import javax.swing.*;




/**
 * Livrable 2 
 * @author Équipe 58 : Haytem Chahid, Mouad Malhoud, Vincent Bellemare, Nicolas Bélanger, Mamadou Alpha Diallo
 * 
 */
public class Equipe58 {
    private Controller controller;
    private Afficheur afficheur;
    private Afficheur frame;
    
    public Equipe58() {
//        this.controller = new Controller(cncDto pt)
        this.frame = new Afficheur();
    }

    

    

 public static void main(String[] args) {
      System.out.println("Hello world from equipe 58");


      
      
      
       SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                Afficheur frame = new Afficheur();
                frame.setVisible(true);  
            }
        });
    }
 }



