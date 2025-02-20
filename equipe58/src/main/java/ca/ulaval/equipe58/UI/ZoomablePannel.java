package ca.ulaval.equipe58.UI;

import Utils.Point3D;
import ca.ulaval.equipe58.UI.Drawer.Drawer;
import ca.ulaval.equipe58.domaine.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZoomablePannel extends JPanel implements Serializable{
    private double zoomLevel = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private static final double DPI = 96; 
    private List<Point3D> Point3Ds; 
    private Drawer drawer;
    private Controller c1;
    private boolean afficherGrille = false;
    private Afficheur mainAfficheur;
    private Dimension dimensionFenetre;
    private double hauteurGrille = 50;
    private double largeurGrille = 50;

    

    public ZoomablePannel() {
        setLayout(new BorderLayout());
    }
    public ZoomablePannel(Afficheur mainAfficheur) {
        this.mainAfficheur = mainAfficheur;
        setLayout(new BorderLayout());
        setVisible(true);
        Point3Ds = new ArrayList<>();
        
        addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double factor = (e.getWheelRotation() < 0) ? 1.1 : 1 / 1.1;
                    double oldZoomLevel = zoomLevel;
                    zoomLevel *= factor;
                    //System.out.println("Zoom level: " + zoomLevel);

                    double mouseX = e.getX();
                    double mouseY = e.getY();

                    double px = (mouseX - offsetX) / oldZoomLevel;
                    double py = (mouseY - offsetY) / oldZoomLevel;

                    offsetX = mouseX - px * zoomLevel;
                    offsetY = mouseY - py * zoomLevel;

                    repaint();
                }
            });

            MouseAdapter mouseAdapter = new MouseAdapter() {
                private Point lastDragPoint;

                @Override
                public void mousePressed(MouseEvent e) {
                    lastDragPoint = e.getPoint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if ((e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0) {
                        double dx = e.getX() - lastDragPoint.x;
                        double dy = e.getY() - lastDragPoint.y;

                        offsetX += dx;
                        offsetY += dy;

                        lastDragPoint = e.getPoint();
                        repaint();
                    }
                }

                @Override
            public void mouseMoved(MouseEvent e) {
                double mouseX = e.getX();
                double mouseY = e.getY();

                double adjustedX = (mouseX - offsetX) / zoomLevel;
                double adjustedY = (mouseY - offsetY) / zoomLevel;

                double xMm = convertPxToMm(adjustedX);
                double yMm = convertPxToMm(adjustedY);

                if (mainAfficheur != null) {
                    mainAfficheur.updateCoordinateDisplay(xMm, yMm);
                }
            }
        };

            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

private void updateMouseCoordinates(MouseEvent e) {
    double mouseX = e.getX();
    double mouseY = e.getY();
    
    double adjustedX = (mouseX - offsetX) / zoomLevel;
    double adjustedY = (mouseY - offsetY) / zoomLevel;
    
    double xMm = convertPxToMm(adjustedX);
    double yMm = convertPxToMm(adjustedY);
    
    double panelWidth = mainAfficheur.controller.getArrivePanneau().getX();
    double panelHeight = mainAfficheur.controller.getArrivePanneau().getY();
    
    if (xMm < 0 || yMm < 0 || xMm > panelWidth || yMm > panelHeight) {
        xMm = 0.0;
        yMm = 0.0;
    }
    
    if (mainAfficheur != null) {
                mainAfficheur.actualMousePoint.setX(xMm);
                mainAfficheur.actualMousePoint.setY(yMm);
                mainAfficheur.updateCoordinateDisplay(xMm, yMm);
            }
}



        private double convertPxToMm(double pixels) {
            return (pixels / DPI) * 25.4;
        }
    
    public double getOffsetX() {
        return offsetX;
    }
    

    public double getOffsetY() {
        return offsetY;
    }
    
    private double ajustCoords(double a, double offset, double zoom) {
        return (a - offset) / zoom;
    }
    
    public double getZoomLevel() {
        return zoomLevel;
    }
    

    
public void setAfficherGrille(boolean afficherGrille, double hauteurGrille,double largeurGrille) {
    this.afficherGrille = afficherGrille;  
    this.largeurGrille = largeurGrille;
    this.hauteurGrille = hauteurGrille;
}
   

    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (mainAfficheur != null){  
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(offsetX, offsetY);
            g2d.scale(zoomLevel, zoomLevel);
            Drawer mainDrawer = new Drawer(mainAfficheur.controller,dimensionFenetre);

            //System.out.println("Largeufjhgfmk");
            mainDrawer.setAfficherGrille(afficherGrille, largeurGrille, hauteurGrille );
            mainDrawer.draw(g);
        }

    }
    
}