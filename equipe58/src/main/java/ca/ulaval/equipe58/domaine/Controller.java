package ca.ulaval.equipe58.domaine;

import Utils.Point3D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {

    private Cnc cnc;

    private Stack<List<Coupe>> undoStack = new Stack<>();
    private Stack<List<Coupe>> redoStack = new Stack<>();

    public Controller(Cnc cnc) {
        this.cnc = cnc;
    }
    
    public Coupe getSelectedCoupe2(Point3D Point3D, String typeDeCoupe) {
        return cnc.getSelectedCoupe2(Point3D, typeDeCoupe);
    }
    
    public Controller() {
        cnc = new Cnc();
        cnc.ajouterOutil("Outils par default", 10.5);
    }

    public void resetPanneau() {
        cnc.resetPanneau();
    }

    public void redimensionnerPanneau(double x, double y, double z) {
        cnc.redimensionnerPanneau(x, y, z);
    }

    public List<OutilDTO> obtenirListeOutils() {
        return cnc.getOutils().stream()
                .map(OutilDTO::fromOutil)
                .collect(Collectors.toList());
    }

    public Coupe getSelectedCoupe() {
        return cnc.getCoupes().stream()
                .filter(Coupe::isSelected)
                .findFirst()
                .orElse(null);
    }

    public boolean isReferencePointValid(Point3D Point3D, String typeDeCoupe) {
        return cnc.isReferencePointValid(Point3D, typeDeCoupe);
    }

    public void creerCoupe(String typeDeCoupe, Point3D Point3DReference, Point3D Point3DDeCoupe) {
        saveState();
        Outil outil = cnc.getOutilActuel();
        cnc.creerCoupe(typeDeCoupe, Point3DReference, Point3DDeCoupe, outil);
    }

    public void supprimerCoupe() {
        saveState();
        cnc.supprimerCoupe();
    }

    public void supprimerCoupeBordure() {
        cnc.supprimerCoupeBordure();
    }

    public void changerOutilDeLaCoupeSelectionnee(String nomOutil) {
        Coupe coupeSelectionnee = getSelectedCoupe();
        if (coupeSelectionnee != null) {
            Outil nouvelOutil = cnc.getOutils().stream()
                    .filter(outil -> outil.getNom().equals(nomOutil))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Outil introuvable : " + nomOutil));

            coupeSelectionnee.setOutil(nouvelOutil);
        }
    }
    public Coupe switchSelectionStatus2(double x, double y) {
        return cnc.switchSelectionStatus2(x, y);
    }
    public void ExportGcode(double vitesse, double spin) {
        cnc.ExportGcode(vitesse, spin);
    }

    public Outil getOutilsActuel() {
        return this.cnc.getOutilActuel();
    }

    public OutilDTO getOutilsDTO() {
        return OutilDTO.fromOutil(cnc.getOutilActuel());
    }

    public void definirOutilActuel(String nomOutil) {
        cnc.setOutilActuel(nomOutil);
    }

    public void sauvegarderCnc(String filepath) {
        CncFile cncFile = new CncFile(cnc.getPanneau().getListeCoupe(), cnc.getPanneau(), cnc);
        cncFile.genererCNCFile(filepath);
    }

    public void importerCnc(String filePath) throws IOException {
        List<Coupe> coupes = new ArrayList<>();
        Panneau panneau = null;
        Outil outilActuel = null;

        String panneauPattern = "DIMENSIONS Panneau: Depart = \\(([^,]+), ([^,]+), ([^,]+)\\), Arrive = \\(([^,]+), ([^,]+), ([^,]+)\\), Profondeur = ([^,]+)";
        String outilPattern = "Tool \\d+: Nom = ([^,]+), Largeur = ([^,]+)mm";
        String coupePattern = "Coupe (\\d+(?:\\.\\d+)?): Type = ([^,]+), Nom de l'Outil = ([^,]+), Largeur = ([^,]+)mm, Depart = \\(([^,]+), ([^,]+)\\), Arrivee = \\(([^,]+), ([^,]+)\\)"
                + ", Reference = ([^,]+)"
                + ", ID = ([^,]+)"
                + "(, Bordure = ([^,]+))?"
                + "(, Ref = ([^,]+))?"
                + "(, Distance = ([^,]+))?"
                + "(, EdgeRef = ([^,]+))?"
                + "(, B1 = ([^,]+))?"
                + "(, B2 = ([^,]+))?"
                + "(, Ref1 = ([^,]+))?"
                + "(, Ref2 = ([^,]+))?"
                + "(, D1 = ([^,]+))?"
                + "(, D2 = ([^,]+))?"
                + "(, D3 = ([^,]+))?"
                + "(, D4 = ([^,]+))?";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                System.out.println("Ligne: " + line);
                // Ignorer les commentaires
                if (line.startsWith(";")) {
                    continue;
                }

                // Traitement des dimensions du panneau
                Matcher panneauMatcher = Pattern.compile(panneauPattern).matcher(line);
                if (panneauMatcher.find()) {
                    double departX = parseDoubleSafe(panneauMatcher.group(1));
                    double departY = parseDoubleSafe(panneauMatcher.group(2));
                    double departZ = parseDoubleSafe(panneauMatcher.group(3));

                    double arriveX = parseDoubleSafe(panneauMatcher.group(4));
                    double arriveY = parseDoubleSafe(panneauMatcher.group(5));
                    double arriveZ = parseDoubleSafe(panneauMatcher.group(6));

                    double profondeur = parseDoubleSafe(panneauMatcher.group(7));

                    Point3D departPanneau = new Point3D(departX, departY, departZ);
                    Point3D arrivePanneau = new Point3D(arriveX, arriveY, arriveZ);
                    Point3D profondeurPanneau = new Point3D(0, 0, profondeur);

                    panneau = new Panneau(departPanneau, arrivePanneau, profondeurPanneau);
                    continue;
                }

                // Traitement des outils
                Matcher outilMatcher = Pattern.compile(outilPattern).matcher(line);
                if (outilMatcher.find()) {
                    String nomOutil = outilMatcher.group(1).trim();
                    double largeurOutil = parseDoubleSafe(outilMatcher.group(2).trim());
                    outilActuel = new Outil(nomOutil, largeurOutil);
                    continue;
                }

                Matcher coupeMatcher = Pattern.compile(coupePattern).matcher(line);
                if (coupeMatcher.find()) {
                    String coupeNumber = coupeMatcher.group(1);
                    if (coupeNumber.contains(".")) {
                        continue;
                    }

                    String typeCoupe = coupeMatcher.group(2).trim();
                    String nomOutil = coupeMatcher.group(3).trim();
                    double largeurOutil = parseDoubleSafe(coupeMatcher.group(4).trim());
                    double departX = parseDoubleSafe(coupeMatcher.group(5));
                    double departY = parseDoubleSafe(coupeMatcher.group(6));
                    double arriveX = parseDoubleSafe(coupeMatcher.group(7));
                    double arriveY = parseDoubleSafe(coupeMatcher.group(8));

                    String reference = coupeMatcher.group(9).trim();
                    UUID id = UUID.fromString(coupeMatcher.group(10).trim());
                    String bordure = coupeMatcher.group(12);
                    ReferenceType refType = coupeMatcher.group(14) != null && !coupeMatcher.group(14).trim().equals("null")
                            ? ReferenceType.valueOf(coupeMatcher.group(14))
                            : ReferenceType.EDGE;

                    double distance = coupeMatcher.group(16) != null
                            ? parseDoubleSafe(coupeMatcher.group(16)) : 0.0;

                    EdgeType edgeRef = coupeMatcher.group(18) != null
                            ? EdgeType.valueOf(coupeMatcher.group(18)) : null;

                    Object b1 = parseObject(coupeMatcher.group(20), coupes);
                    Object b2 = parseObject(coupeMatcher.group(22), coupes);

                    ReferenceType ref1 = coupeMatcher.group(24) != null && !coupeMatcher.group(24).trim().equals("null")
                            ? ReferenceType.valueOf(coupeMatcher.group(24))
                            : ReferenceType.EDGE;

                    ReferenceType ref2 = coupeMatcher.group(26) != null && !coupeMatcher.group(26).trim().equals("null")
                            ? ReferenceType.valueOf(coupeMatcher.group(26))
                            : null;

                    double d1 = coupeMatcher.group(28) != null
                            ? parseDoubleSafe(coupeMatcher.group(28)) : 0.0;
                    double d2 = coupeMatcher.group(30) != null
                            ? parseDoubleSafe(coupeMatcher.group(30)) : 0.0;
                    double d3 = coupeMatcher.group(32) != null
                            ? parseDoubleSafe(coupeMatcher.group(32)) : 0.0;
                    double d4 = coupeMatcher.group(34) != null
                            ? parseDoubleSafe(coupeMatcher.group(34)) : 0.0;

                    Point3D depart = new Point3D(departX, departY, 0);
                    Point3D arrive = new Point3D(arriveX, arriveY, 0);

                    // Création des coupes selon le type
                    switch (typeCoupe) {
                        case "CoupeVerticale":
                            CoupeVerticale coupeVerticale = new CoupeVerticale(
                                    id,
                                    bordure,
                                    refType,
                                    distance,
                                    outilActuel,
                                    panneau,
                                    edgeRef
                            );
                            coupeVerticale.setReference(reference);
                            coupes.add(coupeVerticale);
                            break;

                        case "CoupeHorizontale":
                            CoupeHorizontale coupeHorizontale = new CoupeHorizontale(
                                    id,
                                    bordure,
                                    refType,
                                    distance,
                                    outilActuel,
                                    panneau,
                                    edgeRef
                            );
                            coupeHorizontale.setReference(reference);
                            coupes.add(coupeHorizontale);
                            break;

                        case "CoupeEnL":
                            Object refB1 = parseReferenceForCoupeEnL(b1, coupes, true);
                            Object refB2 = parseReferenceForCoupeEnL(b2, coupes, false);
                            System.out.println("Debug coordinates:");
                            System.out.println("Start point: (" + departX + "," + departY + ")");
                            System.out.println("End point: (" + arriveX + "," + arriveY + ")");
                            System.out.println("D values: d1=" + d1 + " d2=" + d2 + " d3=" + d3 + " d4=" + d4);
                            CoupeEnL coupeEnL = new CoupeEnL(
                                    id,
                                    refB1,
                                    refB2,
                                    refB1 instanceof Coupe ? ReferenceType.COUPE : ReferenceType.EDGE,
                                    refB2 instanceof Coupe ? ReferenceType.COUPE : ReferenceType.EDGE,
                                    departX, // Start X from regex
                                    departY, // Start Y from regex
                                    arriveX - departX, // Width as difference
                                    arriveY - departY, // Height as difference
                                    outilActuel,
                                    panneau,
                                    refB1 instanceof EdgeType ? (EdgeType) refB1 : EdgeType.LEFT
                            );

                            coupeEnL.setReference(reference);
                            coupes.add(coupeEnL);
                            break;
                        case "CoupeRectangulaire":
                            CoupeRectangulaire coupeRectangulaire = new CoupeRectangulaire(
                                    id,
                                    EdgeType.LEFT,
                                    EdgeType.TOP,
                                    ReferenceType.EDGE,
                                    ReferenceType.EDGE,
                                    departX,
                                    departY,
                                    arriveX,
                                    arriveY,
                                    outilActuel,
                                    panneau,
                                    EdgeType.LEFT
                            );

                            coupeRectangulaire.setReference(reference);
                            coupes.add(coupeRectangulaire);
                            break;
                    }
                }
            }

            if (panneau != null) {
                panneau.setListeCoupe(coupes);
                cnc.setPanneau(panneau);
                verifyAndLogCoupes(coupes);

            }

        } catch (IOException e) {
            System.err.println("Erreur lors de l'importation du fichier CNC : " + e.getMessage());
            throw e;
        }
    }

    private Object parseReferenceForCoupeEnL(Object ref, List<Coupe> coupes, boolean isVertical) {
        System.out.println("Parsing reference: " + ref + " (isVertical: " + isVertical + ")");

        if (ref instanceof String) {
            String refStr = ((String) ref).trim();
            System.out.println("Processing string reference: " + refStr);

            try {
                EdgeType edge = EdgeType.valueOf(refStr);
                System.out.println("Found EdgeType: " + edge);

                if (isVertical && (edge == EdgeType.LEFT || edge == EdgeType.RIGHT)) {
                    return edge;
                } else if (!isVertical && (edge == EdgeType.TOP || edge == EdgeType.BOTTOM)) {
                    return edge;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Not an EdgeType, checking for Coupe reference");

                Optional<Coupe> existingCoupe = coupes.stream()
                        .filter(c -> c.getReference().equals(refStr))
                        .findFirst();

                if (existingCoupe.isPresent()) {
                    Coupe coupe = existingCoupe.get();
                    System.out.println("Found Coupe reference: " + coupe.getClass().getSimpleName());

                    if (isVertical && coupe instanceof CoupeVerticale) {
                        return coupe;
                    } else if (!isVertical && coupe instanceof CoupeHorizontale) {
                        return coupe;
                    }
                }
            }
        }

        // Return appropriate default
        EdgeType defaultEdge = isVertical ? EdgeType.LEFT : EdgeType.TOP;
        System.out.println("Using default edge: " + defaultEdge);
        return defaultEdge;
    }

    private void verifyAndLogCoupes(List<Coupe> coupes) {
        System.out.println("\n=== DETAILED COUPES LIST ===");
        for (int i = 0; i < coupes.size(); i++) {
            Coupe coupe = coupes.get(i);
            System.out.println(String.format("\nCoupe %d:", i + 1));
            System.out.println(String.format("Type: %s", coupe.getClass().getSimpleName()));
            System.out.println(String.format("Reference: %s", coupe.getReference()));
            System.out.println(String.format("Depart: %s", coupe.getDepart()));
            System.out.println(String.format("Arrive: %s", coupe.getArrive()));

            if (coupe instanceof CoupeRectangulaire) {
                CoupeRectangulaire rect = (CoupeRectangulaire) coupe;
                System.out.println("Sub-cuts:");
                System.out.println(String.format("  Vertical Start (%s): %s -> %s",
                        rect.getCoupeVerticaleDepart().getReference(),
                        rect.getCoupeVerticaleDepart().getDepart(),
                        rect.getCoupeVerticaleDepart().getArrive()));
                System.out.println(String.format("  Vertical End (%s): %s -> %s",
                        rect.getCoupeVerticaleArrive().getReference(),
                        rect.getCoupeVerticaleArrive().getDepart(),
                        rect.getCoupeVerticaleArrive().getArrive()));
                System.out.println(String.format("  Horizontal Start (%s): %s -> %s",
                        rect.getCoupeHorizontaleDepart().getReference(),
                        rect.getCoupeHorizontaleDepart().getDepart(),
                        rect.getCoupeHorizontaleDepart().getArrive()));
                System.out.println(String.format("  Horizontal End (%s): %s -> %s",
                        rect.getCoupeHorizontaleArrive().getReference(),
                        rect.getCoupeHorizontaleArrive().getDepart(),
                        rect.getCoupeHorizontaleArrive().getArrive()));
            }
        }
        System.out.println("\n==========================");
    }

    private Coupe parseObject(String objectStr, List<Coupe> coupes) {
        if (objectStr == null || objectStr.trim().isEmpty()) {
            return null;
        }

        if (objectStr.contains("CoupeVerticale")
                || objectStr.contains("CoupeHorizontale")
                || objectStr.contains("CoupeEnL")
                || objectStr.contains("CoupeRectangulaire")) {

            String[] parts = objectStr.split("@");
            if (parts.length > 1) {
                String hashCode = parts[1].trim();

                for (Coupe coupe : coupes) {
                    if (coupe.toString().contains(hashCode)) {
                        return coupe;
                    }
                }
            }
        }

        return null;
    }

    private static double parseDoubleSafe(String value) {
        try {
            value = value.replace("mm", "").trim();
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion pour la valeur : " + value);
            return 0.0;
        }
    }

    public void ajouterOutil(String nom, double largeur) {
        cnc.ajouterOutil(nom, largeur);
    }

    public void supprimerOutil(String nom) {
        cnc.supprimerOutil(nom);
    }

    public void ajouterZoneInterdite(Point3D departZI, Point3D arriveZI) {
        this.cnc.ajouterZoneInterdite(departZI, arriveZI);
    }

    public void supprimerZoneInterdite(UUID id) {
        this.cnc.supprimerZoneInterdite(id);
    }

    public void modifierZoneInterdite(UUID zoneId, ZoneInterdite nouvelleZone) {
        this.cnc.modifierZoneInterdite(zoneId, nouvelleZone);
    }

    public CncDTO getCncDto() {
        return CncDTO.fromCnc(cnc);
    }

    public Point3D getDepartPanneau() {
        return this.cnc.getDepartPanneau();
    }

    public Point3D getArrivePanneau() {
        return this.cnc.getArrivePanneau();
    }

    public List<Coupe> getCoupes() {
        return this.cnc.getCoupes();
    }

    public void switchSelectionStatus(double x, double y) {
        cnc.switchSelectionStatus(x, y);
    }

    public void updateSelectedItemsPositions(double delta) {
        cnc.updateSelectedItemsPositions(delta);
    }

    public PanneauDTO getPanneauDTO() {
       // System.out.println(cnc.getPanneau().getDepartPanneau().toString());
        return PanneauDTO.fromPanneau(cnc.getPanneau());
    }

    public void modifierOutil(String nomOutil, double nouveauDiametre) {
        cnc.modifierOutil(nomOutil, nouveauDiametre);
    }

    public boolean isReferencePointValidForIrregularCut(Point3D Point) {
        return cnc.isReferencePointValidForIrregularCut(Point);
    }

    public void creerCoupeRectangulaire(Point3D Point3DReference, Point3D firstPoint3DDeCoupe, Point3D secondPoint3DDeCoupe) {
        Outil outil = cnc.getOutilActuel();
        cnc.creerCoupeRectangulaire(Point3DReference, firstPoint3DDeCoupe, secondPoint3DDeCoupe, outil);
    }

    public void creerCoupeEnL(Point3D Point3DReference, Point3D Point3DDeCoupe) {
        Outil outil = cnc.getOutilActuel();
        cnc.creerCoupeEnL(Point3DReference, Point3DDeCoupe, outil);
    }

    public void creerCoupeBordure(double Hauteur, double Largeur) {
        Outil outil = cnc.getOutilActuel();
        cnc.creerCoupeBordure(Hauteur, Largeur, outil);
    }

    public void modifierCoupeBordure(double Hauteur, double Largeur) {
        Outil outil = cnc.getOutilActuel();
        cnc.modifierCoupeBordure(Hauteur, Largeur, outil);
    }

    public void creerNouveauPanneau(double width, double height, double depth) {
        cnc.creerNouveauPanneau(width, height, depth);
    }

public void saveState() {
    List<Coupe> snapshot = cnc.getCoupes().stream()
        .map(coupe -> coupe.clone()) // Chaque clone est une copie indépendante
        .collect(Collectors.toList());
    undoStack.push(snapshot);
    redoStack.clear(); // Réinitialise Redo
}

public void undo() {
    if (!undoStack.isEmpty()) {
        redoStack.push(cnc.getCoupes());
            cnc.getPanneau().setListeCoupe(undoStack.pop());
        }
    }

public void redo() {
    if (!redoStack.isEmpty()) {
        undoStack.push(cnc.getCoupes()); // Sauvegarder l'état actuel dans undo
            cnc.getPanneau().setListeCoupe(redoStack.pop());
        }
    }
}
