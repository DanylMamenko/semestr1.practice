package practice06;

class Fraction {
    public int numerator;
    public int denominator;

    public void setValues(int num, int den) {
        if (den == 0) {
            System.out.println("Помилка: знаменник не може дорівнювати 0!");
            den = 1;
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator = num;
        this.denominator = den;
        }


    private int gcd (int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
     }

    public void simplify(){
         int num = gcd (numerator, denominator);
         numerator /= num;
         denominator /= num;
     }

    public double toDecimal(){
        return (double) numerator / denominator;
    }

    public void printFraction() {
        if (denominator == 1) {
            System.out.print(numerator);
        } else {
            System.out.print(numerator + "/" + denominator);
        }
    }

    public void add (int otherNum, int otherDen){
        if (otherDen == 0 ){
            System.out.println("Помилка: знаменник не може дорівнювати 0!");
            return;
        }
        int oldNum = numerator;
        int oldDen = denominator;

        System.out.println();
        System.out.println("Додаємо дріб " + otherNum + "/" + otherDen + " до " + oldNum + "/" + oldDen + "...");

        numerator = oldNum * otherDen + otherNum * oldDen;
        denominator = oldDen * otherDen;
        simplify();

        System.out.println("Формула: " + oldNum + "/" + oldDen + " + " + otherNum + "/"
                        + otherDen + "= (" + oldNum + "*" + otherDen + " + " + otherNum + "*" + oldDen
                        + ") / (" + oldDen + "*" + otherDen + ") = " + numerator + "/" + denominator );
    }
}
public class Task1Fraction {
    static void main(String[] args) {
        System.out.println("=== КАЛЬКУЛЯТОР ЗВИЧАЙНИХ ДРОБІВ ===");
        Fraction fraction = new Fraction();
        fraction.setValues(8, 12);
        System.out.print("Дріб 1: ");
        fraction.printFraction();

        System.out.println("");

        fraction.simplify();
        System.out.print("Після скорочення: ");
        fraction.printFraction();

        System.out.printf(" (десятковий вигляд: %.4f)%n", fraction.toDecimal());
        fraction.add(3,4);
        System.out.print("Результат додавання: ");
        fraction.printFraction();
        System.out.printf(" (десятковий вигляд: %.4f)%n", fraction.toDecimal());
        System.out.println("=====================================");


    }
}
