package practice10;

interface CashbackCapable{
    double MAX_CASHBACK_UAH = 500.0;
    double getCashbackRate();
    default double calculateCashback(double baseAmount){
        return Math.min(MAX_CASHBACK_UAH, baseAmount * getCashbackRate());
    }
}

interface Refundable{
    boolean requestRefund(String reason);
    boolean isRefunded();
}

interface BlockchainVerifiable {
    String getTxHash();

    int getConfirmations();

    default boolean isFinalized() {
        return getConfirmations() >= 12;
    }
}

abstract class AbstractPaymentTransaction{
    private final String txId;
    private final double baseAmountUah;
    private boolean processed;

    public AbstractPaymentTransaction(String txId, double baseAmountUah){
        this.processed=false;
        this.txId=txId;
        this.baseAmountUah=baseAmountUah;
    }

    public double getBaseAmountUah() {
        return baseAmountUah;
    }

    public String getTxId() {
        return txId;
    }

    public abstract double calculateFee();
    public abstract String getPaymentChannel();

    public final boolean process(){
        if (baseAmountUah <= 0 || processed == true){
            return false;
        }
        processed = true;
        return true;
    }

    public boolean isProcessed() {
        return processed;
    }

    public final double getTotalCharged(){
        return baseAmountUah + calculateFee();
    }
}

class PremiumCardPayment extends AbstractPaymentTransaction implements CashbackCapable, Refundable{
    private final double cashbackRate = 0.03;
    private boolean refunded;

    public PremiumCardPayment(String txId, double baseAmountUah){
        super(txId,baseAmountUah);
        this.refunded=false;
    }

    @Override
    public String getPaymentChannel(){
        return "Visa Infinite";
    }

    @Override
    public double calculateFee(){
        return getBaseAmountUah()*0.01;
    }

    @Override
    public double getCashbackRate(){
        return cashbackRate;
    }

    @Override
    public boolean requestRefund(String reason){
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded(){
        return refunded;
    }
}

class CryptoUsdtPayment extends AbstractPaymentTransaction implements CashbackCapable, BlockchainVerifiable{
    private final String txHash;
    private final int confirmations;
    public CryptoUsdtPayment(String txId, double baseAmountUah, String txHash, int confirmations){
        super(txId,baseAmountUah);
        this.txHash=txHash;
        this.confirmations=confirmations;
    }

    @Override
    public String getPaymentChannel(){
        return "USDT TRC-20";
    }

    @Override
    public double calculateFee(){
        return 40.0;
    }

    @Override
    public double getCashbackRate() {
        return 0.01;
    }

    @Override
    public String getTxHash(){
        return txHash;
    }

    @Override
    public int getConfirmations(){
        return confirmations;
    }
}

final class TaxTreasuryPayment extends AbstractPaymentTransaction implements Refundable{
    private final String taxCode;
    private boolean refunded;
    public TaxTreasuryPayment(String txId, double baseAmountUah,String taxCode){
        super(txId,baseAmountUah);
        this.taxCode=taxCode;
        this.refunded=false;
    }

    @Override
    public double calculateFee(){
        return 0.0;
    }

    @Override
    public String getPaymentChannel(){
        return "ДержКазначейство";
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded(){
        return refunded;
    }

}



public class Task2SmartPaymentAndAuditGateway {
    public static void auditLedger(AbstractPaymentTransaction[] ledger){
    double turnover = 0;
    double fees = 0;
    double cashbackTotal = 0;

     System.out.println("[1. ПРОВЕДЕННЯ ПАКЕТУ ТРАНЗАКЦІЙ (final process())]:");
     for (AbstractPaymentTransaction tx : ledger){
         boolean kk = tx.process();
         String text = String.format("[%s | %s]", tx.getTxId(), tx.getPaymentChannel());
         if (kk) {
             turnover += tx.getBaseAmountUah();
             fees += tx.calculateFee();
             System.out.printf("  ✅ %-24s Сума: %8.2f грн | Комісія: %6.2f грн | Всього: %8.2f грн%n",
                     text, tx.getBaseAmountUah(), tx.calculateFee(), tx.getTotalCharged());
         } else {
             System.out.printf("  ❌ %-24s платіж відхилено (сума <= 0 або вже проведено)%n", text);
         }
     }

        System.out.println("");

        System.out.println("[2. АВТОМАТИЧНИЙ АУДИТ МОЖЛИВОСТЕЙ (Pattern Matching instanceof)]:");
        for (AbstractPaymentTransaction tx : ledger){
            if (tx instanceof CashbackCapable c){
                double raw = tx.getBaseAmountUah()+ c.getCashbackRate();
                double calculated = tx.getBaseAmountUah() * c.getCashbackRate();
                double paid = c.calculateCashback(tx.getBaseAmountUah());
                cashbackTotal += paid;
                double percent = c.getCashbackRate()*100;
                if (! (tx instanceof BlockchainVerifiable)) {
                    System.out.printf("  💰 [CashbackCapable] %s: розрахунковий %.0f%% = %.0f грн -> нараховано %.2f грн (Ліміт MAX_CASHBACK_UAH!)%n",
                            tx.getTxId(), percent, calculated, paid);
                }
                else if (tx instanceof BlockchainVerifiable) {
                    String crypto =  (tx instanceof BlockchainVerifiable ) ? "крипто-бонусу" : "кешбеку";
                    System.out.printf("  💰 [CashbackCapable] %s: нараховано %.0f%% %s = %.2f грн %n",
                            tx.getTxId(), percent, crypto, calculated);
                }
            }
        }

        for (AbstractPaymentTransaction tx : ledger){
            if (tx instanceof BlockchainVerifiable b){
                System.out.printf("  ⛓️ [BlockchainVerifiable] %s: Hash=%s, Підтверджень=%d (Finalized: %b)%n",
                        tx.getTxId(), b.getTxHash(), b.getConfirmations(), b.isFinalized());
            }
        }

        for (AbstractPaymentTransaction tx : ledger){
            if (tx instanceof Refundable r){
                boolean refunded = r.requestRefund("Подвійне списання");
                System.out.printf("  🔄 [Refundable] Спроба повернення %s ('Подвійне списання'): %s%n",
                        tx.getTxId(), refunded ? "УСПІШНО ПОВЕРНЕНО!" : "ВІДМОВЛЕНО");
                break;
            }
        }
        System.out.println();
        System.out.println("[ПІДСУМКОВИЙ ЗВІТ ШЛЮЗУ]:");
        System.out.printf("  %-28s%9.2f грн%n", "Сумарний базовий оборот:", turnover);
        System.out.printf("  %-28s%9.2f грн%n", "Сумарна комісія шлюзу:", fees);
        System.out.printf("  %-28s%9.2f грн%n", "Сумарний виплачений кешбек:", cashbackTotal);
    }

    static void main() {
        AbstractPaymentTransaction[] ledger = {
                new PremiumCardPayment("TX-101", 20000.0),
                new CryptoUsdtPayment("TX-102", 8500.0, "0x9f8a...c4", 18),
                new TaxTreasuryPayment("TX-103", 5000.0, "PDV-2026")
        };
        System.out.println("================ ПРОЦЕСИНГОВИЙ ЦЕНТР FINTECH (POLYMORPHISM & INTERFACES) ================");
        auditLedger(ledger);
        System.out.println("=========================================================================================");
    }
}
