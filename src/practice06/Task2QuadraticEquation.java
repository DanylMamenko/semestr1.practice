package practice06;

class QuadraticEquation {
    private double a;
    private double b;
    private double c;

    public void setCoefficients(double a, double b, double c){
        if (a == 0){
            System.out.println("Увага: коефіцієнт 'a' не може дорівнювати 0 (це лінійне рівняння)!");
            a = 1;
        } else {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}

public class Task2QuadraticEquation {
}
