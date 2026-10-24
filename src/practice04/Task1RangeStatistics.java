package practice04;
import java.util.Scanner;

public class Task1RangeStatistics {
    static void main() {
        System.out.println("=== АНАЛІЗАТОР ДІАПАЗОНУ ЧИСЕЛ ===");
        Scanner scanner = new Scanner(System.in);
        int number;
        do {
            System.out.print("Введіть число N: ");
            number = scanner.nextInt();
        if (number <= 0) {
            System.out.println("Помилка: N має бути більшим за 0!");
        }
        } while (number <= 0);
        int totalSum = 0;
        int evenSum = 0;
        int evenCount = 0;
        int oddSum = 0;
        int oddCount = 0;
        for (int i = 1; i <= number; i++) {
            totalSum += i;
        if (i % 2 == 0){
            evenSum += i;
            evenCount++;
        } else {
            oddSum += i;
            oddCount++;
        };
        };
        double average = (double)totalSum / number;
        System.out.println("\n--- РЕЗУЛЬТАТИ ДЛЯ ДІАПАЗОНУ [1 .. 10] ---");
        System.out.println("Загальна сума чисел:\t" + totalSum);
        System.out.println("Сума парних чисел:  \t" + evenSum + " (кількість: " + evenCount + ")");
        System.out.println("Сума непарних чисел:\t" + oddSum + " (кількість: " + oddCount + ")");
        System.out.println("Середнє арифметичне:\t" + average);
    }
}
