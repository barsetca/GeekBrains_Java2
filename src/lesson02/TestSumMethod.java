package lesson02;

import lesson02.exceptions.MyArrayDataException;
import lesson02.exceptions.MyArraySizeException;
import lesson02.util.SumUtil;

import java.util.Arrays;

public class TestSumMethod {

    public static void main(String[] args) {

        String[][] arrayCorrect = new String[SumUtil.ARRAY_ROWS][SumUtil.ARRAY_COLUMN];
        for (String[] strings : arrayCorrect) Arrays.fill(strings, "1");

        String[][] arraySizeError = new String[2][2];
        for (String[] strings : arraySizeError) Arrays.fill(strings, "1");

        String[][] arrayElementError = new String[SumUtil.ARRAY_ROWS][SumUtil.ARRAY_COLUMN];
        for (String[] strings : arrayElementError) Arrays.fill(strings, "one");

        String[][] arrayElementErrorNew = {{"1", "1", "1", "1"}, {"1", "1"},{"1", "1", "1", "1"},{"1", "1"}};

        System.out.println(testDate(arrayCorrect));
        System.out.println(testDate(arraySizeError));
        System.out.println(testDate(arrayElementError));
        System.out.println(testDate(arrayElementErrorNew));
    }

    public static String testDate(String[][] integers) {
        String result;
        try {
            result = String.format("Сумма элементов массива = %d", SumUtil.summarizeArraysElements(integers));
        } catch (MyArrayDataException | MyArraySizeException e) {
            result = e.getMessage();
        }
        return result;
    }
}

