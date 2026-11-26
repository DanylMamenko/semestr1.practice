package practice12;

import java.util.*;

abstract class Transport{
    private final String name;
    private final double speedKmH;

    public Transport(String name, double speedKmH){
        this.name = name;
        this.speedKmH = speedKmH;
    }

    public double calculateTimeHours(int distanceKm){
        return distanceKm/speedKmH;
    }

    public String getName() {
        return name;
    }

    public double getSpeedKmH() {
        return speedKmH;
    }

    public abstract double calculateCostUah(int distanceKm);
}

class Bus extends Transport{
    public Bus() {
        super ("Bus", 80.0);
    }

    @Override
    public double calculateCostUah(int distanceKm){
        return distanceKm * 2.5;
    }
}

class Train extends Transport{
    public Train() {
        super("Train", 100.0);
    }

    @Override
    public double calculateCostUah(int distanceKm){
        return distanceKm * 1.8 + 150;
    }
}

class DeliveryTruck extends Transport{
    public DeliveryTruck(){
        super("DeliveryTruck", 70.0);
    }

    @Override
    public double calculateCostUah(int distanceKm){
        return distanceKm * 4.0 + 300.0;
    }
}

class TripOrder {
    private final String orderId;
    private final String fromCity;
    private final String toCity;
    private final Transport transport;

    public TripOrder(String orderId, String fromCity, String toCity, Transport transport) {
        this.orderId = orderId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.transport = transport;
    }

    public String getOrderId() { return orderId; }
    public String getFromCity() { return fromCity; }
    public String getToCity() { return toCity; }
    public Transport getTransport() { return transport; }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TripOrder) {
            TripOrder other = (TripOrder) o;
            return orderId.equals(other.orderId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return orderId.hashCode();
    }
}

class RouteDispatcher{
    private final ArrayList<String> cities;
    private final int[][] distances;
    private final HashSet<String> closedCities;
    private final ArrayList<TripOrder> orders = new ArrayList<>();

    public RouteDispatcher(ArrayList<String> cities, int[][] distances, HashSet<String> closedCities) {
        this.cities = new ArrayList<>(cities);
        this.distances = new int[distances.length][];
        for (int i = 0; i < distances.length; i++) {
            this.distances[i] = distances[i].clone();
        }
        this.closedCities = new HashSet<>(closedCities);
    }

    public void addOrder(TripOrder order) {
        orders.add(order);
    }

    public int getDistance(String from, String to) {
        int i = cities.indexOf(from);
        int j = cities.indexOf(to);
        if (i != -1 && j != -1) {
            return distances[i][j];
        }
        return 0;
    }

    public HashSet<String> getDirectNeighbors(String city) {
        HashSet<String> result = new HashSet<>();
        int idx = cities.indexOf(city);
        if (idx == -1) return result;
        for (int j = 0; j < distances[idx].length; j++) {
            if (distances[idx][j] > 0) {
                result.add(cities.get(j));
            }
        }
        return result;
    }

    public HashSet<String> getCommonNeighbors(String cityA, String cityB) {
        HashSet<String> result = new HashSet<>(getDirectNeighbors(cityA));
        result.retainAll(getDirectNeighbors(cityB));
        return result;
    }

    public HashSet<String> getExclusiveNeighbors(String cityA, String cityB) {
        HashSet<String> result = new HashSet<>(getDirectNeighbors(cityA));
        result.removeAll(getDirectNeighbors(cityB));
        return result;
    }

    public int filterInvalidOrders() {
        HashSet<String> seenIds = new HashSet<>();
        int removed = 0;
        Iterator<TripOrder> it = orders.iterator();
        while (it.hasNext()) {
            TripOrder o = it.next();
            String route = o.getFromCity() + " -> " + o.getToCity();
            if (!seenIds.add(o.getOrderId())) {
                System.out.println("  ❌ [ДУБЛІКАТ]       " + o.getOrderId() + " (" + route + ") — видалено!");
                it.remove();
                removed++;
            } else if (closedCities.contains(o.getFromCity()) || closedCities.contains(o.getToCity())) {
                System.out.println("  ❌ [МІСТО ЗАКРИТЕ]  " + o.getOrderId() + " (" + route + ") — видалено!");
                it.remove();
                removed++;
            } else if (getDistance(o.getFromCity(), o.getToCity()) <= 0) {
                System.out.println("  ❌ [НЕМАЄ ДОРОГИ]   " + o.getOrderId() + " (" + route + ") — видалено!");
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    public void printReport(int removedCount) {
        System.out.println("--- 3. Схвалені рейси (залишилось " + orders.size() + ") ---");
        int totalDistance = 0;
        double totalCost = 0;
        for (TripOrder o : orders) {
            int d = getDistance(o.getFromCity(), o.getToCity());
            Transport t = o.getTransport();
            double time = t.calculateTimeHours(d);
            double cost = t.calculateCostUah(d);
            totalDistance += d;
            totalCost += cost;
            System.out.println(String.format("  ✅ %s | %s -> %s | %d км | %-13s | Час: %.2f год | Вартість: %.2f грн",
                    o.getOrderId(), o.getFromCity(), o.getToCity(), d, t.getName(), time, cost));
        }
        System.out.println();
        System.out.println("--- Підсумок ---");
        System.out.println("Видалено заявок: " + removedCount + " | Схвалено: " + orders.size());
        System.out.printf("Загальна відстань: %d км | Загальна вартість перевезень: %.2f грн%n", totalDistance, totalCost);
        System.out.println("=======================================================================");
    }
}

public class Task4MatrixAndCityNetwork{
    static void main(String[] args) {
        ArrayList<String> cities = new ArrayList<>(List.of("Київ", "Львів", "Одеса", "Дніпро"));
        int[][] distances = {
                {  0, 540, 480, 450},
                {540,   0, 790,   0},
                {480, 790,   0, 460},
                {450,   0, 460,   0}
        };
        HashSet<String> closed = new HashSet<>(Set.of("Одеса"));

        RouteDispatcher dispatcher = new RouteDispatcher(cities,distances,closed);
        dispatcher.addOrder(new TripOrder("ORD-1", "Київ", "Львів", new Train()));
        dispatcher.addOrder(new TripOrder("ORD-1", "Київ", "Львів", new Bus()));
        dispatcher.addOrder(new TripOrder("ORD-2", "Київ", "Одеса", new Bus()));
        dispatcher.addOrder(new TripOrder("ORD-3", "Львів", "Дніпро", new DeliveryTruck()));
        dispatcher.addOrder(new TripOrder("ORD-4", "Київ", "Дніпро", new DeliveryTruck()));
        System.out.println("=============== 🚚 ЛОГІСТИЧНИЙ ДИСПЕТЧЕР: МАТРИЦЯ ТА МЕРЕЖА 🚚 ===============");
        System.out.println("--- 1. Аналіз мережі доріг (2D-масив -> HashSet) ---");
        Set<String> kyiv = dispatcher.getDirectNeighbors("Київ");
        Set<String> lviv = dispatcher.getDirectNeighbors("Львів");
        System.out.println("  Прямі сусіди міста Київ [" + kyiv.size() + "]:   " + kyiv);
        System.out.println("  Прямі сусіди міста Львів [" + lviv.size() + "]:   " + lviv);
        System.out.println("  Спільні сусіди Київ ∩ Львів:   " + dispatcher.getCommonNeighbors("Київ", "Львів"));
        System.out.println("  Тільки з Києва (Київ \\ Львів): " + dispatcher.getExclusiveNeighbors("Київ", "Львів"));
        System.out.println();

        System.out.println("--- 2. Фільтрація заявок через Iterator.remove() (Закриті міста: " + closed + ") ---");
        int removed = dispatcher.filterInvalidOrders();
        System.out.println();

        dispatcher.printReport(removed);
    }
}