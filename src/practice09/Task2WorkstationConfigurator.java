package practice09;

import com.sun.java.accessibility.util.AccessibilityListenerList;

class Motherboard{
    private final String model;
    private final String socketType;
    private final int maxExpansionSlots;

    public Motherboard(String model, String socketType, int maxExpansionSlots){
        if (maxExpansionSlots < 1 || maxExpansionSlots > 8) {
            System.out.println("Кількість слотів має бути в межах 1..8");
            this.maxExpansionSlots = 1;
            this.model = model;
            this.socketType = socketType;
        } else {
        this.model = model;
        this.socketType = socketType;
        this.maxExpansionSlots = maxExpansionSlots;
        }
    }

    public String getModel() {
        return model;
    }

    public String getSocketType() {
        return socketType;
    }

    public int getMaxExpansionSlots() {
        return maxExpansionSlots;
    }

    public boolean isSocketCompatible(String cpuSocket){
        return socketType.equalsIgnoreCase(cpuSocket);
    }
}

class PowerSupplyUnit{
    private final String model;
    private final int maxOutputWatts;
    private final double efficiencyCoefficient;

    public PowerSupplyUnit(String model, int maxOutputWatts, double efficiencyCoefficient){
        if (efficiencyCoefficient < 0.80 || efficiencyCoefficient > 0.96) {
            System.out.println("ККД має бути в межах [0.80; 0.96]");
            this.efficiencyCoefficient = 0.80;
            this.model = model;
            this.maxOutputWatts = maxOutputWatts;
        } else {
            this.model = model;
            this.maxOutputWatts = maxOutputWatts;
            this.efficiencyCoefficient = efficiencyCoefficient;
        }
    }

    public String getModel() {
        return model;
    }

    public double getEfficiencyCoefficient() {
        return efficiencyCoefficient;
    }

    public int getMaxOutputWatts() {
        return maxOutputWatts;
    }

    public int getSafeContinuousWatts(){
        return (int)(maxOutputWatts * 0.85);
    }

    public double calculateWallPowerDraw(int internalLoadWatts) {
        return internalLoadWatts / efficiencyCoefficient;
    }
}

abstract class AbstractHardwareComponent{
    private final String modelName;
    private final double priceUah;

    public AbstractHardwareComponent(String modelName, double priceUah){
        if (priceUah < 0){
            System.out.println("Ціна не може бути від'ємною");
            this.priceUah = 1;
            this.modelName=modelName;
        } else {
            this.priceUah = priceUah;
            this.modelName=modelName;
        }
    }

    public double getPriceUah() {
        return priceUah;
    }

    public String getModelName() {
        return modelName;
    }

    public abstract int getPowerWatts();
    public abstract double getBenchmarkScore();
    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("[%s] %s (%d Вт | Score: %.1f)",
                getCategory(), modelName, getPowerWatts(), getBenchmarkScore());
    }
}

class ProcessorUnit extends AbstractHardwareComponent {
    private final String socket;
    private final int cores;
    private final double boostClockGhz;
    private final int tdpWatts;

    public ProcessorUnit(String modelName, double priceUah, String socket,
                         int cores, double boostClockGhz, int tdpWatts) {
        super(modelName, priceUah);
        this.socket = socket;
        this.cores = cores;
        this.boostClockGhz = boostClockGhz;
        this.tdpWatts = tdpWatts;
    }

    public String getSocket() {
        return socket;
    }

    @Override
    public int getPowerWatts() {
        return tdpWatts;
    }

    @Override
    public double getBenchmarkScore() {
        return cores * boostClockGhz * 120.0;
    }

    @Override
    public String getCategory() {
        return "CPU";
    }
}
class GraphicsAccelerator extends AbstractHardwareComponent{
    private final int vramGb;
    private final int tensorCores;
    private final int boardPowerWatts;

    public GraphicsAccelerator (String modelName, double priceUah,int vramGb,
                                int tensorCores, int boardPowerWatts){
        super(modelName, priceUah);
        this.vramGb = vramGb;
        this.tensorCores = tensorCores;
        this.boardPowerWatts = boardPowerWatts;
    }

    @Override public int getPowerWatts() {
        return boardPowerWatts;
    }

    @Override public double getBenchmarkScore(){
        return vramGb * 250.0 + tensorCores * 1.5;
    }

    @Override
    public String getCategory() {
        return "GPU";
    }
}

class NvmeSsdDrive extends AbstractHardwareComponent{
    private final int capacityGb;
    private final int readSpeedMbSec;
    private final int[] speedSamples;

    public NvmeSsdDrive(String modelName, double priceUah, int capacityGb,
                        int readSpeedMbSec, int[] speedSamples){
        super(modelName, priceUah);
        this.capacityGb = capacityGb;
        this.readSpeedMbSec = readSpeedMbSec;
        this.speedSamples = (speedSamples != null) ? speedSamples.clone() : new int[0];
    }


    public int[] getSpeedSamples() {
        return speedSamples.clone();
    }

    @Override
    public int getPowerWatts() {
        return 12;
    }

    @Override
    public double getBenchmarkScore() {
        return readSpeedMbSec * 0.25;
    }

    @Override
    public String getCategory() {
        return "NVMe SSD";
    }
}

class WorkstationPC {
    private final String buildName;
    private final Motherboard motherboard;
    private final PowerSupplyUnit psu;
    private final AbstractHardwareComponent[] components;
    private int componentCount;

    public WorkstationPC(String buildName, Motherboard motherboard, PowerSupplyUnit psu) {
        this.buildName = buildName;
        this.motherboard = motherboard;
        this.psu = psu;
        this.components = new AbstractHardwareComponent[motherboard.getMaxExpansionSlots()];
        this.componentCount = 0;
    }

    public boolean installComponent(AbstractHardwareComponent comp) {
        if (comp == null) {
            System.out.println("❌ Відмова: компонент не вказано!");
            return false;
        }

        // 1. Вільний слот
        if (componentCount >= motherboard.getMaxExpansionSlots()) {
            System.out.println("❌ Відмова: немає вільних слотів на платі "
                    + motherboard.getModel() + "!");
            return false;
        }

        // 2. Сумісність сокета для процесора
        if (comp instanceof ProcessorUnit) {
            ProcessorUnit cpu = (ProcessorUnit) comp;
            if (!motherboard.isSocketCompatible(cpu.getSocket())) {
                System.out.printf("❌ Відмова: Процесор %s (%s) несумісний із платою %s (Сокет %s)!%n",
                        cpu.getModelName(), cpu.getSocket(),
                        motherboard.getModel(), motherboard.getSocketType());
                return false;
            }
        }

        // 3. Ліміт блока живлення
        if (getTotalPowerWatts() + comp.getPowerWatts() > psu.getSafeContinuousWatts()) {
            System.out.printf("❌ Відмова: %s перевищує безпечний ліміт БЖ (%d Вт) — ризик перевантаження!%n",
                    comp.getModelName(), psu.getSafeContinuousWatts());
            return false;
        }

        components[componentCount++] = comp;
        System.out.println("✅ Встановлено: " + comp);
        return true;
    }

    public int getTotalPowerWatts() {
        int sum = 0;
        for (int i = 0; i < componentCount; i++) {
            sum += components[i].getPowerWatts();
        }
        return sum;
    }

    public double getTotalSystemScore() {
        double sum = 0;
        for (int i = 0; i < componentCount; i++) {
            sum += components[i].getBenchmarkScore();
        }
        return sum;
    }

    public double getTotalBuildPrice() {
        double sum = 0;
        for (int i = 0; i < componentCount; i++) {
            sum += components[i].getPriceUah();
        }
        return sum;
    }

    @Override
    public String toString() {
        int power = getTotalPowerWatts();
        String status = (power <= psu.getSafeContinuousWatts()) ? "СТАБІЛЬНО!" : "ПЕРЕВАНТАЖЕННЯ!";
        return String.format(
                "[ПАСПОРТ ЗБІРКИ '%s']:%n"
                        + "  Материнська плата: %s (Socket: %s, Слотів: %d/%d)%n"
                        + "  Блок живлення:     %s (Безпечний ліміт 85%%: %d Вт, ККД: %.0f%%)%n"
                        + "  Внутрішнє споживання: %d Вт (з розетки з урахуванням ККД: %.2f Вт) -> %s%n"
                        + "  Сумарний індекс продуктивності: %.2f балів%n"
                        + "  Загальна вартість конфігурації: %.2f грн",
                buildName,
                motherboard.getModel(), motherboard.getSocketType(),
                componentCount, motherboard.getMaxExpansionSlots(),
                psu.getModel(), psu.getSafeContinuousWatts(), psu.getEfficiencyCoefficient() * 100,
                power, psu.calculateWallPowerDraw(power), status,
                getTotalSystemScore(), getTotalBuildPrice());
    }
}

public class Task2WorkstationConfigurator {
    static void main(String[] args) {
                Motherboard motherboard = new Motherboard("ASUS ROG B650", "AM5", 4);
                PowerSupplyUnit psu = new PowerSupplyUnit("Seasonics 750W Gold", 750, 0.90);
                WorkstationPC pc = new WorkstationPC("DeepLearning-Rig", motherboard, psu);

                ProcessorUnit intel = new ProcessorUnit("Intel Core i9", 20000, "LGA1700", 24, 5.8, 253);
                ProcessorUnit ryzen = new ProcessorUnit("AMD Ryzen 9 7950X", 25000, "AM5", 16, 5.7, 170);
                GraphicsAccelerator gpu = new GraphicsAccelerator("NVIDIA RTX 4080 Super", 45000, 16, 552, 320);

                // Перевірка захисного копіювання масиву в NvmeSsdDrive
                int[] samples = {7400, 7450, 7430};
                NvmeSsdDrive ssd = new NvmeSsdDrive("Samsung 990 Pro 2TB", 8900, 2000, 7450, samples);
                samples[0] = 0;                   // зміна вихідного масиву
                ssd.getSpeedSamples()[1] = 0;     // зміна копії, отриманої з геттера
                if (ssd.getSpeedSamples()[0] != 7400 || ssd.getSpeedSamples()[1] != 7450) {
                    System.out.println("Помилка: захисне копіювання в NvmeSsdDrive не працює!");
                }

                System.out.println("================ КОНФІГУРАТОР РОБОЧОЇ СТАНЦІЇ (КОМПОЗИЦІЯ) ================");
                System.out.println("[ПЕРЕВІРКА СУМІСНОСТІ]:");
                pc.installComponent(intel);   // несумісний сокет
                pc.installComponent(ryzen);
                pc.installComponent(gpu);
                pc.installComponent(ssd);

                System.out.println();
                System.out.println(pc);
                System.out.println("=".repeat(75));
            }
}