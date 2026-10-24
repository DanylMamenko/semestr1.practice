package practice04;
import java.util.Scanner;

public class Task2DigitAnalyzer {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== АНАЛІЗАТОР РОЗРЯДІВ ЧИСЛА ===");
        System.out.print("Введіть додатне ціле число:" );
        int number = scanner.nextInt();
        scanner.close();
        int sumOfNumber = number;
        int originalNumber = number;
        int maxDigit = 0;
        System.out.println("\n--- СТАТИСТИКА ЧИСЛА ---");
        int allDigits = 0;
        int totalSum = 0;
        int reversedNumber = 0;
        while (number > 0) {
            int lastDigit = number % 10;
            if (lastDigit > maxDigit){
                maxDigit = lastDigit;
            }
            totalSum += lastDigit;
            allDigits += 1;
            reversedNumber = reversedNumber * 10 + lastDigit;
            number /= 10;
        };
        System.out.println("Початкове число:\t" + originalNumber);
        System.out.println("Кількість цифр: \t" + allDigits);
        System.out.println("Сума цифр:\t\t\t" + totalSum);
        System.out.println("Найбільша цифра:  \t" + maxDigit);
        System.out.println("Перевернуте число:\t" + reversedNumber);
        System.out.print("Результат:\t\t\t");
        if (reversedNumber == originalNumber) {
            System.out.println("Це число є паліндромом! ✨");
        } else {
            System.out.println("Це звичайне число (не паліндром).");
        };
    }
}
