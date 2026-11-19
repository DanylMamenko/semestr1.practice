package practice11;

import java.util.*;

public class Task3ListHierarchyAndSafeIterator {


    public static void demonstrateFailFastCrash(List<String> logs) {
        try {
            for (String log : logs) {
                if (log.contains("CORRUPTED")) {
                    logs.remove(log);
                    System.out.println("  ❌ Спроба викликати logs.remove() всередині for-each...");
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("⚡ Перехоплено ConcurrentModificationException! " +
                    "Причина: пряма зміна списку порушила expectedModCount ітератора.");
        }
    }

    public static int purgeLogsByKeywordSafely(List<String> logs, String keyword) {
        Iterator<String> it = logs.iterator();
        int removed = 0;
        while (it.hasNext()) {
            String current = it.next();
            if (current == null || current.isEmpty() || current.contains(keyword)) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    public static void enrichCriticalLogsWithListIterator(List<String> logs) {
        ListIterator<String> listIt = logs.listIterator();
        while (listIt.hasNext()) {
            String trimmed = listIt.next().trim();
            listIt.set(trimmed);
            if (trimmed.startsWith("ERROR:")) {
                listIt.add("[ALERT-TRIGGERED]");
            }
        }
    }

    public static List<String> collectReverseOrder(List<String> logs){
        List<String> reversed = new ArrayList<>();
        ListIterator<String> listIt = logs.listIterator(logs.size());
        while (listIt.hasPrevious()){
            String current = listIt.previous();
            reversed.add(current);
        }
        return reversed;
    }

    static void main(String[] args) {
        List<String> rawLogs = Arrays.asList(
                "INFO: Auth OK",
                "CORRUPTED_PACKET_01",
                "  ERROR: DB Timeout  ",
                "DEBUG: Ping",
                "CORRUPTED_PACKET_02",
                null,
                "ERROR: Disk Full",
                "INFO: Logout");
            List<String> arrayLogs = new ArrayList<>(rawLogs);
            List<String> linkedLogs = new LinkedList<>(rawLogs);

        System.out.println("================ ІЄРАРХІЯ LIST ТА БЕЗПЕЧНИЙ ITERATOR ================\n" +
                "Початковий потік логів (8 записів):");
        System.out.println(rawLogs);
        System.out.println("");
        System.out.println("[1. ДЕМОНСТРАЦІЯ ПАСТКИ FAIL-FAST У ЦИКЛІ FOR-EACH]:");
        demonstrateFailFastCrash(new ArrayList<>(rawLogs));
        System.out.println("");
        System.out.println("[2. ПОЛІМОРФНА ОБРОБКА ARRAYLIST<STRING> ЧЕРЕЗ ITERATOR]:");
        int removedArray = purgeLogsByKeywordSafely(arrayLogs, "CORRUPTED");
        System.out.println("-> Безпечно видалено пошкоджених/null записів через it.remove():" + removedArray + "шт.");
        System.out.println("-> Залишилося чистих логів: " + arrayLogs);
        enrichCriticalLogsWithListIterator(arrayLogs);
        System.out.println("-> Модифікація через ListIterator (set + add після ERROR)\n " + arrayLogs);
        System.out.println("");
        System.out.println("[3. ПОЛІМОРФНА ОБРОБКА LINKEDLIST<STRING> ТИМИ САМИМИ МЕТОДАМИ]:");
        purgeLogsByKeywordSafely(linkedLogs, "CORRUPTED");
        enrichCriticalLogsWithListIterator(linkedLogs);
        System.out.println("-> Передано LinkedList у ті самі методи List<String>...");
        System.out.println("-> Результат LinkedList повністю збігається з ArrayList: " + arrayLogs.equals(linkedLogs) +
                "(Розмір: " + linkedLogs.size() + ")");
        System.out.println("-> Зворотний обхід через ListIterator (hasPrevious / previous):\n" + collectReverseOrder(linkedLogs));
        System.out.println("=====================================================================");




    }
}


