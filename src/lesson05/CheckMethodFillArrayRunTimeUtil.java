package lesson05;

import java.util.Arrays;

public class CheckMethodFillArrayRunTimeUtil {

    protected static final int SIZE = 10_000_000;

    protected static final int HALF_SIZE = SIZE / 2;

    protected static final double[] ARRAY_WITH_UNITS = new double[SIZE];

    private CheckMethodFillArrayRunTimeUtil() {
    }

    protected static void changeArrayWithOneThread() {
        long startTime = System.currentTimeMillis();

        Arrays.fill(ARRAY_WITH_UNITS, 1);

        for (int i = 0; i < SIZE; i++) {
            ARRAY_WITH_UNITS[i] = changeValueArrayElement(ARRAY_WITH_UNITS[i], i, 0);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Время выполнения метода c использованием ОДНОГО потока: " + (endTime - startTime) + " ms");
    }

    protected static void changeArrayWithTwoThreads() {
        long startTime = System.currentTimeMillis();

        Thread one = new Thread(() -> {
            Arrays.fill(ARRAY_WITH_UNITS, 0, HALF_SIZE, 1);
            double[] firstPartArray = Arrays.copyOfRange(ARRAY_WITH_UNITS, 0, HALF_SIZE);
            for (int i = 0; i < HALF_SIZE; i++) {
                firstPartArray[i] = changeValueArrayElement(firstPartArray[i], i, 0);
            }
            System.arraycopy(firstPartArray, 0, ARRAY_WITH_UNITS, 0, HALF_SIZE);
        });
        one.start();

        Thread two = new Thread(() -> {
            Arrays.fill(ARRAY_WITH_UNITS, HALF_SIZE, SIZE, 1);
            double[] secondPartArray = Arrays.copyOfRange(ARRAY_WITH_UNITS, HALF_SIZE, SIZE);
            for (int i = 0; i < HALF_SIZE; i++) {
                secondPartArray[i] = changeValueArrayElement(secondPartArray[i], i, HALF_SIZE);
            }
            System.arraycopy(secondPartArray, 0, ARRAY_WITH_UNITS, HALF_SIZE, HALF_SIZE);
        });
        two.start();

        try {
            one.join();
            two.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Время выполнения метода c использованием ДВУХ потоков: " + (endTime - startTime) + " ms");
    }

    private static double changeValueArrayElement(double oldValue, int index, int add) {
        return oldValue
                * Math.sin(0.2 + (index + add) / 5.0)
                * Math.cos(0.2 + (index + add) / 5.0)
                * Math.cos(0.4 + (index + add) / 2.0);

    }

}
