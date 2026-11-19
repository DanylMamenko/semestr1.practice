package practice11;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task1WrapperAndAutoboxingMemory {

    public static void inspectIntegerCache(int[] testValues){
        for (int val : testValues){
            Integer first = Integer.valueOf(val);
            Integer second = Integer.valueOf(val);
            boolean equalReference = (first == second);
            boolean doubleEqualReference = (first.equals(second));
            String source = equalReference ? "IntegerCache" : "Новий об'єкт у Heap!";
            System.out.printf(  "Значення: %5d | (a == b): %-5b | a.equals(b): %-5b | Джерело: %s%n",
            val, equalReference, doubleEqualReference, source);
        }
    }

    public static void demonstrateAutoboxingUnderTheHood() {

        //int
        Integer unstatedInt = 42;
        Integer explicitInt = Integer.valueOf(42);
        int unboxedUnstatedInt = unstatedInt;
        int unboxedExplicitInt = explicitInt.intValue();
        System.out.print("Явне пакування: Integer.valueOf(42) -> " + explicitInt);
        System.out.println(" | Розпакування: .intValue() -> " + unboxedExplicitInt);

        //double
        Double unstatedDouble = 42.0;
        Double explicitDouble = Double.valueOf(42.0);
        double unboxedUnstatedDouble = unstatedDouble;
        double unboxedExplicitDouble = explicitDouble.doubleValue();
        System.out.print("Явне пакування: Double.valueOf(42) -> " + explicitDouble);
        System.out.println(" | Розпакування: .doubleValue() -> " + unboxedExplicitDouble);

        //boolean
        Boolean unstatedBoolean = true;
        Boolean explicitBoolean = Boolean.valueOf(true);
        boolean unboxedUnstatedBoo = unstatedBoolean;
        boolean unboxedExplicitBoo = explicitBoolean.booleanValue();
        System.out.print("Явне пакування: Boolean.valueOf(true) -> " + explicitBoolean);
        System.out.println(" | Розпакування: .booleanValue() -> " + unboxedExplicitBoo);

        //null
        //Для прикладу роботи методу joinIndices я додав ще один null за 6 індексом
        List<Integer> sensors = new ArrayList<>(Arrays.asList(45, null, -12, 88, null, 102 , null));
        System.out.println("Вхідний список із датчиків (з null):" + sensors);
        List<Integer> nullIndices = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < sensors.size(); i++) {
            Integer boxed = sensors.get(i);
            if (boxed == null) {
                nullIndices.add(i);
                continue;
            }
            sum += boxed.intValue();
        }

        System.out.print("⚠ Пропущено null-елемент на індексі " + joinIndecies(nullIndices));
        System.out.println("(запобігання NullPointerException при intValue()!)");
        System.out.println("Сума валідних розпакованих елементів: " + sum);


    }

    // method inserts "та" between indices
    private static String joinIndecies(List<Integer> indicies){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indicies.size(); i++){
            if (i > 0){
                sb.append(" та ");
            }
            sb.append(indicies.get(i));
        }
        return sb.toString();
    }

    public static List<Integer> parseTelemetryStream(String[] rawPackets){
        List<Integer> result = new ArrayList<>();
        for (String packets : rawPackets) {
            if (packets == null || packets.trim().isEmpty()) {
                System.out.println("  ❌ Порожній або null-пакет відхилено");
                continue;
            }
            String cleaned = packets.trim();
            try {
                result.add(Integer.parseInt(cleaned));
            } catch (NumberFormatException n) {
                System.out.println("  ❌ Пакет '" + cleaned + "' відхилено (NumberFormatException)");
            }
        }
        return result;
    }

    //max min searcher
    public static void printExtremesAndStats(List<Integer> validData){
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        long sum = 0;

        for (Integer value : validData){
            int current = value;
            if (current < min){
                min = current;
            }
            if (current > max){
                max = current;
            }
            sum += current;
        }
        System.out.println( "Знайдений мінімум: " + min + " | Знайдений максимум: " + max
                + " | Сума (long): " + sum);

        //byValue remove
        List<Integer> byValue = new ArrayList<>(validData);
        Boolean removed = byValue.remove(Integer.valueOf(-30));
        System.out.println("  Після remove(Integer.valueOf(-30)) (за значенням, removed=" + removed + "): " + byValue);


        //byIndex remove
        List<Integer> byIndex = new ArrayList<>(validData);
        Integer removedByIndex = byIndex.remove(0);
        System.out.println("  Після remove(0) (за індексом, видалено " + removedByIndex + "): " + byIndex);
    }

    static void main(String[] args) {
        System.out.println("================ ДОСЛІДЖЕННЯ WRAPPER CLASSES ТА INTEGER CACHE ================");

        System.out.println("[1. ЕКСПЕРИМЕНТ: INTEGER CACHE [-128..127] (== проти .equals())]:");
        inspectIntegerCache(new int[]{-128, -50, 0, 127, 128, 500, 2026});
        System.out.println();
        System.out.println("[2. AUTOBOXING / UNBOXING ПІД КАПОТОМ ТА ЗАХИСТ ВІД NULL]:");
        demonstrateAutoboxingUnderTheHood();

        System.out.println();
        System.out.println("[3. ПАРСИНГ РЯДКІВ (Integer.parseInt) ТА ЕКСТРЕМУМИ (MAX_VALUE / MIN_VALUE)]:");
        System.out.println("  Початкові межі пошуку: min = " + Integer.MAX_VALUE + " (MAX_VALUE), max = "
                + Integer.MIN_VALUE + " (MIN_VALUE)");
        String[] rawPackets = {"  145 ", "-30", "ERR_404", "980", "", null, "2147483647", "-85"};
        List<Integer> validData = parseTelemetryStream(rawPackets);
        System.out.println("  Успішно розпізнаний список Integer: " + validData);
        printExtremesAndStats(validData);

        System.out.println("==============================================================================");
    }
}
