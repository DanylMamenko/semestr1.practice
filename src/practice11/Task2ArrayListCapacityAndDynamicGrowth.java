package practice11;

public class Task2ArrayListCapacityAndDynamicGrowth {

    static class CustomArrayCapacityEmulator<E> {
        private static final int DEFAULT_CAPACITY = 10;
        private Object[] elementData;
        private int size = 0;

        private int resizeCount = 0;
        private int totalElementsCopied = 0;

        public CustomArrayCapacityEmulator() {
            this.elementData = new Object[DEFAULT_CAPACITY];
        }

        private void ensureCapacityInternal() {
            if (size == elementData.length) {
                int oldCapacity = elementData.length;
                int newCapacity = oldCapacity + (oldCapacity >> 1); // 10 -> 15 -> 22 -> 33 -> 49 ...
                Object[] newBuffer = new Object[newCapacity];
                System.arraycopy(elementData, 0, newBuffer, 0, size);
                elementData = newBuffer;
                resizeCount++;
                totalElementsCopied += size;
            }
        }

        public void add(E element) {
            ensureCapacityInternal();
            elementData[size++] = element;
        }

        public void add(int index, E element) {
            if (0 <= index || index < size) {
                ensureCapacityInternal();
                System.arraycopy(elementData, index, elementData, index + 1, size - index);
                elementData[index] = element;
                size++;
            } else {
                throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }

        //метод чек для зручності
        public void checkIndex(int index) {
            if (0 > index || index >= size) {
                throw new ArrayIndexOutOfBoundsException("index" + index + "size" + size);
            }
        }

        public E remove(int index) {
            checkIndex(index);
            E oldValue = (E) elementData[index];
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(elementData, index + 1, elementData, index, numMoved);
            }
            elementData[--size] = null;
            return oldValue;
        }

        public E get(int index) {
            checkIndex(index);
            return (E) elementData[index];
        }

        public E set(int index, E element) {
            checkIndex(index);
            E oldValue = (E) elementData[index];
            elementData[index] = element;
            return oldValue;
        }

        public int size() {
            return size;
        }

        public int getCapacity() {
            return elementData.length;
        }

        public void trimToSize() {
            if (size < elementData.length) {
                Object[] trimmed = new Object[size];
                System.arraycopy(elementData, 0, trimmed, 0, size);
                elementData = trimmed;
            }
        }

        public int getResizeCount() {
            return resizeCount;
        }

        public int getTotalElementsCopied() {
            return totalElementsCopied;
        }
    }

    static void main(String[] args) {
        var emulator = new CustomArrayCapacityEmulator<String>();
        // УВАГА: var raw = new ArrayList<>();
        // тип виводиться як ArrayList<Object> -> втрата типобезпеки!
        System.out.println("[ІНІЦІАЛІЗАЦІЯ]: Створено CustomArrayCapacityEmulator<String> | size = "
                + emulator.size() + " | capacity = " + emulator.getCapacity());

        System.out.println("");
        System.out.println("--- Додавання 35 елементів у список ---");
        int growCounter = 0;
        for (int i = 0; i < 35; i++) {
            int oldCapacity = emulator.getCapacity();
            int beforeChange = emulator.getTotalElementsCopied();
            emulator.add("Block-" + i);
            int newCapacity = emulator.getCapacity();
            if (newCapacity != oldCapacity) {
                growCounter++;
                System.out.println("⚡ [GROW #1] Переповнення при додаванні " +
                        (i+1) + "-го елемента! Ємність: " + oldCapacity + "->" + newCapacity +
                        "(System.arraycopy скопіював " + (emulator.totalElementsCopied - beforeChange)
                        + "ел.)");
            }
        }
        System.out.println("Стан після додавання 35 елементів: size = " + emulator.size() +
                "| capacity = " + emulator.getCapacity() + "(Вільний резерв: " +
                (emulator.getCapacity() - emulator.size()) + " комірок null)");
        System.out.println("");
        System.out.println("--- Тестування вставки add(0, 'HEADER') та видалення remove(5) ---");
        int sizeBeforeAdd = emulator.size();
        emulator.add(0, "HEADER");
        System.out.println("  -> Вставлено 'HEADER' на індекс 0 (зсунуто праворуч " + sizeBeforeAdd
                + " елементів через System.arraycopy)");

        int removeIndex = 5;
        int movedLeft = emulator.size() - removeIndex - 1;
        String removed = emulator.remove(removeIndex);
        System.out.println("  -> Видалено елемент '" + removed + "' з індексу " + removeIndex
                + " (зсунуто ліворуч " + movedLeft + " елементів, хвіст занулено для GC)");
        System.out.println("  Поточний стан: size = " + emulator.size() + " | capacity = " + emulator.getCapacity()
                + " | Перший: '" + emulator.get(0) + "' | Останній: '" + emulator.get(emulator.size()-1) + "'");
        System.out.println();
        System.out.println("--- Оптимізація пам'яті trimToSize() ---");
        int capacityBeforeTrim = emulator.getCapacity();
        emulator.trimToSize();
        System.out.println("  -> Викликано trimToSize(): буфер Object[" + capacityBeforeTrim
                + "] стиснуто рівно до Object[" + emulator.getCapacity() + "]!");
        System.out.println("  Фінальна телеметрія: size = " + emulator.size()
                + " | capacity = " + emulator.getCapacity()
                + " | Кількість розширень: " + emulator.getResizeCount());
        System.out.println("=====================================================================================");
    }
}
