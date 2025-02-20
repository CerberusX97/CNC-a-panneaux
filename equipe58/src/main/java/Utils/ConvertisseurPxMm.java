/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author ADMO-PC
 */
public class ConvertisseurPxMm {
    private static final double DPI = 96; 

    
      // Méthode pour convertir les pixels en mm
    public double convertPxToMm(double pixels) {
        return (pixels / DPI) * 25.4;
    }

    // Méthode pour convertir de mm en px
    public double convertMmToPx(double mm) {
        return (double) ((mm * DPI) / 25.4);
    }
}
