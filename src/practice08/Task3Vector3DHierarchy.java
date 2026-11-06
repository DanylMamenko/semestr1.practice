package practice08;

import jdk.swing.interop.SwingInterOpUtils;

class Vector2D{
    private double x;
    private double y;

    public Vector2D(double x, double y){
        this.x=x;
        this.y=y;
    }

    public Vector2D(){
        this(0, 0);
    }

    public void setX(double x){
        this.x=x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength(){
        return Math.sqrt(x * x + y * y);
    }

    public double dotProduct(Vector2D other){
        return x * other.x + y * other.y;
    }

    @Override
    public String toString(){
        return String.format("Vector2D(%.2f; %.2f) |L|=%.2f", x, y, getLength());
    }
}

class Vector3D extends Vector2D{
    private double z;

    public Vector3D(double x, double y, double z){
        super(x, y);
        this.z = z;
    }

    public void setZ(double z){
        this.z=z;
    }

    public double getZ() {
        return z;
    }

    @Override
    public double getLength(){
        return Math.hypot(super.getLength(), z);
    }

    public double dotProduct(Vector3D other){
        return super.dotProduct(other) + this.z * other.z;
    }

    public Vector3D crossProduct(Vector3D other){
        double newX = getY() * other.z - getZ() * other.getY();
        double newY = getZ() * other.getX() - getX() * other.z;
        double newZ = getX() * other.getY() - getY() * other.getX();
        return new Vector3D(newX, newY, newZ);
    }

    @Override
    public String toString(){
        return String.format("Vector3D(%.2f; %.2f, %.2f) |L|=%.2f", getX(), getY(), z,  getLength());
    }
}

class ForceVector3D extends Vector3D{
    private String forceName;

    public ForceVector3D(String forceName, double fx, double fy, double fz){
        super(fx,fy,fz);
        this.forceName = forceName;
    }

    public double calculateWork(Vector3D displacement){
        return super.dotProduct(displacement);
    }

    public double calculateAcceleration(double massKg){
        if (massKg <= 0) {
            return 0.0;
        }
        return getLength() / massKg;
    }

    @Override
    public String toString(){
        return String.format("Force[name='%s' %s] ", forceName, super.toString().replace("|L|", "|F|"));
    }
}

public class Task3Vector3DHierarchy {
    static void main(String[] args) {
        Vector2D V2 = new Vector2D(3.0, 4.0);
        Vector3D V3 = new Vector3D(3.0, 4.0, 12.0);
        Vector3D displacement = new Vector3D(2.0, 0.0, 5.0);
        ForceVector3D FV3 = new ForceVector3D("Тяга ракети", 3.0, 4.0, 12.0);
        double work = FV3.calculateWork(displacement);
        double mass = 2.5;
        double acceleration = FV3.calculateAcceleration(mass);

        System.out.println("================ ІЄРАРХІЯ ВЕКТОРІВ (2D -> 3D -> FORCE) ================");
        System.out.println("1. Рівень 1 (2D): " + V2);
        System.out.println("2. Рівень 2 (3D):" + V3);
        System.out.println("  Вектор переміщення S:" + displacement);
        System.out.println("");
        System.out.println("3. Рівень 3 (Фізична сила):");
        System.out.println(" " + FV3);
        System.out.println(" -> Виконана механічна робота A = F · S = " + work + "Дж");
        System.out.println("  -> Прискорення тіла масою 2.5 кг: a = " + acceleration + " м/с²");
        System.out.println("=======================================================================");


    }
}
