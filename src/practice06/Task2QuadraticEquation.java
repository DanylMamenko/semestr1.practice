package practice06;

class QuadraticEquation {
    private double a;
    private double b;
    private double c;

    public void setCoefficients(double a, double b, double c){
        if (a == 0){
            System.out.println("Увага: коефіцієнт 'a' не може дорівнювати 0 (це лінійне рівняння)!");
            a = 1;
        }
        this.a = a;
        this.b = b;
        this.c = c;
        }
    public double getDiscriminant(){
        double b2 = Math.pow(b,2);
        return b2 - (4 * a * c);
    }

    public boolean hasRealRoots(){
        return getDiscriminant() >= 0;
    }

    public double getRoot1(){
        if (!hasRealRoots()) {
            return Double.NaN;
        }
        return (-b + Math.sqrt(getDiscriminant())) / (a * 2);
    }

    public double getRoot2 (){
        if (!hasRealRoots()){
            return Double.NaN;
        }
        return (-b - Math.sqrt(getDiscriminant())) / (a * 2);
    }

    public void printSolutions (){
        if (b < 0){
            System.out.println(a + "*x^2 + " + "(" + b + ")" + "*x + " + c + " = 0");
        } else {                                                                    // Для виведення коеф Б в дужках якщо він від'ємний
        System.out.println(a + "*x^2 + " + b + "*x + " + c + " = 0");
        }
        double d = getDiscriminant();
        System.out.println("Дискримінант D = " + d);
        if (d > 0){
            System.out.println("Рівняння має 2 дійсні корені : " );
            System.out.println("    x1 = " + getRoot1());
            System.out.println("    x2 = " + getRoot2());
        } else if (d == 0){
            System.out.println("Рівняння має один дійсний корінь = ");
            System.out.println("x1 = " + getRoot1());
        } else {
            System.out.println("Рівняння не має дійсних коренів у множині R");
        }
        System.out.println("--------------------------------------");
    }
}

public class Task2QuadraticEquation {
    static void main(String[] args) {
        QuadraticEquation nums = new QuadraticEquation();
        System.out.println("Тест 1:");
        System.out.print("Рівняння: ");
        nums.setCoefficients(1, -5, 6);
        nums.printSolutions();

        System.out.println("Тест 2:");
        System.out.print("Рівняння: ");
        nums.setCoefficients(1,-4,4);
        nums.printSolutions();

        System.out.println("Тест 3:");
        System.out.print("Рівняння: ");
        nums.setCoefficients(1,2,5);
        nums.printSolutions();
    }
}
