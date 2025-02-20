/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author Vincent
 */
public class Gcode {
    private List<Coupe> ListeCoupe;
    private double Vitesse;
    private double Spin;
    private Panneau panneau;
    
    public Gcode(List<Coupe> Liste, double vitesse, double spin, Panneau pan){
        this.ListeCoupe = Liste;
        this.Spin = spin;
        this.Vitesse = vitesse;
        this.panneau = pan;
    }
    
    public void Generate() {
    String baseName = "GCODE";
    String extension = ".txt";
    String nomFichierSortie = generateUniqueFileName(baseName, extension);
    double profondeur = panneau.getProfondeurPanneau().getZ();
    double vitesseMmParMin = Vitesse * 1000;
    StringBuilder gcode = new StringBuilder();

    gcode.append("; G-code généré par l'application Panneau\n");
    gcode.append("G21 ; Définir les unités en millimètres\n");
    gcode.append("G90 ; Positionnement absolu\n");
    gcode.append("G17 ; Sélectionner le plan XY\n");
    gcode.append(String.format("S%.2f ; Régler la vitesse de rotation (tr/min)\n", Spin));
    gcode.append("M3 ; Démarrer la broche\n\n");

    for (Coupe coupe : ListeCoupe) {
        Point3D depart = coupe.getDepart();
        Point3D arrive = coupe.getArrive();
        double Loutil = coupe.getOutil().getLargeur()/2;
        TypeDeCoupe typeDeCoupe = coupe.getType();

        if (depart != null && arrive != null) {
            gcode.append(String.format("; Type de coupe : %s\n", typeDeCoupe));

            switch (typeDeCoupe) {
                case BORDURE:
                    gcode.append(String.format("G0 X%.2f Y%.2f ; Aller au point de départ (en bas à gauche)\n", depart.getX() - Loutil, depart.getY() - Loutil));
                    gcode.append(String.format("G1 Z%.2f F%.2f ; Descendre à la profondeur de coupe avec la vitesse définie\n", -profondeur, vitesseMmParMin));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'en haut à gauche\n", depart.getX() - Loutil, arrive.getY() + Loutil));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'en haut à droite\n", arrive.getX() + Loutil, arrive.getY() + Loutil));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'en bas à droite\n", arrive.getX() - Loutil, depart.getY()- Loutil));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Retourner au point de départ (en bas à gauche)\n", depart.getX() - Loutil, depart.getY() - Loutil));
                    gcode.append("G1 Z0 ; Remonter à une hauteur sécuritaire\n\n");
                    break;

                case EN_L:
                    gcode.append(String.format("G0 X%.2f Y%.2f ; Aller au point de départ (en bas à gauche du L)\n", depart.getX(), panneau.getArrivePanneau().getY() - arrive.getY()));
                    gcode.append(String.format("G1 Z%.2f F%.2f ; Descendre à la profondeur de coupe avec la vitesse définie\n", -profondeur, vitesseMmParMin));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe du segment vertical du L\n", arrive.getX(),panneau.getArrivePanneau().getY() - arrive.getY()));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe du segment horizontal du L\n", arrive.getX(),panneau.getArrivePanneau().getY() - depart.getY()));
                    gcode.append("G1 Z0 ; Remonter à une hauteur sécuritaire\n\n");
                    break;

                case RECTANGULAIRE:
                    gcode.append(String.format("G0 X%.2f Y%.2f ; Aller au point de départ (en bas à gauche)\n", depart.getX(), panneau.getArrivePanneau().getY() - depart.getY()));
                    gcode.append(String.format("G1 Z%.2f F%.2f ; Descendre à la profondeur de coupe avec la vitesse définie\n", -profondeur, vitesseMmParMin));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'en haut à gauche\n", depart.getX(), panneau.getArrivePanneau().getY() -  arrive.getY()));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'en haut à droite\n", arrive.getX(), panneau.getArrivePanneau().getY() -  arrive.getY()));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'en bas à droite\n", arrive.getX(), panneau.getArrivePanneau().getY() -  depart.getY()));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Retourner au point de départ (en bas à gauche)\n", depart.getX(), panneau.getArrivePanneau().getY() -  depart.getY()));
                    gcode.append("G1 Z0 ; Remonter à une hauteur sécuritaire\n\n");
                    break;

                case VERTICALE:
                    gcode.append(String.format("G0 X%.2f Y%.2f ; Aller au point de départ (en bas de la ligne verticale)\n", depart.getX(), depart.getY()));
                    gcode.append(String.format("G1 Z%.2f F%.2f ; Descendre à la profondeur de coupe avec la vitesse définie\n", -profondeur, vitesseMmParMin));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'en haut de la ligne verticale\n", depart.getX(), arrive.getY()));
                    gcode.append("G1 Z0 ; Remonter à une hauteur sécuritaire\n\n");
                    break;

                case HORIZONTALE:
                    gcode.append(String.format("G0 X%.2f Y%.2f ; Aller au point de départ (à gauche de la ligne horizontale)\n", depart.getX(), panneau.getArrivePanneau().getY() - depart.getY()));
                    gcode.append(String.format("G1 Z%.2f F%.2f ; Descendre à la profondeur de coupe avec la vitesse définie\n", -profondeur, vitesseMmParMin));
                    gcode.append(String.format("G1 X%.2f Y%.2f ; Découpe jusqu'à droite de la ligne horizontale\n", arrive.getX(),panneau.getArrivePanneau().getY() -  depart.getY()));
                    gcode.append("G1 Z0 ; Remonter à une hauteur sécuritaire\n\n");
                    break;

                default:
                    gcode.append("; Type de coupe non supporté\n");
                    break;
            }
        } else {
            gcode.append("; Coupe invalide ignorée (points nuls)\n");
        }
    }

    gcode.append("\nM5 ; Arrêter la broche\n");
    gcode.append("G0 Z10 ; Remonter l'outil\n");
    gcode.append("G0 X0 Y0 ; Retourner au Points d'origine\n");
    gcode.append("M30 ; Fin du programme\n");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichierSortie))) {
        writer.write(gcode.toString());
        System.out.println("G-code généré avec succès. Fichier enregistré sous : " + nomFichierSortie);
    } catch (IOException e) {
        System.err.println("Erreur lors de l'écriture du fichier G-code : " + e.getMessage());
    }
}
    
    private String generateUniqueFileName(String baseName, String extension) {
        int counter = 1;
        String fileName = baseName + extension;
        while (Files.exists(new File(fileName).toPath())) {
            fileName = baseName + " (" + counter + ")" + extension;
            counter++;
        }
        return fileName;
    }
}