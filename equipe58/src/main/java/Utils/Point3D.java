package Utils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author ADMO-PC
 */
public class Point3D {

    private double x;
    private double y;
    private double z;

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", getX(), getY(), getZ());
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(double x, double y) {
        this(x, y, 0);
    }

    public Point3D() {
        this(0, 0, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
