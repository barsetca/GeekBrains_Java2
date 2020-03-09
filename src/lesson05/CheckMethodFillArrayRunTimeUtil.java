package lesson05;

import java.util.Arrays;

public class CheckMethodFillArrayRunTimeUtil {

    protected static final int SIZE = 10_000_000;
    protected static final int HALF_SIZE = SIZE / 2;

    private CheckMethodFillArrayRunTimeUtil() {
    }

    protected static void changeArrayWithOneThread() {
        double[] arrayForChange = new double[SIZE];
        Arrays.fill(arrayForChange, 1);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < SIZE; i++) {
            arrayForChange[i] = changeValueArrayElement(arrayForChange[i], i, 0);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Время выполнения метода c использованием ОДНОГО потока: " + (endTime - startTime) + " ms");
    }

    protected static void changeArrayWithTwoThreads() {
        double[] arrayForChange = new double[SIZE];
        Arrays.fill(arrayForChange, 1);

        long startTime = System.currentTimeMillis();
        Thread one = new Thread(() -> {
            double[] firstPartArray = Arrays.copyOfRange(arrayForChange, 0, HALF_SIZE);
            for (int i = 0; i < HALF_SIZE; i++) {
                firstPartArray[i] = changeValueArrayElement(firstPartArray[i], i, 0);
            }
            System.arraycopy(firstPartArray, 0, arrayForChange, 0, HALF_SIZE);
        });
        one.start();

        Thread two = new Thread(() -> {
            double[] secondPartArray = Arrays.copyOfRange(arrayForChange, HALF_SIZE, SIZE);
            for (int i = 0; i < HALF_SIZE; i++) {
                secondPartArray[i] = changeValueArrayElement(secondPartArray[i], i, HALF_SIZE);
            }
            System.arraycopy(secondPartArray, 0, arrayForChange, HALF_SIZE, HALF_SIZE);
        });
        two.start();

        try {
            one.join();
            two.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.print("Время выполнения метода c использованием ДВУХ потоков: " + (endTime - startTime) + " ms");
    }

    private static double changeValueArrayElement(double oldValue, int index, int add) {
        return oldValue
                * Math.sin(0.2 + (index + add) / 5.0)
                * Math.cos(0.2 + (index + add) / 5.0)
                * Math.cos(0.4 + (index + add) / 2.0);

    }

}
