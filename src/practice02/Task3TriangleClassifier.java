package practice02;
import java.util.Scanner;
public class Task3TriangleClassifier {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть сторони трикутника a, b, c");
        double b = scanner.nextDouble();
        double a = scanner.nextDouble();
        double c = scanner.nextDouble();
        System.out.println("Сторони трикутника: a= " + a + " b= " + b + " c= " + c);
        boolean exists = (a + b > c) && (a + c > b) && (b + c > a);
        boolean equalSides = (a == b) && (a == c) && (b == c);
        boolean equifemoral = (a == b) || (a == c) || (b == c);
        double a2 = Math.pow(a, 2);
        double b2 = Math.pow(b, 2);
        double c2 = Math.pow(c, 2);
        boolean rectangular = (c2 == a2 + b2) || (a2 == c2 + b2) || (b2 == c2 + a2);
        System.out.println("--------------------------------------------------");
        System.out.println("Чи існує такий трикутник:\t" + exists);
        System.out.println("Чи є рівностороннім: \t\t" + equalSides);
        System.out.println("Чи є рівнобедреним:\t\t\t" + equifemoral);
        System.out.println("Чи є прямокутним: \t\t\t" + rectangular);
        scanner.close();
    }
}
