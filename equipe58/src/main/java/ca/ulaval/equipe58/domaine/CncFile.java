package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CncFile {

    private List<Coupe> listeCoupe;
    private Panneau panneau;
    private Cnc cnc;

    public CncFile(List<Coupe> listeCoupe, Panneau panneau, Cnc cnc) {
        this.listeCoupe = listeCoupe;
        this.panneau = panneau;
        this.cnc = cnc;
    }

    public void genererCNCFile(String filePath) {
        System.out.println("je suis appelé");
        StringBuilder cncContent = new StringBuilder();
        cncContent.append("; Info panneau\n");
        cncContent.append(String.format("DIMENSIONS Panneau: Depart = (%.2f, %.2f, %.2f), Arrive = (%.2f, %.2f, %.2f), Profondeur = %.2f\n",
                panneau.getDepartPanneau().getX(), panneau.getDepartPanneau().getY(), panneau.getDepartPanneau().getZ(),
                panneau.getArrivePanneau().getX(), panneau.getArrivePanneau().getY(), panneau.getArrivePanneau().getZ(),
                panneau.getProfondeurPanneau().getZ()));
        cncContent.append("\n");
        cncContent.append("; Outils utilisés\n");
        int outilCount = 1;
        List<Outil> outilsUtilises = listeCoupe.stream()
                .map(Coupe::getOutil)
                .distinct()
                .collect(Collectors.toList());

        for (Outil outil : outilsUtilises) {
            cncContent.append(String.format("Tool %d: Nom = %s, Largeur = %.2fmm\n",
                    outilCount++, outil.getNom(), outil.getLargeur()));
        }

        System.out.println("Outils utilisés: " + outilsUtilises.size());

        cncContent.append("\n; Outils disponibles non utilisés\n");
        List<Outil> tousLesOutils = cnc.getOutils();
        List<Outil> outilsNonUtilises = tousLesOutils.stream()
                .filter(outil -> !outilsUtilises.contains(outil))
                .collect(Collectors.toList());

        System.out.println("Tous les outils: " + tousLesOutils.size());
        System.out.println("Outils non utilisés: " + outilsNonUtilises.size());

        for (Outil outil : outilsNonUtilises) {
            cncContent.append(String.format("Tool %d: Nom = %s, Largeur = %.2fmm\n",
                    outilCount++, outil.getNom(), outil.getLargeur()));
        }
        cncContent.append("\n");
        cncContent.append("; Details Coupes\n");
        for (int i = 0; i < listeCoupe.size(); i++) {
            Coupe coupe = listeCoupe.get(i);
            cncContent.append(genererDetailCoupe(i + 1, coupe));
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(cncContent.toString());
            System.out.println("Fichier .cnc généré avec succès : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier .cnc : " + e.getMessage());
        }
    }

    private String genererDetailCoupe(int numero, Coupe coupe) {
        Point3D depart = coupe.getDepart();
        Point3D arrive = coupe.getArrive();
        Outil outil = coupe.getOutil();
        String typeCoupe = coupe.getClass().getSimpleName();

        StringBuilder details = new StringBuilder();
        details.append(String.format("Coupe %d: Type = %s, Nom de l'Outil = %s, Largeur = %.2fmm, Depart = (%.2f, %.2f), Arrivee = (%.2f, %.2f), Reference = %s",
                numero, typeCoupe, outil.getNom(), outil.getLargeur(),
                depart.getX(), depart.getY(), arrive.getX(), arrive.getY(), coupe.getReference()));

        if (coupe instanceof CoupeEnL) {
            CoupeEnL coupeEnL = (CoupeEnL) coupe;
            String b1Ref = (coupeEnL.getBordure() instanceof Coupe) ? ((Coupe) coupeEnL.getBordure()).getReference() : coupeEnL.getBordure().toString();
            String b2Ref = (coupeEnL.getB2() instanceof Coupe) ? ((Coupe) coupeEnL.getB2()).getReference() : coupeEnL.getB2().toString();
            details.append(String.format(", ID = %s, B1 = %s, B2 = %s, Ref1 = %s, Ref2 = %s, D1 = %.2f, D2 = %.2f, D3 = %.2f, D4 = %.2f, EdgeRef = %s",
                    coupeEnL.getId(), b1Ref, b2Ref,
                    coupeEnL.getTypeDeReference(), coupeEnL.getRef2(),
                    coupeEnL.getDistanceFromReference(), coupeEnL.getD2(),
                    coupeEnL.getD3(), coupeEnL.getD4(),
                    coupeEnL.getEdgeType()));
        } else if (coupe instanceof CoupeRectangulaire) {
            CoupeRectangulaire coupeRect = (CoupeRectangulaire) coupe;

            // Main rectangle info
            details.append(String.format("Coupe %d: Type = %s, Nom de l'Outil = %s, Largeur = %.2fmm, Depart = (%.2f, %.2f), Arrivee = (%.2f, %.2f), Reference = %s",
                    numero, coupeRect.getClass().getSimpleName(),
                    coupeRect.getOutil().getNom(),
                    coupeRect.getOutil().getLargeur(),
                    coupeRect.getDepart().getX(), coupeRect.getDepart().getY(),
                    coupeRect.getArrive().getX(), coupeRect.getArrive().getY(),
                    coupeRect.getReference()));
            String b1Ref = (coupeRect.getBordure() instanceof Coupe)
                    ? ((Coupe) coupeRect.getBordure()).getReference()
                    : coupeRect.getBordure().toString();
            String b2Ref = (coupeRect.getB2() instanceof Coupe)
                    ? ((Coupe) coupeRect.getB2()).getReference()
                    : coupeRect.getB2().toString();
            details.append(String.format(", ID = %s, B1 = %s, B2 = %s, Ref1 = %s, Ref2 = %s, D1 = %.2f, D2 = %.2f, D3 = %.2f, D4 = %.2f, EdgeRef = %s",
                    coupeRect.getId(), b1Ref, b2Ref,
                    coupeRect.getTypeDeReference(), coupeRect.getRef2(),
                    coupeRect.getDistanceFromReference(), coupeRect.getD2(),
                    coupeRect.getD3(), coupeRect.getD4(),
                    coupeRect.getEdgeType()));
            details.append("\n").append(genererDetailCoupeBase(numero + ".1", coupeRect.getCoupeVerticaleDepart()));
            details.append(genererDetailCoupeBase(numero + ".2", coupeRect.getCoupeVerticaleArrive()));
            details.append(genererDetailCoupeBase(numero + ".3", coupeRect.getCoupeHorizontaleDepart()));
            details.append(genererDetailCoupeBase(numero + ".4", coupeRect.getCoupeHorizontaleArrive()));
        } else if (coupe instanceof CoupeHorizontale) {
            CoupeHorizontale coupeHorizontale = (CoupeHorizontale) coupe;
            details.append(String.format(", ID = %s, Bordure = %s, Ref = %s, Distance = %.2f, EdgeRef = %s",
                    coupeHorizontale.getId(), coupeHorizontale.getBordure(),
                    coupeHorizontale.getTypeDeReference(),
                    coupeHorizontale.getDistanceFromReference(),
                    coupeHorizontale.getEdgeReference()));
        } else if (coupe instanceof CoupeVerticale) {
            CoupeVerticale coupeVerticale = (CoupeVerticale) coupe;
            details.append(String.format(", ID = %s, Bordure = %s, Ref = %s, Distance = %.2f, EdgeRef = %s",
                    coupeVerticale.getId(), coupeVerticale.getBordure(),
                    coupeVerticale.getTypeDeReference(),
                    coupeVerticale.getDistanceFromReference(),
                    coupeVerticale.getEdgeReference()));
        }

        details.append("\n");
        return details.toString();
    }

    private String genererDetailCoupeBase(String numero, Coupe coupe) {
        Point3D depart = coupe.getDepart();
        Point3D arrive = coupe.getArrive();
        Outil outil = coupe.getOutil();
        String typeCoupe = coupe.getClass().getSimpleName();

        return String.format("Coupe %s: Type = %s, Nom de l'Outil = %s, Largeur = %.2fmm, "
                + "Depart = (%.2f, %.2f), Arrivee = (%.2f, %.2f), Reference = %s, ID = %s, "
                + "Bordure = %s, Ref = %s, Distance = %.2f, EdgeRef = %s\n",
                numero, typeCoupe, outil.getNom(), outil.getLargeur(),
                depart.getX(), depart.getY(), arrive.getX(), arrive.getY(),
                coupe.getReference(), coupe.getId(),
                coupe.getBordure(), coupe.getTypeDeReference(),
                coupe.getDistanceFromReference(),
                (coupe instanceof CoupeVerticale || coupe instanceof CoupeHorizontale)
                        ? ((coupe instanceof CoupeVerticale) ? "LEFT" : "TOP") : "null");
    }
}
