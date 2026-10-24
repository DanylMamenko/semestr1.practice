package practice03;
import java.util.Scanner;
public class Task1FuelCalculator {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== КАЛЬКУЛЯТОР ПОЇЗДКИ ТА ПАЛЬНОГО ===");
        System.out.print("Введіть відстань поїздки (км): ");
        double distance = scanner.nextDouble();
        System.out.print("Введіть середню витрату на 100 км (л): ");
        double fuelConsumption = scanner.nextDouble();
        System.out.print("Оберіть тип пального (1 - А-95, 2 - Дизель, 3 - Газ): ");
        int fuelType = scanner.nextInt();
        scanner.close();
        System.out.println("\n ---------------- ЗВІТ ПОЇЗДКИ ----------------");

        if (distance <= 0 || fuelConsumption <= 0) {
            System.out.println("Помилка!!! Введіть коректні дані відстані та витрат");
            return;
        };

        double pricePerLiter = switch(fuelType) {
            case 1 -> 56.50;
            case 2 -> 52.80;
            case 3 -> 29.40;
            default -> 0;
        };

        String chosedFuel = switch(fuelType){
            case 1 -> "А-95";
            case 2 -> "Дизель";
            case 3 -> "Газ";
            default -> "Помилка";
        };

        System.out.println("Обране пальне:\t    \t\t" + chosedFuel + " (" + pricePerLiter + " грн/л)");

        if (pricePerLiter == 0) {
            System.out.println("Помилка!!! Невідомий тип пального!");
            return;
        };

        double neededFuel = (distance * fuelConsumption) / 100;
        System.out.println("Необхідно пального:\t\t\t" + neededFuel + " л");
        double baseCost = neededFuel * pricePerLiter;
        System.out.println("Базова вартість:\t\t\t" + baseCost + " грн");
        double finalCost = (baseCost >= 2000.0) ? baseCost * 0.95 : baseCost;

        if (baseCost >= 2000) {
            double discount = baseCost * 0.05;
            System.out.println("Знижка АЗС (5%):\t\t\t" + discount + " грн");
        } else {
            double discount = 0;
            System.out.println("Знижка АЗС (5%):\t\t\t" + discount + " грн");
        };
        System.out.println("----------------------------------------------");
        System.out.println("Підсумкова сума до сплати:\t" + finalCost + " грн");
        System.out.println("==============================================");
    }

}
