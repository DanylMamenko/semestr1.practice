package practice02;
public class Task2NumberAnalysis {
    static void main() {
        int number = 567;
        int hundreds = number / 100;
        int tens = number / 10 % 10 ;
        int units = number % 10;
        int summa = hundreds + tens + units;
        int product = hundreds * tens * units;
        int reversedNumber = units * 100 + tens * 10 + hundreds;
        boolean isSumEven = summa % 2 == 0;
        System.out.println("Початкове число: \t\t" + number);
        System.out.println("Розряди:" + "\t\t\t\tСотні= " + hundreds + ", Десятки = " + tens + ", Одиниці = " + units);
        System.out.println("Сума цифр: \t\t\t\t" + summa);
        System.out.println("Добуток цифр: \t\t\t" + product);
        System.out.println("Перевернуте число: \t\t" + reversedNumber );
        System.out.println("Чи є сума цифр парною?\t" + isSumEven);
    }
}

