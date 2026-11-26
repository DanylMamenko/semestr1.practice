package practice12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

abstract class Payment {
    private final String id;
    private final String shopName;
    private final double amount;

    public Payment(String id, String shopName, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сума платежу має бути більшою за 0");
        }
        this.amount = amount;
        this.id = id;
        this.shopName = shopName;
    }

    public abstract double calculateFee();

    public double getNetAmount() {
        return amount - calculateFee();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Payment){
            Payment other = (Payment) o;
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    public double getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getShopName() {
        return shopName;
    }
}

class CardPayment extends Payment {

    public CardPayment(String id, String shopName, double amount) {
        super(id, shopName, amount);
    }

    @Override
    public double calculateFee() {
        return getAmount() * 0.015;
    }
}

class TransferPayment extends Payment {

    public TransferPayment(String id, String shopName, double amount) {
        super(id, shopName, amount);
    }

    @Override
    public double calculateFee() {
        return getAmount() * 0.02 + 20.0;
    }
}

class PaymentProcessor {
    private ArrayList<Payment> payments = new ArrayList<>();
    private HashSet<String> blockedShops = new HashSet<>();
    private double maxLimit;

    public PaymentProcessor(HashSet<String> blockedShops, double maxLimit){
        this.blockedShops= new HashSet<>(blockedShops);
        this.maxLimit = maxLimit;
    }

    public HashSet<String> getBlockedShopsCopy() {
        return new HashSet<>(blockedShops);
    }

    public ArrayList<Payment> getPaymentsCopy(){
        return new ArrayList<>(payments);
    }

    public void addPayment(Payment p){
        payments.add(p);
    }

    public int cleanPayments(){
        int cleanedAmount = 0;
        Iterator<Payment> iterator = payments.iterator();
        HashSet<String> seenIds = new HashSet<>();
        while (iterator.hasNext()){
            Payment p = iterator.next();
            if (!seenIds.add(p.getId())){
                System.out.printf(" ❌[ДУБЛІКАТ]          %s | %s (%.2f грн) - видалено%n", p.getId(),
                        p.getShopName(), p.getAmount());
                iterator.remove();
                cleanedAmount++;
            } else if (blockedShops.contains(p.getShopName())) {
                System.out.printf(" ❌[ЧОРНИЙ СПИСОК]     %s | %s (%.2f грн) - видалено%n", p.getId(),
                        p.getShopName(), p.getAmount());
                iterator.remove();
                cleanedAmount++;
            } else if (p.getAmount() > maxLimit){
                System.out.printf(" ❌[ПОНАД ЛІМІТ]       %s | %s (%.2f грн) - видалено%n", p.getId(),
                        p.getShopName(), p.getAmount());
                iterator.remove();
                cleanedAmount++;
            }
        }
        return cleanedAmount;
    }


    public double getTotalGross(){
        double total = 0;
        for (Payment p : payments){
            total += p.getAmount();
        }
        return total;
    }

    public HashSet<String> getApprovedShops(){
        HashSet<String> shops = new HashSet<>();
        for (Payment p : payments){
            shops.add(p.getShopName());
        }
        return shops;
    }

    public double getTotalFees(){
        double totalFees = 0;
        for (Payment p : payments){
            totalFees += p.calculateFee();
        }
        return totalFees;
    }
}

public class Task2PaymentFilterAndAudit {
    static void main(String[] args) {
        HashSet<String> blocked = new HashSet<>();
        blocked.add("ScamShop");
        blocked.add("FakeStore");
        double limit = 20000.0;

        PaymentProcessor processor = new PaymentProcessor(blocked, limit);
        processor.addPayment(new CardPayment("P-01", "Rozetka", 4000.0));
        processor.addPayment(new TransferPayment("P-02", "Comfy", 10000.0));
        processor.addPayment(new CardPayment("P-01", "Rozetka", 4000.0));
        processor.addPayment(new CardPayment("P-03", "ScamShop", 5000.0));
        processor.addPayment(new TransferPayment("P-04", "LuxWatch", 25000.0));
        processor.addPayment(new CardPayment("P-05", "Rozetka", 2000.0));
        processor.addPayment(new TransferPayment("P-06", "Allo", 5000.0));

        System.out.println("================ 🛡️ БАНКІВСЬКИЙ ПЛАТІЖНИЙ ФІЛЬТР 🛡️ ================");
        System.out.printf("Чорний список магазинів [%d]: [%s]%n", blocked.size(), processor.getBlockedShopsCopy());
        System.out.printf("Ліміт одного платежу: %.2f грн | Вхідних платежів: %d%n", limit, processor.getPaymentsCopy().size());
        System.out.println("");
        System.out.println("--- Очищення черги через Iterator.remove() ---");
        int removed = processor.cleanPayments();
        System.out.println("");
        System.out.println("--- Схвалені платежі (залишилось 4) ---");
        for (Payment p : processor.getPaymentsCopy()){
            System.out.printf("✅ %s | %s | Сума: %.2f | Комісія: %.2f | Нетто: %.2f грн%n", p.getId(), p.getShopName(),p.getAmount(),p.calculateFee(), p.getNetAmount());
        }
        double totalGross = processor.getTotalGross();
        double totalFees = processor.getTotalFees();
        System.out.println("");
        System.out.println("--- Фінансовий підсумок ---");
        System.out.printf("Видалено платежів: %d | Схвалено: %d%n", removed, processor.getPaymentsCopy().size());
        System.out.printf("Загальна сума (брутто): %.2f грн%n", totalGross);
        System.out.printf("Загальна комісія:       %.2f грн%n",totalFees);
        System.out.printf("Чиста сума (нетто):     %.2f грн%n", totalGross-totalFees);
        System.out.printf("Унікальні магазини [%d]: %s%n", processor.getBlockedShopsCopy().size(), processor.getApprovedShops());
        System.out.println("====================================================================");
    }
}
