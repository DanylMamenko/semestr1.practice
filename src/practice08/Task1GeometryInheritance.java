package practice08;

import java.util.concurrent.CompletableFuture;

class Circle {
    private double radius;

    public Circle (double radius){
        setRadius(radius);
    }

    public void setRadius(double radius){
        if (radius <= 0){
            System.out.println("Попередження: радіус має бути більшим за 0. Встановлено 1.0");
            this.radius = 1;
        } else {
            this.radius = radius;
        }
    }

    public double getRadius() {
        return radius;
    }

    public double getArea(){
        return Math.PI * radius * radius;
    }

    public double getCircumference(){
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString(){
        return String.format("Circle[r=%.2f, S=%.2f, C=%.2f]",
                radius, getArea(), getCircumference());
    }
}

class Cylinder extends Circle {
    private double height;

    public Cylinder(double radius, double height) {
        super(radius);
        setHeight(height);
    }

    public void setHeight(double height) {
        if (height <= 0) {
            System.out.println("Попередження: радіус має бути більшим за 0. Встановлено 1.0");
            this.height = 1;
        } else {
            this.height = height;
        }
    }

    public double getVolume() {
        return super.getArea() * height;
    }

    public double getTotalSurfaceArea() {
        return 2 * super.getArea() + super.getCircumference() * height;
    }

    @Override
    public String toString() {
        return String.format("Cylinder[base=%s], h=%.2f, V=%.2f, TotalArea=%.2f", super.toString(),
                height, getVolume(), getTotalSurfaceArea());
    }
}
class Cone extends Circle {
        private double height;

        public Cone(double height, double radius) {
            super(radius);
            setHeight(height);
        }

        public void setHeight(double height){
            if (height <= 0){
                System.out.println("Попередження: радіус має бути більшим за 0. Встановлено 1.0");
                this.height = 1;
            } else {
                this.height = height;
            }
        }

        public double getSlantHeight(){
            return Math.sqrt(getRadius() * getRadius()  + height * height);
        }

        public double getVolume(){
            return super.getArea() * height / 3.0;
        }

        public double getTotalSurfaceArea(){
            return super.getArea() + Math.PI * getRadius() * getSlantHeight();
        }

        @Override
        public String toString(){
            return String.format("Cone[base=%s, h=%.2f, l=%.2f, V=%.2f, TotalArea=%.2f]",
                    super.toString(), height, getSlantHeight(), getVolume(), getTotalSurfaceArea());
        }
    }

public class Task1GeometryInheritance {
    static void main(String[] args) {
        Circle circle = new Circle (5.0);
        Cylinder cylinder = new Cylinder(5.0, 10.0);
        Cone cone = new Cone (3.0, 4.0);
        System.out.println("================ ГЕОМЕТРІЯ ТІЛ ОБЕРТАННЯ ================");
        System.out.println("Базове коло:");
        System.out.println(circle);
        System.out.println();
        System.out.println("Циліндр (успадковано від Circle):");
        System.out.println(cylinder);
        System.out.println();
        System.out.println("Конус (успадковано від Circle):");
        System.out.println(cone);
        System.out.println("=========================================================");

    }
}
