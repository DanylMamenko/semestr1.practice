package practice03;
import java.util.Scanner;
public class Task2DeliveryService {
    static void main(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ ===");
        System.out.print("Введіть вагу відправлення (кг): ");
        double weight = scanner.nextDouble();
        System.out.print("Введіть відстань транспортування (км): ");
        int distance = scanner.nextInt();
        System.out.print("Оберіть пункт призначення (1 - Відділення, 2 - Поштомат, 3 - Кур'єр): ");
        int deliveryType = scanner.nextInt();
        System.out.print("Чи є у вас карта Premium? (так/ні): ");
        scanner.nextLine();
        String premiumCard = scanner.nextLine();
        scanner.close();
        if (weight > 0 && weight <= 50){
            System.out.println("");
        } else {
            System.out.println("Помилка: вага має бути більше 0 та не більше 50.0 кг");
            return;
        }
        if (distance > 0){
            System.out.println("");
        } else {
            System.out.println("Помилка: дистанція має бути більше 0 км");
            return;
        }

        String name = switch (deliveryType) {
            case 1 -> "Віділення";
            case 2 -> "Поштомат";
            case 3 -> "Кур'єрська доставка до дверей";
            default -> "Невідомий тип доставки";
        };
        System.out.println("------------- НАКЛАДНА ДОСТАВКИ -------------");
        double rate = switch (deliveryType) {
            case 1 -> 50.0;
            case 2 -> {
                if (weight > 15) {
                    System.out.println("Поштомат не приймає габаритні посилки!");
                    yield -1;
                } else {
                    yield 60.0;
                }
            }
            case 3 -> 100.0;
            default -> {
                System.out.println("Невідомий тип доставки!");
                yield -1;
            }
        };
        if (rate < 0) {return;};
        System.out.println("Тип доставки: \t\t\t" + name + "(базовий тариф: " + rate + ")");

        double addCharge = 0;
        if (distance <= 50){
            addCharge = 0.0;
        } else if (distance > 50 && distance <= 200){
            addCharge = 35.0;
        } else if (distance > 200 ) {
            addCharge = 80.0;
        };
        System.out.println("Доплата за відстань: \t" + addCharge + "грн" + "(" + distance + "км)");
        double sumBeforeDiscount = addCharge + rate;
        System.out.println("Сума до знижки: \t\t"+ sumBeforeDiscount + "грн");
        boolean isPremium = premiumCard.equalsIgnoreCase("так");
        double discount = isPremium ? 0.20 : 0.0;
        double totalBill = sumBeforeDiscount - (sumBeforeDiscount * discount);
        double statusName = isPremium ? 1 : 0;
        System.out.print("Статус клієнта: \t\t");
        if (statusName > 0) {
            System.out.println("Premium (-20%)");
        } else {
            System.out.println("Звичайний (-0%)");
        };
        System.out.println("---------------------------------------------");
        System.out.println("РАЗОМ ДО СПЛАТИ: \t\t" + totalBill + "грн");
        System.out.println("=============================================");
            }
        }


